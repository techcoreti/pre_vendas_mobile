package com.techcore.appprevendastechcore.Modelos;

import java.util.Locale;

public class Vendedores {
    private int    ID;
    private String FUN_CODIGO;
    private String FUN_NOME;
    private String FUN_APELIDO;
    private double DSC_MARGEM;

    public Vendedores(int ID, String FUN_CODIGO, String FUN_NOME, String FUN_APELIDO, double DSC_MARGEM) {
        this.ID          = ID;
        this.FUN_CODIGO  = FUN_CODIGO;
        this.FUN_NOME    = FUN_NOME;
        this.FUN_APELIDO = FUN_APELIDO;
        this.DSC_MARGEM  = DSC_MARGEM;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getFUN_CODIGO() {
        return String.format(Locale.getDefault(),"%06d", Integer.parseInt(FUN_CODIGO));
    }

    public void setFUN_CODIGO(String FUN_CODIGO) {
        this.FUN_CODIGO = FUN_CODIGO;
    }

    public String getFUN_NOME() {
        return FUN_NOME;
    }

    public void setFUN_NOME(String FUN_NOME) {
        this.FUN_NOME = FUN_NOME;
    }

    public String getFUN_APELIDO() {
        return FUN_APELIDO;
    }

    public void setFUN_APELIDO(String FUN_APELIDO) {
        this.FUN_APELIDO = FUN_APELIDO;
    }

    public double getDSC_MARGEM() {
        return DSC_MARGEM;
    }

    public void setDSC_MARGEM(double DSC_MARGEM) {
        this.DSC_MARGEM = DSC_MARGEM;
    }
}
