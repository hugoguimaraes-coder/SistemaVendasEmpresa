package br.com.empresa.model;

public class RelatorioVendedor {
    private String nomeVendedor;
    private int quantidadeVendas;
    private double totalVendido;
    private double percentualComissao;
    private double comissao;

    public RelatorioVendedor(String nomeVendedor, int quantidadeVendas, double totalVendido, double percentualComissao) {
        this.nomeVendedor = nomeVendedor;
        this.quantidadeVendas = quantidadeVendas;
        this.totalVendido = totalVendido;
        this.percentualComissao = percentualComissao;
        this.calcularComissao();
    }

    public void calcularComissao() {
        this.comissao = (this.totalVendido * this.percentualComissao) / 100.0;
    }

    public String getNomeVendedor() {
        return nomeVendedor;
    }

    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    public int getQuantidadeVendas() {
        return quantidadeVendas;
    }

    public void setQuantidadeVendas(int quantidadeVendas) {
        this.quantidadeVendas = quantidadeVendas;
    }

    public double getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(double totalVendido) {
        this.totalVendido = totalVendido;
        this.calcularComissao();
    }

    public double getPercentualComissao() {
        return percentualComissao;
    }

    public void setPercentualComissao(double percentualComissao) {
        this.percentualComissao = percentualComissao;
        this.calcularComissao();
    }

    public double getComissao() {
        return comissao;
    }

    public void setComissao(double comissao) {
        this.comissao = comissao;
    }
}