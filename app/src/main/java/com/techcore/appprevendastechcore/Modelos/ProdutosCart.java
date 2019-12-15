package com.techcore.appprevendastechcore.Modelos;

public class ProdutosCart {
    /**
     * Campos tabela de ProdutosCart
     */
    private String idTabela;
    private String ProdutoCodigo        = null;
    private String ProdutoNome          = null;
    private String ProdutoUnidade       = null;
    private double ProdutoValor         = 0;
    private double ProdutoQuantidade    = 0;
    private double ProdutoValorTotal    = 0;
    private int    ProdutoCest          = 0;
    private int    ProdutoNcm           = 0;

    public ProdutosCart() {

    }

    public ProdutosCart(String idTabela, String produtoCodigo, String produtoNome, String produtoUnidade,
                        double produtoValor, double produtoQuantidade, double produtoValorTotal,
                        int produtoCest, int produtoNcm) {
        idTabela            = idTabela;
        ProdutoCodigo       = produtoCodigo;
        ProdutoNome         = produtoNome;
        ProdutoUnidade      = produtoUnidade;
        ProdutoValor        = produtoValor;
        ProdutoQuantidade   = produtoQuantidade;
        ProdutoValorTotal   = produtoValorTotal;
        ProdutoCest         = produtoCest;
        ProdutoNcm          = produtoNcm;
    }

    public String getIdTabela() {
        return idTabela;
    }

    public void setIdTabela(String idTabela) {
        this.idTabela = idTabela;
    }

    public String getProdutoCodigo() {
        return ProdutoCodigo;
    }

    public void setProdutoCodigo(String produtoCodigo) {
        ProdutoCodigo = produtoCodigo;
    }

    public String getProdutoNome() {
        return ProdutoNome;
    }

    public void setProdutoNome(String produtoNome) {
        ProdutoNome = produtoNome;
    }

    public String getProdutoUnidade() {
        return ProdutoUnidade;
    }

    public void setProdutoUnidade(String produtoUnidade) {
        ProdutoUnidade = produtoUnidade;
    }

    public double getProdutoValor() {
        return ProdutoValor;
    }

    public void setProdutoValor(double produtoValor) {
        ProdutoValor = produtoValor;
    }

    public double getProdutoQuantidade() {
        return ProdutoQuantidade;
    }

    public void setProdutoQuantidade(double produtoQuantidade) {
        ProdutoQuantidade = produtoQuantidade;
    }

    public double getProdutoValorTotal() {
        return ProdutoValorTotal;
    }

    public void setProdutoValorTotal(double produtoValorTotal) {
        ProdutoValorTotal = produtoValorTotal;
    }

    public int getProdutoCest() {
        return ProdutoCest;
    }

    public void setProdutoCest(int produtoCest) {
        ProdutoCest = produtoCest;
    }

    public int getProdutoNcm() {
        return ProdutoNcm;
    }

    public void setProdutoNcm(int produtoNcm) {
        ProdutoNcm = produtoNcm;
    }
}
