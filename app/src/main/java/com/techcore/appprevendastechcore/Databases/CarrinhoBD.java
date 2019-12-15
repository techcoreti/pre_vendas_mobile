package com.techcore.appprevendastechcore.Databases;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import com.techcore.appprevendastechcore.Modelos.ProdutosCart;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoBD extends SQLiteOpenHelper {

    private static CarrinhoBD instance;
    private static final String bdNome       = "bdPreVendas.db";
    private static final String tbConfig     = "tbConfig";
    private static final String tbPedidos    = "tbPedidos";
    private static final String tbProdutos   = "tbProdutos";
    private static final String tbDescontos  = "tbDescontos";
    private static final String tbVendedores = "tbVendedores";
    private static final int    bdVersao     = 1;
    private final Context mContext;

    /**
     * tabela configurações
     */
    private static final String ID           = "ID";         // Indice do registro
    private static final String CodigoFilial = "CodigoFilial";
    private static final String UrlServer    = "UrlServer";

    public CarrinhoBD(Context context){
        super(context, bdNome, null, bdVersao);
        this.mContext = context;
    }

    public static synchronized CarrinhoBD getInstance(Context context) {
        if (instance == null) {
            instance = new CarrinhoBD(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        /**
         * Tabela das configurações
         */
        try {
            String sql = "Create table " + tbConfig + "("
                    + ID                + " integer primary key autoincrement,"
                    + CodigoFilial      + " Varchar(10),"
                    + UrlServer         + " Varchar(255)"
                    + ")";
            db.execSQL(sql);
        }catch (SQLException e){
            Toast.makeText(mContext, "Erro ao criar o banco de dados Config, Erro: " +  e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /**
         * Tabela de descontos
         */
        try{
            String sql = "Create Table if not exists " + tbDescontos + "("
                    + "ID           integer primary key autoincrement," // Chave primaria da tabela
                    + "Codigo       Varchar(10),"                       // Codigo de referência na tabela de desconto
                    + "Descricao    Varchar(50),"                       // Descrição / nome do desconto
                    + "Margem       Real(18,3),"                        // Margem máxima de desconto permitido
                    + "Status       Integer not null,"                  // Habilita e desabilita o desconto
                    + "Comissao     integer not null"                   // Percentual de comissão oferecida pro desconto
                    + ")";
            db.execSQL(sql);
        }catch (SQLException e){
            Toast.makeText(mContext, "Erro ao criar o banco de dados, Erro: " +  e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /**
         * Tabela de vendedores
         */
        try{
            String sql = "Create Table if not exists " + tbVendedores + "("
                    + "ID               integer primary key autoincrement,"
                    + "Codigo           Varchar(10),"
                    + "CodigoDesconto   Varchar(10),"
                    + "Nome             Varchar(50),"
                    + "Apelido          Varchar(25)"
                    + ")";
            db.execSQL(sql);
        }catch (SQLException e){
            Toast.makeText(mContext, "Erro ao criar o banco de dados, Erro: " +  e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /**
         * Tabela dos pedidos
         */
        try{
            String sql = "Create Table if not exists " + tbPedidos + "("
                    + "ID               integer primary key autoincrement,"
                    + "Data             Varchar(10),"
                    + "Vendedor         Varchar(10),"
                    + "ValorSD          Real(18,3),"
                    + "CodigoDesconto   Varchar(10),"
                    + "Desconto         Real(18,3),"
                    + "ValorCD          Real(18,3),"
                    + "CodigoFilial     Varchar(10),"
                    + "CodigoTerminal   Varchar(10),"
                    + "CodigoPrecos     Varchar(10),"
                    + "Status           integer not null default 0"
                    + ")";
            db.execSQL(sql);
        }catch (SQLException e){
            Toast.makeText(mContext, "Erro ao criar o banco de dados, Erro: " +  e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        /**
         * Tabela dos produtos
         */
        try {
            String sql = "Create Table " + tbProdutos + "("
                    + "ID                   integer primary key autoincrement,"
                    + "ProdutoCodigo"       + " Varchar(10),"
                    + "ProdutoNome"         + " Varchar(80),"
                    + "ProdutoUnidade"      + " Varchar(4),"
                    + "ProdutoValor"        + " Real(18,3),"
                    + "ProdutoQuantidade"   + " Real(18,3),"
                    + "ProdutoValorTotal"   + " Real(18,3),"
                    + "ProdutoCest"         + " Integer not null default 0,"
                    + "ProdutoNcm"          + " Integer not null default 0"
                    + ")";
            db.execSQL(sql);
        }catch (SQLException e){
            Toast.makeText(mContext, "Erro ao criar o banco de dados, Erro: " +  e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("Drop table if exists " + tbProdutos);
        onCreate(db);
    }

    public List<ProdutosCart> getProdutos(){
        List<ProdutosCart> produtosList = new ArrayList<ProdutosCart>();
        String sql = "Select * from " + tbProdutos;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            do {
                ProdutosCart pr = new ProdutosCart();
                pr.setProdutoCodigo(c.getString(c.getColumnIndex("ProdutoCodigo")));
                pr.setProdutoNome(c.getString(c.getColumnIndex("ProdutoNome")));
                pr.setProdutoUnidade(c.getString(c.getColumnIndex("ProdutoUnidade")));
                pr.setProdutoValor(c.getDouble(c.getColumnIndex("ProdutoValor")));
                pr.setProdutoQuantidade(c.getDouble(c.getColumnIndex("ProdutoQuantidade")));
                pr.setProdutoValorTotal(c.getDouble(c.getColumnIndex("ProdutoValorTotal")));

                // Adicionando os dados na classe
                produtosList.add(pr);

            } while (c.moveToNext());
        }

        db.close();
        return produtosList;
    }
}
