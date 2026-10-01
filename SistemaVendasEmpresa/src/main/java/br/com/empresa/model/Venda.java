package br.com.empresa.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private int id;
    private int clienteId;
    private int vendedorId;
    private LocalDateTime dataVenda;
    private double subtotal;
    private String tipoDesconto;
    private double valorDesconto;
    private double total;
    private String formaPagamento;
    private int parcelas;

    private List<ItemVenda> itens;

    public Venda(int clienteId, int vendedorId, double subtotal, String tipoDesconto,
                 double valorDesconto, double total, String formaPagamento, int parcelas) {
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.subtotal = subtotal;
        this.tipoDesconto = tipoDesconto;
        this.valorDesconto = valorDesconto;
        this.total = total;
        this.formaPagamento = formaPagamento;
        this.parcelas = parcelas;
        this.itens = new ArrayList<>();
        this.dataVenda = LocalDateTime.now();
    }

    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
    }

    public int getVendedorId() {
        return vendedorId;
    }

    public void setVendedorId(int vendedorId) {
        this.vendedorId = vendedorId;
    }

    public LocalDateTime getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDateTime dataVenda) {
        this.dataVenda = dataVenda;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getTipoDesconto() {
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public double getValorDesconto() {
        return valorDesconto;
    }

    public void setValorDesconto(double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public int getParcelas() {
        return parcelas;
    }

    public void setParcelas(int parcelas) {
        this.parcelas = parcelas;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }
}