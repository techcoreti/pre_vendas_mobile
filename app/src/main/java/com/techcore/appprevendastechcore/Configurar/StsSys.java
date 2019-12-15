package com.techcore.appprevendastechcore.Configurar;

import android.app.Activity;

public class StsSys {
    /*
     * Seção utilizada quando for enviado os dadso para o servidor
     * GravarPedido, GravarProdutos
     */
    public static int ExibirCodigoVenda;

    /*
     * Declara as variaveis globais da aplicação
     */
    public static String  gCodigoTerminal;
    public static String  gCodigoFilial;
    public static String  gCodigoPrecos;
    public static String  gUrlServer;
    public static boolean gCheckout;
    public static int     gCodigoPedido = 0;
    public static double  gValorTotal;
    public static boolean FecharSistema = false;
    public static boolean RefreshCarrinho = false;

    /*
     * Apelido, Código e Código da tabela de desconto
     */
    public static int     gIDVendedor = 0;
    public static String  gCodigoVendedor;
    public static String  gApelidoVendedor;
    public static double  gMargemMaxDesconto;
    public static String  gCodigoDesconto;

    /*
     * Armazena o margem do desconto permitido para o vendedor
     */
    public static double vlrSemDesconto = 0;
    public static double vlrComDesconto = 0;
    public static double vlrDoDesconto  = 0;
    public static double mrgDoDesconto  = 0;

    public static float ValorSD = 0;
    public static float ValorDS = 0;
    public static float ValorCD = 0;

    /*
     * Armazena o contexto da activitys
     */

    public static Activity mActivity;

    public static int getExibirCodigoVenda() {
        return ExibirCodigoVenda;
    }

    public static void setExibirCodigoVenda(int exibirCodigoVenda) {
        ExibirCodigoVenda = exibirCodigoVenda;
    }

    public static String getgCodigoFilial() {
        return gCodigoFilial;
    }

    public static void setgCodigoFilial(String gCodigoFilial) {
        StsSys.gCodigoFilial = gCodigoFilial;
    }

    public static String getgUrlServer() {
        return gUrlServer;
    }

    public static void setgUrlServer(String gUrlServer) {
        StsSys.gUrlServer = gUrlServer;
    }

    public void LoadConfigServer(){

    }
}
