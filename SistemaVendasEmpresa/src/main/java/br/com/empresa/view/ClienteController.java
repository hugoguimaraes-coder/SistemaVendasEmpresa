package br.com.empresa.view;

import br.com.empresa.model.Cliente;
import br.com.empresa.repository.ClienteRepository;
import br.com.empresa.util.ValidadorDocumento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClienteController {

    @FXML private TextField txtNome, txtCpfCnpj, txtEmail, txtTelefone, txtEndereco, txtBairro, txtEstado, txtUf, txtCep, txtCidade;
    @FXML private Label lblStatus;

    @FXML private TableView<Cliente> tabelaClientes;
    @FXML private TableColumn<Cliente, String> colNome, colCpf, colTelefone, colCidade, colUf;

    private ClienteRepository repository = new ClienteRepository();
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpfCnpj"));
        colTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
        colUf.setCellValueFactory(new PropertyValueFactory<>("uf"));

        atualizarTabela();
    }

    @FXML
    public void handleSalvar() {
        String documento = txtCpfCnpj.getText();

        if (!ValidadorDocumento.isValido(documento)) {
            lblStatus.setText("❌ Erro: CPF ou CNPJ inválido!");
            txtCpfCnpj.setStyle("-fx-border-color: red;");
            return;
        }

        try {
            Cliente c = new Cliente(
                    txtNome.getText(),
                    documento,
                    txtEmail.getText(),
                    txtTelefone.getText(),
                    txtEndereco.getText(),
                    txtBairro.getText(),
                    txtCidade.getText(),
                    txtEstado.getText(), // Campo Estado
                    txtUf.getText(),     // Campo UF
                    txtCep.getText()
            );

            repository.salvar(c);
            lblStatus.setText("✅ Cliente cadastrado com sucesso!");
            limparCampos();
            atualizarTabela();

        } catch (Exception e) {
            lblStatus.setText("❌ Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void aoDigitarCpfCnpj() {
        String doc = txtCpfCnpj.getText().replaceAll("\\D", "");
        if (doc.length() == 14) {
            buscarDadosCnpj(doc);
        }
    }

    private void buscarDadosCnpj(String cnpj) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://brasilapi.com.br/api/cnpj/v1/" + cnpj))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(json -> {
                    try {
                        JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

                        javafx.application.Platform.runLater(() -> {
                            if (obj.has("razao_social")) {
                                txtNome.setText(obj.get("razao_social").getAsString());
                                txtEmail.setText(obj.has("email") && !obj.get("email").isJsonNull() ? obj.get("email").getAsString() : "");

                                String ddd = obj.has("ddd_telefone_1") ? obj.get("ddd_telefone_1").getAsString() : "";
                                txtTelefone.setText(ddd);

                                // Extraindo dados do JSON
                                String logradouro = obj.get("logradouro").getAsString();
                                String numero = obj.get("numero").getAsString();
                                String bairro = obj.get("bairro").getAsString();
                                String municipio = obj.get("municipio").getAsString();
                                String uf = obj.get("uf").getAsString();
                                String cep = obj.get("cep").getAsString();

                                // Preenchimento da Interface
                                txtEndereco.setText(logradouro + ", " + numero);
                                txtBairro.setText(bairro);
                                txtCidade.setText(municipio);
                                txtCep.setText(cep);

                                // AQUI ESTAVA O ERRO:
                                // Preenchemos tanto o txtUf quanto o txtEstado com a sigla,
                                // já que a API só retorna a sigla (SP, RJ, etc).
                                txtUf.setText(uf);
                                txtEstado.setText(uf);

                                lblStatus.setText("✅ Dados importados com sucesso!");
                            } else {
                                lblStatus.setText("⚠️ CNPJ não encontrado.");
                            }
                        });
                    } catch (Exception e) {
                        javafx.application.Platform.runLater(() -> lblStatus.setText("❌ Erro ao processar dados."));
                        e.printStackTrace();
                    }
                });
    }

    @FXML
    public void atualizarTabela() {
        listaClientes.setAll(repository.listarTodos());
        tabelaClientes.setItems(listaClientes);
    }

    @FXML
    public void handleDeletar() {
        Cliente selecionado = tabelaClientes.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação de Exclusão");
            alert.setHeaderText("Excluir Cliente");
            alert.setContentText("Tem certeza que deseja excluir: " + selecionado.getNome() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    repository.deletar(selecionado.getId());
                    lblStatus.setText("🗑️ Cliente removido com sucesso!");
                    limparCampos();
                    atualizarTabela();
                } catch (Exception e) {
                    lblStatus.setText("❌ Erro ao deletar cliente.");
                }
            }
        } else {
            lblStatus.setText("⚠️ Selecione um cliente na tabela!");
        }
    }

    @FXML
    public void limparCampos() {
        txtNome.clear(); txtCpfCnpj.clear(); txtEmail.clear();
        txtTelefone.clear(); txtEndereco.clear(); txtCidade.clear();
        txtBairro.clear(); txtEstado.clear(); txtUf.clear(); txtCep.clear();
        txtCpfCnpj.setStyle("");
    }
}