package br.com.empresa.view;

import br.com.empresa.model.Produto;
import br.com.empresa.repository.ProdutoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProdutoController {

    @FXML private TextField txtNome, txtFornecedor, txtUnidade, txtBarras, txtNcm, txtCest, txtPreco, txtEstoque;
    @FXML private Label lblStatus;

    @FXML private TableView<Produto> tabelaProdutos;
    @FXML private TableColumn<Produto, Integer> colId;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, Double> colPreco;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, String> colUnidade;
    @FXML private TableColumn<Produto, String> colNcm;
    @FXML private TableColumn<Produto, String> colCest;
    @FXML private TableColumn<Produto, String> colFornecedor;
    @FXML private TableColumn<Produto, String> colBarras;

    private ProdutoRepository repository = new ProdutoRepository();
    private ObservableList<Produto> listaProdutos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colEstoque.setCellValueFactory(new PropertyValueFactory<>("estoque"));
        colUnidade.setCellValueFactory(new PropertyValueFactory<>("unidade"));
        colFornecedor.setCellValueFactory(new PropertyValueFactory<>("fornecedor"));
        colBarras.setCellValueFactory(new PropertyValueFactory<>("codigoBarras"));

        // VINCULANDO AS NOVAS COLUNAS
        colNcm.setCellValueFactory(new PropertyValueFactory<>("ncm"));
        colCest.setCellValueFactory(new PropertyValueFactory<>("cest"));

        atualizarTabela();
    }

    @FXML
    public void atualizarTabela() {
        listaProdutos.setAll(repository.listarTodos());
        tabelaProdutos.setItems(listaProdutos);
    }

    @FXML
    public void carregarDadosSelecionados() {
        Produto selecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            txtNome.setText(selecionado.getNome());
            txtFornecedor.setText(selecionado.getFornecedor());
            txtUnidade.setText(selecionado.getUnidade());
            txtBarras.setText(selecionado.getCodigoBarras());
            txtNcm.setText(selecionado.getNcm());
            txtCest.setText(selecionado.getCest());
            txtPreco.setText(String.valueOf(selecionado.getPreco()));
            txtEstoque.setText(String.valueOf(selecionado.getEstoque()));
            lblStatus.setText("📝 Editando: " + selecionado.getNome());
        }
    }

    @FXML
    public void handleSalvar() {
        try {
            Produto p = extrairDadosCampos();
            repository.salvar(p);
            lblStatus.setText("✅ Produto salvo com sucesso!");
            limparCampos();
            atualizarTabela();
        } catch (Exception e) {
            lblStatus.setText("❌ Erro ao salvar. Verifique os valores.");
        }
    }

    @FXML
    public void handleAtualizar() {
        Produto selecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            try {
                Produto p = extrairDadosCampos();
                p.setId(selecionado.getId()); // Mantém o mesmo ID do banco
                repository.atualizar(p);
                lblStatus.setText("🔄 Produto '" + p.getNome() + "' atualizado!");
                limparCampos();
                atualizarTabela();
            } catch (Exception e) {
                lblStatus.setText("❌ Erro ao atualizar.");
            }
        } else {
            lblStatus.setText("⚠️ Selecione um produto na tabela para atualizar.");
        }
    }

    @FXML
    public void handleDeletar() {
        Produto selecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            repository.deletar(selecionado.getId());
            lblStatus.setText("🗑️ Produto removido!");
            limparCampos();
            atualizarTabela();
        } else {
            lblStatus.setText("⚠️ Selecione um produto para excluir.");
        }
    }

    private Produto extrairDadosCampos() {
        return new Produto(
                txtNome.getText(),
                txtFornecedor.getText(),
                txtUnidade.getText(),
                txtBarras.getText(),
                txtNcm.getText(),
                txtCest.getText(),
                Double.parseDouble(txtPreco.getText()),
                Integer.parseInt(txtEstoque.getText())
        );
    }

    @FXML
    public void limparCampos() {
        txtNome.clear(); txtFornecedor.clear(); txtUnidade.clear();
        txtBarras.clear(); txtNcm.clear(); txtCest.clear();
        txtPreco.clear(); txtEstoque.clear();
        tabelaProdutos.getSelectionModel().clearSelection();
        lblStatus.setText("Status: Campos limpos.");
    }
}