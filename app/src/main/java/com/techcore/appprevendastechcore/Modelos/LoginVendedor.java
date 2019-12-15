package com.techcore.appprevendastechcore.Modelos;

import java.io.Serializable;

public class LoginVendedor implements Serializable {

    private int ID;
    private String VND_APELIDO;
    private String RESPOSTA;

    public LoginVendedor(int ID, String VND_APELIDO, String RESPOSTA){
        this.ID          = ID;
        this.VND_APELIDO = VND_APELIDO;
        this.RESPOSTA    = RESPOSTA;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public int getID(){
        return this.ID;
    }

    public void setVND_APELIDO(String VND_APELIDO){
        this.VND_APELIDO = VND_APELIDO;
    }

    public String getVND_APELIDO(){
        return this.VND_APELIDO;
    }

    public String getResposta(){
        return this.RESPOSTA;
    }
}
