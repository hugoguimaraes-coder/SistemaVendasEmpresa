package br.com.empresa.repository;

import br.com.empresa.database.ConexaoBanco;
import br.com.empresa.model.Cliente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {

    /**
     * Insere um novo cliente no banco de dados.
     */
    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nome, cpf_cnpj, email, telefone, endereco, bairro, cidade, estado, uf, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCnpj().replaceAll("\\D", "")); // Salva apenas números
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getBairro());
            stmt.setString(7, cliente.getCidade());
            stmt.setString(8, cliente.getEstado());
            stmt.setString(9, cliente.getUf());
            stmt.setString(10, cliente.getCep());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    /**
     * Retorna a lista de todos os clientes cadastrados.
     */
    public List<Cliente> listarTodos() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nome";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpfCnpj(rs.getString("cpf_cnpj"));
                c.setEmail(rs.getString("email"));
                c.setTelefone(rs.getString("telefone"));
                c.setEndereco(rs.getString("endereco"));
                c.setCidade(rs.getString("cidade"));
                c.setEstado(rs.getString("estado"));
                c.setUf(rs.getString("uf"));
                c.setCep(rs.getString("cep"));
                c.setBairro(rs.getString("bairro"));
                lista.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Atualiza os dados de um cliente existente.
     */
    public void atualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nome=?, cpf_cnpj=?, email=?, telefone=?, endereco=?, cidade=? WHERE id=?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpfCnpj().replaceAll("\\D", ""));
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getTelefone());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getCidade());
            stmt.setInt(7, cliente.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar cliente: " + e.getMessage());
        }
    }

    /**
     * Remove um cliente do banco pelo ID.
     */
    public void deletar(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar cliente: " + e.getMessage());
        }
    }

    /**
     * Busca um cliente específico pelo CPF/CNPJ (Útil para a Tela de Vendas)
     */
    public Cliente buscarPorDocumento(String doc) {
        String sql = "SELECT * FROM clientes WHERE cpf_cnpj = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, doc.replaceAll("\\D", ""));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpfCnpj(rs.getString("cpf_cnpj"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Verifica se o "Consumidor Padrão" existe e, caso não exista, realiza a criação.
     * Ideal para ser chamado na inicialização do sistema.
     */
    public void verificarECriarConsumidorPadrao() {
        String sqlVerifica = "SELECT COUNT(*) FROM clientes WHERE cpf_cnpj = '00000000000'";
        String sqlInsere = "INSERT INTO clientes (nome, cpf_cnpj, email, telefone, endereco, bairro, cidade, estado, uf, cep) " +
                "VALUES ('CONSUMIDOR PADRÃO', '00000000000', '', '', '', '', '', '', '', '')";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmtVerifica = conn.prepareStatement(sqlVerifica);
             ResultSet rs = stmtVerifica.executeQuery()) {

            if (rs.next() && rs.getInt(1) == 0) {
                // Se retornou 0, significa que não existe. Vamos criar.
                try (PreparedStatement stmtInsere = conn.prepareStatement(sqlInsere)) {
                    stmtInsere.executeUpdate();
                    System.out.println("✅ Cliente 'CONSUMIDOR PADRÃO' criado com sucesso no banco de dados.");
                }
            } else {
                System.out.println("ℹ️ Cliente 'CONSUMIDOR PADRÃO' já existe. Nenhuma ação necessária.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro ao verificar/criar Consumidor Padrão: " + e.getMessage());
        }
    }
}