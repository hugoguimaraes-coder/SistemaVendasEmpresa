package br.com.empresa.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoBanco {
    private static final String URL = env("DB_URL", "jdbc:postgresql://localhost:5432/sistema_vendas");
    private static final String USUARIO = env("DB_USER", "postgres");
    private static final String SENHA = env("DB_PASSWORD", "");

    private static String env(String nome, String padrao) {
        String valor = System.getenv(nome);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }

    public static void main(String[] args) {
        try (Connection conn = conectar()) {
            if (conn != null) {
                System.out.println("✅ Sucesso! O Java conectou ao banco da máquina Admin.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Erro de conexão: " + e.getMessage());
        }
    }
}