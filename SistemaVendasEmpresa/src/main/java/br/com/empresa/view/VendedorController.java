package br.com.empresa.view;

import br.com.empresa.model.Vendedor;
import br.com.empresa.repository.VendedorRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class VendedorController {

    @FXML private TextField txtNome;
    @FXML private TextField txtComissao;
    @FXML private Label lblStatus;

    // Elementos da Tabela
    @FXML private TableView<Vendedor> tabelaVendedores;
    @FXML private TableColumn<Vendedor, Integer> colId;
    @FXML private TableColumn<Vendedor, String> colNome;
    @FXML private TableColumn<Vendedor, Double> colComissao;

    private VendedorRepository repository = new VendedorRepository();
    private ObservableList<Vendedor> listaVendedores = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configura quais atributos da classe Vendedor aparecem em cada coluna
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colComissao.setCellValueFactory(new PropertyValueFactory<>("percentualComissao"));

        atualizarTabela();
    }

    @FXML
    public void atualizarTabela() {
        listaVendedores.clear();
        List<Vendedor> vends = repository.listarTodos(); // Lembra do método que criamos no console?
        listaVendedores.addAll(vends);
        tabelaVendedores.setItems(listaVendedores);
    }

    @FXML
    public void handleSalvar() {
        try {
            Vendedor v = new Vendedor(txtNome.getText(), Double.parseDouble(txtComissao.getText()));
            repository.salvar(v);
            lblStatus.setText("✅ Vendedor adicionado!");
            txtNome.clear();
            txtComissao.clear();
            atualizarTabela(); // Atualiza a tabela na hora!
        } catch (Exception e) {
            lblStatus.setText("❌ Erro ao salvar.");
        }
    }

    @FXML
    public void handleDeletar() {
        Vendedor selecionado = tabelaVendedores.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            repository.deletar(selecionado.getId());
            lblStatus.setText("🗑️ Removido: " + selecionado.getNome());
            atualizarTabela();
        } else {
            lblStatus.setText("⚠️ Selecione um vendedor na tabela!");
        }
    }
}