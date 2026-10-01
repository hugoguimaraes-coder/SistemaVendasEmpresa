package br.com.empresa.view;

import br.com.empresa.model.HistoricoVenda;
import br.com.empresa.repository.VendaRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoricoVendasController {

    @FXML private TextField txtBusca;

    @FXML private TableView<HistoricoVenda> tabelaHistorico;
    @FXML private TableColumn<HistoricoVenda, Integer> colId;
    @FXML private TableColumn<HistoricoVenda, LocalDateTime> colData;
    @FXML private TableColumn<HistoricoVenda, String> colCliente;
    @FXML private TableColumn<HistoricoVenda, String> colVendedor;
    @FXML private TableColumn<HistoricoVenda, String> colPagamento;
    @FXML private TableColumn<HistoricoVenda, Double> colTotal;

    private VendaRepository vendaRepo = new VendaRepository();
    private ObservableList<HistoricoVenda> listaOriginal = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Vincula as colunas aos atributos da classe HistoricoVenda
        colId.setCellValueFactory(new PropertyValueFactory<>("idVenda"));
        colData.setCellValueFactory(new PropertyValueFactory<>("dataVenda"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nomeCliente"));
        colVendedor.setCellValueFactory(new PropertyValueFactory<>("nomeVendedor"));
        colPagamento.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("valorTotal"));

        // 2. Formata a coluna de Data para o padrão Brasileiro
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colData.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatadorData.format(item));
                }
            }
        });

        // 3. Formata a coluna de Total para R$ 0,00
        colTotal.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", item));
                }
            }
        });

        // 4. Carrega os dados do banco
        carregarDados();
    }

    private void carregarDados() {
        listaOriginal.clear();
        listaOriginal.addAll(vendaRepo.listarHistorico());
        tabelaHistorico.setItems(listaOriginal);
    }

    @FXML
    public void filtrarHistorico() {
        String busca = txtBusca.getText().toLowerCase().trim();

        if (busca.isEmpty()) {
            tabelaHistorico.setItems(listaOriginal);
            return;
        }

        ObservableList<HistoricoVenda> filtrados = FXCollections.observableArrayList();

        for (HistoricoVenda h : listaOriginal) {
            String cliente = h.getNomeCliente() != null ? h.getNomeCliente().toLowerCase() : "";
            String vendedor = h.getNomeVendedor() != null ? h.getNomeVendedor().toLowerCase() : "";
            String id = String.valueOf(h.getIdVenda());

            // Filtra por nome do cliente, nome do vendedor ou número da venda
            if (cliente.contains(busca) || vendedor.contains(busca) || id.equals(busca)) {
                filtrados.add(h);
            }
        }

        tabelaHistorico.setItems(filtrados);
    }
}