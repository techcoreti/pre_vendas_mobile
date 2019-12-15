package com.techcore.appprevendastechcore.Modelos;

public class LoginConfig {
    private String  CodigoFilial;   // Nome que será dado ao terminal
    private String  UrlServer;      // Endereço do servidor
    private int     Retorno;        // Valor retornado pelo servidor REST 100 = login ok, 101 = erro de login

    public LoginConfig(){

    }

    /*public LoginConfig( String codigoFilial, String urlServer, int retorno) {
        CodigoFilial = codigoFilial;
        UrlServer = urlServer;
        Retorno = retorno;
    }*/

    public String getCodigoFilial() {
        return CodigoFilial;
    }

    public void setCodigoFilial(String codigoFilial) {
        CodigoFilial = codigoFilial;
    }

    public String getUrlServer() {
        return UrlServer;
    }

    public void setUrlServer(String urlServer) {
        UrlServer = urlServer;
    }

    public int getRetorno() {
        return Retorno;
    }

    public void setRetorno(int retorno) {
        Retorno = retorno;
    }
}
