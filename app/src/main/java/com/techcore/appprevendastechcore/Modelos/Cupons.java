package com.techcore.appprevendastechcore.Modelos;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Stream;

public class Cupons extends ArrayList<Integer> {

    private String nomeVendedor;
    private int idVendedor;

    public Cupons(String nomeVendedor, int idVendedor ){

        this.nomeVendedor = nomeVendedor;
        this.idVendedor = idVendedor;
    }

    public void setNomeVendedor(String nomeVendedor){
        this.nomeVendedor = nomeVendedor;
    }

    public String getNomeVendedor(){
        return this.nomeVendedor;
    }

    public void setIdVendedor(int idVendedor){
        this.idVendedor = idVendedor;
    }

    public int getIdVendedor(){
        return this.idVendedor;
    }

    @Override
    public Stream<Integer> stream() {
        return null;
    }
}
