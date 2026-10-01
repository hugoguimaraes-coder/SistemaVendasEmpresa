package br.com.empresa.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import java.io.IOException;

public class MainController {

    // Este fx:id deve ser o mesmo no arquivo FXML
    @FXML private StackPane conteudoPrincipal;

    @FXML
    public void abrirCadastroCliente() {
        // CORRIGIDO: Coloquei o nome exato do arquivo que você me enviou
        carregarTela("/br/com/empresa/view/telacliente.fxml");
    }

    @FXML
    public void abrirCadastroProduto() {
        // Como ainda não temos essa tela, vamos avisar o usuário no console
        // ou colocar um try-catch na carregarTela (já fizemos isso).
        // Se você já criou um arquivo vazio para ela, coloque o nome exato aqui:
        carregarTela("/br/com/empresa/view/telaproduto.fxml"); // Exemplo de nome
    }

    @FXML
    public void abrirCadastroVendedor() {
        // Mesma lógica para a tela de vendedor
        carregarTela("/br/com/empresa/view/telavendedor.fxml"); // Exemplo de nome
    }

    @FXML
    public void abrirTelaVenda() {
        // Certifique-se de que o nome do arquivo FXML está exatamente assim
        carregarTela("/br/com/empresa/view/VendaView.fxml");
    }

    @FXML
    public void abrirHistoricoVendas() {
        carregarTela("/br/com/empresa/view/HistoricoVendasView.fxml");
    }

    @FXML
    public void abrirRelatorios() {
        carregarTela("/br/com/empresa/view/RelatorioView.fxml");
    }

    // Método genérico para trocar de tela
    private void carregarTela(String fxml) {
        try {
            Parent tela = FXMLLoader.load(getClass().getResource(fxml));
            conteudoPrincipal.getChildren().clear(); // Remove a tela atual
            conteudoPrincipal.getChildren().add(tela); // Adiciona a nova tela
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro de Navegação");
            alert.setHeaderText("Não foi possível carregar a tela");
            alert.setContentText("Caminho: " + fxml + "\nErro: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}