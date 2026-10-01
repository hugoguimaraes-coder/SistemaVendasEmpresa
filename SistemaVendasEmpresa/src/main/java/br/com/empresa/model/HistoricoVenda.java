package br.com.empresa.model;

import java.time.LocalDateTime;

public class HistoricoVenda {
    private int idVenda;
    private String nomeCliente;
    private String nomeVendedor;
    private LocalDateTime dataVenda;
    private double valorTotal;
    private String formaPagamento;

    // 1. Construtor Vazio (Ideal para preencher passo a passo com setters no Banco de Dados)
    public HistoricoVenda() {
    }

    // 2. Construtor Cheio (Ideal para instanciar o objeto completo de uma vez só)
    public HistoricoVenda(int idVenda, String nomeCliente, String nomeVendedor, LocalDateTime dataVenda, double valorTotal, String formaPagamento) {
        this.idVenda = idVenda;
        this.nomeCliente = nomeCliente;
        this.nomeVendedor = nomeVendedor;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
    }

    // --- Getters e Setters ---

    public int getIdVenda() { return idVenda; }
    public void setIdVenda(int idVenda) { this.idVenda = idVenda; }

    public String getNomeCliente() { return nomeCliente; }
    public void setNomeCliente(String nomeCliente) { this.nomeCliente = nomeCliente; }

    public String getNomeVendedor() { return nomeVendedor; }
    public void setNomeVendedor(String nomeVendedor) { this.nomeVendedor = nomeVendedor; }

    public LocalDateTime getDataVenda() { return dataVenda; }
    public void setDataVenda(LocalDateTime dataVenda) { this.dataVenda = dataVenda; }

    public double getValorTotal() { return valorTotal; }
    public void setValorTotal(double valorTotal) { this.valorTotal = valorTotal; }

    public String getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(String formaPagamento) { this.formaPagamento = formaPagamento; }
}