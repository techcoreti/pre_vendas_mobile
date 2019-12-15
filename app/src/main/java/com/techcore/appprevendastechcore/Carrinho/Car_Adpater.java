package com.techcore.appprevendastechcore.Carrinho;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Databases.CarrinhoBD;
import com.techcore.appprevendastechcore.Modelos.ProdutosCart;
import com.techcore.appprevendastechcore.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Car_Adpater extends BaseAdapter {

    // Declare Variables
    private Activity tela;
    private Context mContext;
    private LayoutInflater inflater;
    private List<ProdutosCart> worldpopulationlist;
    private ArrayList<ProdutosCart> arraylist;
    private DecimalFormat df  = new DecimalFormat("#0.00");
    private NumberFormat nm = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));

    public Car_Adpater(Context context, List<ProdutosCart> worldpopulationlist, Activity tela) {
        this.mContext   = context;
        this.tela       = tela;
        this.worldpopulationlist = worldpopulationlist;
        this.inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {
        TextView txtIdTabela0;
        TextView txtIdTabela1;
        TextView produto;
        TextView codigo;
        TextView unidade;
        TextView quantidade;
        TextView valor;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public ProdutosCart getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, final ViewGroup parent) {
        final Car_Adpater.ViewHolder holder;

        if (view == null) {
            holder = new Car_Adpater.ViewHolder();
            view = inflater.inflate(R.layout.activity_get_carrinho, null);

            // Locate the TextViews in listview_item.xml
            holder.txtIdTabela0 = view.findViewById(R.id.txtIdTabela0);
            holder.txtIdTabela1 = view.findViewById(R.id.txtIdTabela1);
            holder.produto      = view.findViewById(R.id.txtProduto);
            holder.codigo       = view.findViewById(R.id.txtCodigo);
            holder.unidade      = view.findViewById(R.id.txtUnidade);
            holder.quantidade   = view.findViewById(R.id.txtQuant);
            holder.valor        = view.findViewById(R.id.txtValor);

            holder.txtIdTabela0.setVisibility(View.GONE);
            holder.txtIdTabela1.setVisibility(View.GONE);

            view.setTag(holder);
        } else {
            holder = (Car_Adpater.ViewHolder) view.getTag();
        }

        final TextView viewIdTabela = view.findViewById(R.id.txtIdTabela1);

        Button btnCarExcluirPrd = view.findViewById(R.id.btnCarExcluirPrd);
        btnCarExcluirPrd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setTitle("Atenção")
                        .setMessage("Tem certeza que deseja remover este produto?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Exclue o item selecionado
                                ExcluirItem(viewIdTabela.getText().toString());

                                // Atualiza o listview
                                worldpopulationlist.clear();
                                notifyDataSetChanged();

                                // Ajusta a tabela de pedidos
                                AjustaPedido();

                                // Efetua a releitura dos produtos
                                ReloadProdutos();

                                // Efetua releitura do pedido
                                loadPedido();

                            }
                        })
                        .setNegativeButton("Não", null)
                        .show();
            }
        });

        // Set the results into TextViews
        holder.txtIdTabela1.setText(worldpopulationlist.get(position).getIdTabela());
        holder.produto.setText(worldpopulationlist.get(position).getProdutoNome());
        holder.codigo.setText(worldpopulationlist.get(position).getProdutoCodigo());
        holder.unidade.setText(worldpopulationlist.get(position).getProdutoUnidade());
        holder.quantidade.setText(df.format(worldpopulationlist.get(position).getProdutoQuantidade()));
        holder.valor.setText(nm.format(worldpopulationlist.get(position).getProdutoValorTotal()));

        return view;
    }

    public void ExcluirItem(String v){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(mContext);
        SQLiteDatabase db   = DbHelper.getWritableDatabase();
        db.delete("tbProdutos", "ID = ?", new String[]{v});
    }

    public void AjustaPedido(){
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(mContext);
        SQLiteDatabase dbW  = DbHelper.getWritableDatabase();
        SQLiteDatabase dbR  = DbHelper.getReadableDatabase();

        String[] cols = new String[]{"Sum(ProdutoValorTotal) as Total"};
        Cursor cur    = dbR.query("tbProdutos", cols, null, null, null, null, null);
        cur.moveToFirst();

        ContentValues val = new ContentValues();
        val.put("ValorSD",  cur.getDouble(0));
        val.put("Desconto", 0);
        val.put("ValorCD",  cur.getDouble(0));
        dbW.update("tbPedidos",val,null,null);

        cur.close();
    }

    public void ReloadProdutos(){
        TextView edtSize    = tela.findViewById(R.id.dspQtdeItens);
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(mContext);
        SQLiteDatabase db   = DbHelper.getReadableDatabase();
        Cursor cur;

        /**
         * Carrega todos os produtos já inseridos
         */
        String[] colunas = new String[]{"ID","ProdutoCodigo","ProdutoNome","ProdutoUnidade","ProdutoQuantidade","ProdutoValorTotal"};
        cur = db.query("tbProdutos",colunas,null,null,null,null,"ID");

        if (cur.moveToFirst()) {
            do {
                ProdutosCart pr = new ProdutosCart();
                pr.setIdTabela(cur.getString(0));
                pr.setProdutoCodigo(cur.getString(1));
                pr.setProdutoNome(cur.getString(2));
                pr.setProdutoUnidade(cur.getString(3));
                pr.setProdutoQuantidade(cur.getDouble(4));
                pr.setProdutoValorTotal(cur.getDouble(5));

                // Ajusta o listview
                worldpopulationlist.add(pr);
                notifyDataSetChanged();

            } while (cur.moveToNext());

            // Exibe a quantidade de produtos no carrinho
            edtSize.setText(df.format(cur.getCount()));
        }
        db.close();
    }

    public void loadPedido(){
        TextView edtValSD = tela.findViewById(R.id.txtTotalPedido);
        TextView edtValCD = tela.findViewById(R.id.dspValSemDesc);
        TextView edtDesc  = tela.findViewById(R.id.txtValDesc);

        CarrinhoBD DbHelper = CarrinhoBD.getInstance(mContext);
        SQLiteDatabase db  = DbHelper.getWritableDatabase();
        Cursor cur;

        /**
         * Carrega os dados do pedido
         */
        String[] colunas = new String[]{"ID","ValorSD","Desconto","ValorCD"};
        cur = db.query("tbPedidos",colunas,null,null,null,null,null);

        if (cur.moveToFirst()){
            StsSys.gCodigoPedido = cur.getInt(0);
            StsSys.gValorTotal   = cur.getDouble(0);
            edtValSD.setText(nm.format(cur.getDouble(1)));
            edtDesc.setText(nm.format(cur.getDouble(2)));
            edtValCD.setText(nm.format(cur.getDouble(3)));
        }
        cur.close();
    }

}