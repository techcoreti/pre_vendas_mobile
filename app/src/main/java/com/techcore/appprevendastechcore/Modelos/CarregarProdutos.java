package com.techcore.appprevendastechcore.Modelos;

import java.io.Serializable;

public class CarregarProdutos implements Serializable {
    private int ID;
    private int PRD_CODIGO;
    private String PRD_DESCRICAO;
    private String PRD_UNIDADE;
    private double PRD_VALOR;
    private int imagem;
    private int size;

    public CarregarProdutos(int ID, int PRD_CODIGO, String PRD_DESCRICAO, String PRD_UNIDADE, double PRD_VALOR, int imagem, int size) {
        this.ID = ID;
        this.PRD_CODIGO = PRD_CODIGO;
        this.PRD_DESCRICAO = PRD_DESCRICAO;
        this.PRD_UNIDADE = PRD_UNIDADE;
        this.PRD_VALOR = PRD_VALOR;
        this.imagem = imagem;
        this.size = size;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPRD_CODIGO() {
        return PRD_CODIGO;
    }

    public void setPRD_CODIGO(int PRD_CODIGO) {
        this.PRD_CODIGO = PRD_CODIGO;
    }

    public String getPRD_DESCRICAO() {
        return PRD_DESCRICAO;
    }

    public void setPRD_DESCRICAO(String PRD_DESCRICAO) {
        this.PRD_DESCRICAO = PRD_DESCRICAO;
    }

    public String getPRD_UNIDADE() {
        return PRD_UNIDADE;
    }

    public void setPRD_UNIDADE(String PRD_UNIDADE) {
        this.PRD_UNIDADE = PRD_UNIDADE;
    }

    public double getPRD_VALOR() {
        return PRD_VALOR;
    }

    public void setPRD_VALOR(double PRD_VALOR) {
        this.PRD_VALOR = PRD_VALOR;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
