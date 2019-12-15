package com.techcore.appprevendastechcore.Modelos;
import java.io.Serializable;

public class ListarPreVendas implements Serializable {
    private String DATA_VENDA;
    private String PRE_CODIGO_VENDA;
    private Double PRE_VLR_SUBTOTAL;
    private Double PRE_VLR_DESCONTO;
    private Double PRE_VLR_TOTAL;
    private int PRE_STATUS;
    private String LOCALIZADO;
    private Double F_CUP_TOTAL;
    private int F_CUP_STATUS;

    public ListarPreVendas(String DATA_VENDA, String PRE_CODIGO_VENDAS, Double PRE_VLR_SUBTOTAL, Double PRE_VLR_DESCONTO, Double PRE_VLR_TOTAL, int PRE_STATUS, String LOCALIZADO, Double F_CUP_TOTAL, int F_CUP_STATUS) {
        this.DATA_VENDA       =  DATA_VENDA;
        this.PRE_CODIGO_VENDA = PRE_CODIGO_VENDAS;
        this.PRE_VLR_SUBTOTAL = PRE_VLR_SUBTOTAL;
        this.PRE_VLR_DESCONTO = PRE_VLR_DESCONTO;
        this.PRE_VLR_TOTAL    = PRE_VLR_TOTAL;
        this.PRE_STATUS       = PRE_STATUS;
        this.LOCALIZADO       = LOCALIZADO;
        this.F_CUP_TOTAL      = F_CUP_TOTAL;
        this.F_CUP_STATUS     = F_CUP_STATUS;
    }

    public Double getCupValor(){
        return this.F_CUP_TOTAL;
    }

    public int getCupStatus(){
        return this.F_CUP_STATUS;
    }

    public String getLocalizado(){
        return this.LOCALIZADO;
    }

    public String getDATA_VENDA(){
        return this.DATA_VENDA;
    }

    public String getPRE_CODIGO_VENDAS() {
        return PRE_CODIGO_VENDA;
    }

    public void setPRE_CODIGO_VENDAS(String PRE_CODIGO_VENDAS) {
        this.PRE_CODIGO_VENDA = PRE_CODIGO_VENDAS;
    }

    public Double getPRE_VLR_SUBTOTAL() {
        return PRE_VLR_SUBTOTAL;
    }

    public void setPRE_VLR_SUBTOTAL(Double PRE_VLR_SUBTOTAL) {
        this.PRE_VLR_SUBTOTAL = PRE_VLR_SUBTOTAL;
    }

    public Double getPRE_VLR_DESCONTO() {
        return PRE_VLR_DESCONTO;
    }

    public void setPRE_VLR_DESCONTO(Double PRE_VLR_DESCONTO) {
        this.PRE_VLR_DESCONTO = PRE_VLR_DESCONTO;
    }

    public Double getPRE_VLR_TOTAL() {
        return PRE_VLR_TOTAL;
    }

    public void setPRE_VLR_TOTAL(Double PRE_VLR_TOTAL) {
        this.PRE_VLR_TOTAL = PRE_VLR_TOTAL;
    }

    public int getPRE_STATUS() {
        return PRE_STATUS;
    }

    public void setPRE_STATUS(int status) {
        this.PRE_STATUS = status;
    }
}
