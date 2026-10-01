package br.com.empresa.repository;

import br.com.empresa.database.ConexaoBanco;
import br.com.empresa.model.HistoricoVenda;
import br.com.empresa.model.ItemVenda;
import br.com.empresa.model.RelatorioVendedor;
import br.com.empresa.model.Venda;

import java.sql.*;
import java.util.List;

public class VendaRepository {

    public void salvar(Venda venda) throws SQLException {
        String sqlVenda = "INSERT INTO vendas (id_cliente, id_vendedor, subtotal, desconto, valor_total, forma_pagamento, parcelas) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO itens_venda (id_venda, id_produto, quantidade, preco_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        // 🔥 NOVO: Comando SQL para subtrair o estoque
        String sqlAtualizaEstoque = "UPDATE produtos SET estoque = estoque - ? WHERE id = ?";

        Connection conn = null;

        try {
            conn = ConexaoBanco.conectar();
            // Desliga o commit automático (Inicia a Transação)
            conn.setAutoCommit(false);

            // 1. Salvar a Venda (Cabeçalho)
            int idVendaGerado = 0;
            try (PreparedStatement stmtVenda = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS)) {
                stmtVenda.setInt(1, venda.getClienteId());
                stmtVenda.setInt(2, venda.getVendedorId());
                stmtVenda.setDouble(3, venda.getSubtotal());
                stmtVenda.setDouble(4, venda.getValorDesconto());
                stmtVenda.setDouble(5, venda.getTotal());
                stmtVenda.setString(6, venda.getFormaPagamento());
                stmtVenda.setInt(7, venda.getParcelas());

                stmtVenda.executeUpdate();

                ResultSet rs = stmtVenda.getGeneratedKeys();
                if (rs.next()) {
                    idVendaGerado = rs.getInt(1);
                } else {
                    throw new SQLException("Falha ao obter o ID da venda.");
                }
            }

            // 2. Salvar os Itens e Atualizar Estoque
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtEstoque = conn.prepareStatement(sqlAtualizaEstoque)) { // 🔥 Adicionado o statement do estoque

                for (ItemVenda item : venda.getItens()) {
                    // Prepara o Insert do Item
                    stmtItem.setInt(1, idVendaGerado);
                    stmtItem.setInt(2, item.getProdutoId());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setDouble(4, item.getPrecoUnitario());
                    stmtItem.setDouble(5, item.getSubtotal());
                    stmtItem.addBatch();

                    // 🔥 Prepara o Update do Estoque (Estoque atual menos a quantidade vendida)
                    stmtEstoque.setInt(1, item.getQuantidade()); // O valor a ser subtraído
                    stmtEstoque.setInt(2, item.getProdutoId());  // O ID do produto
                    stmtEstoque.addBatch();
                }

                stmtItem.executeBatch(); // Executa todos os inserts de itens de uma vez
                stmtEstoque.executeBatch(); // 🔥 Executa todos os updates de estoque de uma vez
            }

            // Se chegou até aqui sem dar nenhum erro, confirma a gravação no banco
            conn.commit();

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Cancela TUDO (venda, itens e estoque) se der erro!
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Erro ao salvar a venda. Transação cancelada: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<HistoricoVenda> listarHistorico() {
        List<HistoricoVenda> lista = new java.util.ArrayList<>();

        // O INNER JOIN junta as tabelas onde as chaves (IDs) batem.
        // O COALESCE garante que se o nome for nulo, mostre 'Desconhecido'.
        String sql = "SELECT v.id, " +
                "COALESCE(c.nome, 'Cliente Excluído') AS cliente, " +
                "COALESCE(ven.nome, 'Vendedor Excluído') AS vendedor, " +
                "v.data_hora, v.valor_total, v.forma_pagamento " +
                "FROM vendas v " +
                "LEFT JOIN clientes c ON v.id_cliente = c.id " +
                "LEFT JOIN vendedores ven ON v.id_vendedor = ven.id " +
                "ORDER BY v.data_hora DESC"; // Traz as vendas mais recentes primeiro

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HistoricoVenda h = new HistoricoVenda();
                h.setIdVenda(rs.getInt("id"));
                h.setNomeCliente(rs.getString("cliente"));
                h.setNomeVendedor(rs.getString("vendedor"));

                // Pega o Timestamp do banco e converte para LocalDateTime do Java
                Timestamp ts = rs.getTimestamp("data_hora");
                if (ts != null) {
                    h.setDataVenda(ts.toLocalDateTime());
                }

                h.setValorTotal(rs.getDouble("valor_total"));
                h.setFormaPagamento(rs.getString("forma_pagamento"));

                lista.add(h);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar histórico de vendas: " + e.getMessage());
        }
        return lista;
    }

    public List<RelatorioVendedor> buscarVendasPorVendedor() {
        List<RelatorioVendedor> relatorio = new java.util.ArrayList<>();
        String sql = "SELECT ven.nome, COUNT(v.id) as qtd, SUM(v.valor_total) as total " +
                "FROM vendas v " +
                "INNER JOIN vendedores ven ON v.id_vendedor = ven.id " +
                "GROUP BY ven.nome " +
                "ORDER BY total DESC";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                relatorio.add(new RelatorioVendedor(
                        rs.getString("nome"),
                        rs.getInt("qtd"),
                        rs.getDouble("total"),
                        rs.getDouble("comissao")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relatorio;
    }

    public double buscarTotalGeralLoja() {
        String sql = "SELECT SUM(valor_total) FROM vendas";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }

    public List<RelatorioVendedor> buscarVendasPorVendedorPeriodo(java.time.LocalDate inicio, java.time.LocalDate fim) {
        List<RelatorioVendedor> relatorio = new java.util.ArrayList<>();

        // Adicionamos 'ven.percentual_comissao' no SELECT e no GROUP BY
        String sql = "SELECT ven.nome, ven.percentual_comissao, COUNT(v.id) as qtd, SUM(v.valor_total) as total " +
                "FROM vendas v " +
                "INNER JOIN vendedores ven ON v.id_vendedor = ven.id " +
                "WHERE v.data_hora::date BETWEEN ? AND ? " +
                "GROUP BY ven.nome, ven.percentual_comissao " + // Agrupar pela comissão também é necessário
                "ORDER BY total DESC";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, java.sql.Date.valueOf(inicio));
            stmt.setDate(2, java.sql.Date.valueOf(fim));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // AGORA ENVIAMOS OS 4 PARÂMETROS:
                    relatorio.add(new RelatorioVendedor(
                            rs.getString("nome"),
                            rs.getInt("qtd"),
                            rs.getDouble("total"),
                            rs.getDouble("percentual_comissao") // O 4º parâmetro corrigido!
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relatorio;
    }

    public double buscarTotalGeralPeriodo(java.time.LocalDate inicio, java.time.LocalDate fim) {
        String sql = "SELECT SUM(valor_total) FROM vendas WHERE data_hora::date BETWEEN ? AND ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(inicio));
            stmt.setDate(2, java.sql.Date.valueOf(fim));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getDouble(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return 0.0;
    }


}