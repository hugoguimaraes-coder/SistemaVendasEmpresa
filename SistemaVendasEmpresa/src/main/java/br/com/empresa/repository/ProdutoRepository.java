package br.com.empresa.repository;

import br.com.empresa.database.ConexaoBanco;
import br.com.empresa.model.Produto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    public void salvar(Produto p) {
        String sql = "INSERT INTO produtos (nome, fornecedor, unidade, codigo_barras, ncm, cest, preco, estoque) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getFornecedor());
            stmt.setString(3, p.getUnidade());
            stmt.setString(4, p.getCodigoBarras());
            stmt.setString(5, p.getNcm());
            stmt.setString(6, p.getCest());
            stmt.setDouble(7, p.getPreco());
            stmt.setInt(8, p.getEstoque());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar produto: " + e.getMessage());
        }
    }

    public void atualizar(Produto p) {
        String sql = "UPDATE produtos SET nome=?, fornecedor=?, unidade=?, codigo_barras=?, ncm=?, cest=?, preco=?, estoque=? WHERE id=?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getFornecedor());
            stmt.setString(3, p.getUnidade());
            stmt.setString(4, p.getCodigoBarras());
            stmt.setString(5, p.getNcm());
            stmt.setString(6, p.getCest());
            stmt.setDouble(7, p.getPreco());
            stmt.setInt(8, p.getEstoque());
            stmt.setInt(9, p.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar: " + e.getMessage());
        }
    }

    public void deletar(int id) {
        String sql = "DELETE FROM produtos WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar: " + e.getMessage());
        }
    }

    public List<Produto> listarTodos() {
        List<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produtos ORDER BY nome";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Produto p = new Produto();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setFornecedor(rs.getString("fornecedor"));
                p.setUnidade(rs.getString("unidade"));
                p.setCodigoBarras(rs.getString("codigo_barras"));
                p.setNcm(rs.getString("ncm"));
                p.setCest(rs.getString("cest"));
                p.setPreco(rs.getDouble("preco"));
                p.setEstoque(rs.getInt("estoque"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}