package br.com.empresa.model;

public class Vendedor {
    private int id;
    private String nome;
    private double percentualComissao;

    public Vendedor() {}

    public Vendedor(String nome, double percentualComissao) {
        this.nome = nome;
        this.percentualComissao = percentualComissao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public double getPercentualComissao() { return percentualComissao; }
    public void setPercentualComissao(double percentualComissao) { this.percentualComissao = percentualComissao; }

    @Override
    public String toString() {
        return this.nome;
    }

}