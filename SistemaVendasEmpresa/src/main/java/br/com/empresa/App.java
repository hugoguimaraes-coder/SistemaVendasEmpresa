package br.com.empresa;

import br.com.empresa.repository.ClienteRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/br/com/empresa/view/MainView.fxml"));
        primaryStage.setTitle("Sistema de Vendas - Java Cliente/Servidor");
        primaryStage.setScene(new Scene(root, 1024, 768)); // Tamanho maior para o Dashboard
        primaryStage.show();
        ClienteRepository clienteRepo = new ClienteRepository();
        clienteRepo.verificarECriarConsumidorPadrao();
    }

    public static void main(String[] args) {
        launch();
    }
}