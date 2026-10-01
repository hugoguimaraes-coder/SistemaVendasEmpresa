package br.com.empresa.view;

import br.com.empresa.model.RelatorioVendedor;
import br.com.empresa.repository.VendaRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;

public class RelatorioController {

    @FXML private Label lblTotalLoja;
    @FXML private DatePicker dpInicio;
    @FXML private DatePicker dpFim;
    @FXML private TextField txtComissao;

    @FXML private TableView<RelatorioVendedor> tabelaRelatorio;
    @FXML private TableColumn<RelatorioVendedor, String> colVendedor;
    @FXML private TableColumn<RelatorioVendedor, Integer> colQtdVendas;
    @FXML private TableColumn<RelatorioVendedor, Double> colTotalVendido;
    @FXML private TableColumn<RelatorioVendedor, Double> colComissao;

    private VendaRepository vendaRepo = new VendaRepository();

    @FXML
    public void initialize() {
        // Datas padrão: do dia 1 do mês atual até hoje
        dpInicio.setValue(LocalDate.now().withDayOfMonth(1));
        dpFim.setValue(LocalDate.now());

        colVendedor.setCellValueFactory(new PropertyValueFactory<>("nomeVendedor"));
        colQtdVendas.setCellValueFactory(new PropertyValueFactory<>("quantidadeVendas"));
        colTotalVendido.setCellValueFactory(new PropertyValueFactory<>("totalVendido"));

        // FIX: colComissao já existe no FXML, apenas configuramos a factory
        colComissao.setCellValueFactory(new PropertyValueFactory<>("comissao"));

        // Nova coluna para exibir a PORCENTAGEM da comissão
        TableColumn<RelatorioVendedor, Double> colPerc = new TableColumn<>("% Comis.");
        colPerc.setCellValueFactory(new PropertyValueFactory<>("percentualComissao"));
        colPerc.setPrefWidth(100);
        // Insere a coluna de porcentagem antes da coluna de valor em R$
        tabelaRelatorio.getColumns().add(3, colPerc);

        // Formatação de Dinheiro para Total e Comissão
        formatarColunaDinheiro(colTotalVendido);
        formatarColunaDinheiro(colComissao);
        
        // Formatação da porcentagem
        colPerc.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("%.1f%%", item));
            }
        });

        carregarDados();
    }

    private void formatarColunaDinheiro(TableColumn<RelatorioVendedor, Double> col) {
        col.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText((empty || item == null) ? null : String.format("R$ %.2f", item));
            }
        });
    }

    @FXML
    public void carregarDados() {
        LocalDate inicio = dpInicio.getValue();
        LocalDate fim = dpFim.getValue();

        double totalGeral = vendaRepo.buscarTotalGeralPeriodo(inicio, fim);
        lblTotalLoja.setText(String.format("R$ %.2f", totalGeral));

        // Busca dados do banco (Já traz a comissão exclusiva de cada vendedor via JOIN no VendaRepository)
        java.util.List<RelatorioVendedor> lista = vendaRepo.buscarVendasPorVendedorPeriodo(inicio, fim);

        ObservableList<RelatorioVendedor> dados = FXCollections.observableArrayList(lista);
        tabelaRelatorio.setItems(dados);
    }
}