package br.com.empresa.model;

public class Produto {
    private int id;
    private String nome;
    private String fornecedor;
    private String unidade;
    private String codigoBarras;
    private String ncm;
    private String cest;
    private double preco;
    private int estoque;

    public Produto() {}

    // Construtor completo para cadastro
    public Produto(String nome, String fornecedor, String unidade, String codigoBarras,
                   String ncm, String cest, double preco, int estoque) {
        this.nome = nome;
        this.fornecedor = fornecedor;
        this.unidade = unidade;
        this.codigoBarras = codigoBarras;
        this.ncm = ncm;
        this.cest = cest;
        this.preco = preco;
        this.estoque = estoque;
    }

    // Getters e Setters para TODOS os campos (Importante para a TableView)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getFornecedor() { return fornecedor; }
    public void setFornecedor(String fornecedor) { this.fornecedor = fornecedor; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public String getNcm() { return ncm; }
    public void setNcm(String ncm) { this.ncm = ncm; }
    public String getCest() { return cest; }
    public void setCest(String cest) { this.cest = cest; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }

    @Override
    public String toString() {
        return this.nome;
    }
}