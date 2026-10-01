package br.com.empresa.view;

import br.com.empresa.model.*;
import br.com.empresa.repository.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

public class VendaController {

    @FXML private ComboBox<Vendedor> cbVendedor;
    @FXML private ComboBox<Cliente> cbCliente;
    @FXML private ComboBox<Produto> cbProduto;
    @FXML private ComboBox<String> cbFormaPagamento;
    @FXML private ComboBox<Integer> cbParcelas;

    @FXML private TextField txtBuscaProduto;
    @FXML private TextField txtBuscaCliente;
    @FXML private TextField txtQuantidade;
    @FXML private TextField txtDesconto;
    @FXML private Label lblSubtotal;
    @FXML private Label lblTotal;
    @FXML private Label lblStatus;
    @FXML private HBox boxParcelas;

    @FXML private TableView<ItemVenda> tabelaItens;
    @FXML private TableColumn<ItemVenda, String> colProduto;
    @FXML private TableColumn<ItemVenda, Integer> colQtd;
    @FXML private TableColumn<ItemVenda, Double> colPreco;
    @FXML private TableColumn<ItemVenda, Double> colSubtotal;

    private VendaRepository vendaRepo = new VendaRepository();
    private ClienteRepository clienteRepo = new ClienteRepository();
    private ProdutoRepository produtoRepo = new ProdutoRepository();
    private VendedorRepository vendedorRepo = new VendedorRepository();

    private ObservableList<ItemVenda> carrinho = FXCollections.observableArrayList();
    private ObservableList<Produto> listaProdutosOriginal = FXCollections.observableArrayList();
    private ObservableList<Cliente> listaClientesOriginal = FXCollections.observableArrayList();
    private double subtotalVenda = 0.0;
    private double totalFinal = 0.0;

    @FXML
    public void initialize() {
        colProduto.setCellValueFactory(new PropertyValueFactory<>("nomeProduto"));
        colQtd.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colPreco.setCellValueFactory(new PropertyValueFactory<>("precoUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        tabelaItens.setItems(carrinho);

        listaClientesOriginal.clear();
        listaClientesOriginal.addAll(clienteRepo.listarTodos());
        cbCliente.setItems(listaClientesOriginal);

        listaProdutosOriginal.clear();
        listaProdutosOriginal.addAll(produtoRepo.listarTodos());
        cbProduto.setItems(listaProdutosOriginal);

        cbVendedor.setItems(FXCollections.observableArrayList(vendedorRepo.listarTodos()));

        cbFormaPagamento.getItems().addAll("Dinheiro", "PIX", "Cartão de Débito", "Cartão de Crédito", "Cheque");
        for (int i = 1; i <= 12; i++) cbParcelas.getItems().add(i);
        cbParcelas.getSelectionModel().selectFirst();

        cbFormaPagamento.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            boolean mostrarParcelas = newVal != null && (newVal.equals("Cartão de Crédito") || newVal.equals("Cheque"));
            boxParcelas.setVisible(mostrarParcelas);
            boxParcelas.setManaged(mostrarParcelas);
            if (!mostrarParcelas) cbParcelas.getSelectionModel().selectFirst();
        });

        txtBuscaProduto.setOnAction(e -> {
            if (!cbProduto.getSelectionModel().isEmpty()) {
                txtQuantidade.requestFocus();
                txtQuantidade.selectAll();
            }
        });

        txtQuantidade.setOnAction(e -> {
            adicionarAoCarrinho();
            txtBuscaProduto.clear();
            filtrarProdutos();
            txtBuscaProduto.requestFocus();
        });

        cbVendedor.setConverter(criarConversorVendedor());
        cbCliente.setConverter(criarConversorCliente());
        cbProduto.setConverter(criarConversorProduto());

        selecionarClientePadrao();
    }

    private javafx.util.StringConverter<Vendedor> criarConversorVendedor() {
        return new javafx.util.StringConverter<>() {
            @Override public String toString(Vendedor v) { return v == null ? "" : v.getNome(); }
            @Override public Vendedor fromString(String s) { return null; }
        };
    }

    private javafx.util.StringConverter<Cliente> criarConversorCliente() {
        return new javafx.util.StringConverter<>() {
            @Override public String toString(Cliente c) { return c == null ? "" : c.getNome(); }
            @Override public Cliente fromString(String s) { return null; }
        };
    }

    private javafx.util.StringConverter<Produto> criarConversorProduto() {
        return new javafx.util.StringConverter<>() {
            @Override
            public String toString(Produto p) {
                if (p == null) {
                    return "";
                }
                return String.format("%s  -  R$ %.2f", p.getNome(), p.getPreco());
            }

            @Override
            public Produto fromString(String s) {
                return null;
            }
        };
    }

    private void selecionarClientePadrao() {
        for (Cliente c : cbCliente.getItems()) {
            if (c.getCpfCnpj().replaceAll("\\D", "").equals("00000000000")) {
                cbCliente.getSelectionModel().select(c);
                break;
            }
        }
    }

    @FXML
    public void filtrarClientes() {
        String busca = txtBuscaCliente.getText().toLowerCase();

        if (busca.isEmpty()) {
            cbCliente.setItems(listaClientesOriginal);
            return;
        }

        ObservableList<Cliente> clientesFiltrados = FXCollections.observableArrayList();
        String buscaNumeros = busca.replaceAll("\\D", "");

        for (Cliente c : listaClientesOriginal) {
            String nome = c.getNome() != null ? c.getNome().toLowerCase() : "";
            String cpfCnpj = c.getCpfCnpj() != null ? c.getCpfCnpj().replaceAll("\\D", "") : "";

            if (nome.contains(busca) || (!buscaNumeros.isEmpty() && cpfCnpj.contains(buscaNumeros))) {
                clientesFiltrados.add(c);
            }
        }

        cbCliente.setItems(clientesFiltrados);

        if (!clientesFiltrados.isEmpty()) {
            cbCliente.show();
            if (clientesFiltrados.size() == 1) {
                cbCliente.getSelectionModel().selectFirst();
            }
        } else {
            cbCliente.hide();
        }
    }

    @FXML
    public void filtrarProdutos() {
        String busca = txtBuscaProduto.getText().toLowerCase().trim();

        if (busca.isEmpty()) {
            cbProduto.setItems(listaProdutosOriginal);
            return;
        }

        ObservableList<Produto> produtosFiltrados = FXCollections.observableArrayList();

        for (Produto p : listaProdutosOriginal) {
            String nome = p.getNome() != null ? p.getNome().toLowerCase() : "";
            String codigo = p.getCodigoBarras() != null ? p.getCodigoBarras().toLowerCase() : "";

            if (nome.contains(busca) || codigo.equals(busca) || codigo.contains(busca)) {
                produtosFiltrados.add(p);
            }
        }

        cbProduto.setItems(produtosFiltrados);

        if (!produtosFiltrados.isEmpty()) {
            cbProduto.show();
            if (produtosFiltrados.size() == 1) {
                cbProduto.getSelectionModel().selectFirst();
            }
        } else {
            cbProduto.hide();
        }
    }

    @FXML
    public void adicionarAoCarrinho() {
        Produto prodSelecionado = cbProduto.getSelectionModel().getSelectedItem();
        String qtdTexto = txtQuantidade.getText();

        if (prodSelecionado == null || qtdTexto.isEmpty()) {
            lblStatus.setText("⚠️ Selecione um produto e informe a quantidade.");
            return;
        }

        try {
            int qtd = Integer.parseInt(qtdTexto);
            if (qtd <= 0) throw new NumberFormatException();

            ItemVenda item = new ItemVenda(prodSelecionado.getId(), prodSelecionado.getNome(), qtd, prodSelecionado.getPreco());
            carrinho.add(item);

            atualizarValores();
            lblStatus.setText("✅ Produto adicionado ao carrinho!");

            cbProduto.getSelectionModel().clearSelection();
            txtQuantidade.setText("1");

        } catch (NumberFormatException e) {
            lblStatus.setText("❌ Quantidade inválida.");
        }
    }

    @FXML
    public void calcularTotalFinal() {
        atualizarValores();
    }

    private void atualizarValores() {
        subtotalVenda = 0.0;
        for (ItemVenda item : carrinho) {
            subtotalVenda += item.getSubtotal();
        }

        double desconto = 0.0;
        try {
            String descTxt = txtDesconto.getText().replace(",", ".");
            if (!descTxt.isEmpty()) {
                desconto = Double.parseDouble(descTxt);
            }
        } catch (NumberFormatException e) {
        }

        totalFinal = subtotalVenda - desconto;
        if (totalFinal < 0) totalFinal = 0.0;

        lblSubtotal.setText(String.format("R$ %.2f", subtotalVenda));
        lblTotal.setText(String.format("R$ %.2f", totalFinal));
    }

    @FXML
    public void finalizarVenda() {
        if (carrinho.isEmpty()) {
            lblStatus.setText("⚠️ O carrinho está vazio!");
            return;
        }

        Cliente cliente = cbCliente.getSelectionModel().getSelectedItem();
        Vendedor vendedor = cbVendedor.getSelectionModel().getSelectedItem();
        String formaPagamento = cbFormaPagamento.getSelectionModel().getSelectedItem();

        if (cliente == null || vendedor == null || formaPagamento == null) {
            lblStatus.setText("⚠️ Preencha Vendedor, Cliente e Forma de Pagamento.");
            return;
        }

        double desconto = subtotalVenda - totalFinal;
        int parcelas = boxParcelas.isVisible() ? cbParcelas.getSelectionModel().getSelectedItem() : 1;

        Venda venda = new Venda(cliente.getId(), vendedor.getId(), subtotalVenda, "R$", desconto, totalFinal, formaPagamento, parcelas);

        venda.getItens().addAll(carrinho);

        try {
            vendaRepo.salvar(venda);

            lblStatus.setText("✅ Venda finalizada com sucesso!");
            limparTela();
        } catch (Exception e) {
            lblStatus.setText("❌ Erro ao salvar: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void limparTela() {
        carrinho.clear();
        txtDesconto.clear();
        cbFormaPagamento.getSelectionModel().clearSelection();
        cbProduto.getSelectionModel().clearSelection();
        txtQuantidade.setText("1");
        atualizarValores();
        selecionarClientePadrao();
    }
}