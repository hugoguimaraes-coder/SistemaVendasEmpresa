package br.com.empresa.repository;

import br.com.empresa.database.ConexaoBanco;
import br.com.empresa.model.Vendedor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendedorRepository {

    // CREATE - Você já tem este!
    public void salvar(Vendedor vendedor) {
        String sql = "INSERT INTO vendedores (nome, percentual_comissao) VALUES (?, ?)";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vendedor.getNome());
            stmt.setDouble(2, vendedor.getPercentualComissao());
            stmt.executeUpdate();
            System.out.println("✅ Vendedor salvo!");
        } catch (SQLException e) { System.err.println("❌ Erro ao salvar: " + e.getMessage()); }
    }

    // UPDATE - Para alterar nome ou comissão
    public void atualizar(Vendedor vendedor) {
        String sql = "UPDATE vendedores SET nome = ?, percentual_comissao = ? WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vendedor.getNome());
            stmt.setDouble(2, vendedor.getPercentualComissao());
            stmt.setInt(3, vendedor.getId());
            stmt.executeUpdate();
            System.out.println("🔄 Vendedor atualizado com sucesso!");
        } catch (SQLException e) { System.err.println("❌ Erro ao atualizar: " + e.getMessage()); }
    }

    // DELETE - Para remover um vendedor
    public void deletar(int id) {
        String sql = "DELETE FROM vendedores WHERE id = ?";
        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("🗑️ Vendedor removido!");
        } catch (SQLException e) { System.err.println("❌ Erro ao deletar: " + e.getMessage()); }
    }
    public List<Vendedor> listarTodos() {
        List<Vendedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM vendedores ORDER BY id ASC";

        try (Connection conn = ConexaoBanco.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Vendedor v = new Vendedor();
                v.setId(rs.getInt("id"));
                v.setNome(rs.getString("nome"));
                v.setPercentualComissao(rs.getDouble("percentual_comissao"));
                lista.add(v);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro ao listar vendedores: " + e.getMessage());
        }
        return lista;
    }

}