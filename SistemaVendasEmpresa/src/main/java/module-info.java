module br.com.empresa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql; // Necessário para o PostgreSQL
    requires org.postgresql.jdbc; // Libera o driver do banco
    requires java.net.http; // Necessário para a busca de CNPJ
    requires com.google.gson; // <--- ADICIONE ESTA LINHA

    opens br.com.empresa.view to javafx.fxml;
    opens br.com.empresa.model to javafx.base;
    opens br.com.empresa.repository to java.sql;

    exports br.com.empresa;
}