package com.techcore.appprevendastechcore.Produtos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techcore.appprevendastechcore.Modelos.CarregarProdutos;
import com.techcore.appprevendastechcore.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdapterListView extends BaseAdapter {
    private LayoutInflater inflater;
    private List<CarregarProdutos> worldpopulationlist;
    private ArrayList<CarregarProdutos> arraylist;

    public AdapterListView(Context context, List<CarregarProdutos> worldpopulationlist) {

        this.worldpopulationlist = worldpopulationlist;
        inflater                 = LayoutInflater.from(context);
        this.arraylist           = new ArrayList<>();

        this.arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {
        TextView produto;
        TextView codigo;
        TextView unidade;
        TextView valor;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public CarregarProdutos getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.content_main, null);

            // Locate the TextViews in listview_item.xml
            holder.produto    = view.findViewById(R.id.txtProduto);
            holder.codigo     = view.findViewById(R.id.txtCodigo);
            holder.unidade    = view.findViewById(R.id.txtUnidade);
            holder.valor      = view.findViewById(R.id.txtValor);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        NumberFormat nfVal = NumberFormat.getCurrencyInstance(Locale.getDefault());

        // Insere os dados nas textviews
        holder.produto.setText(worldpopulationlist.get(position).getPRD_DESCRICAO());
        holder.codigo.setText(String.format(Locale.getDefault(),"%06d", worldpopulationlist.get(position).getPRD_CODIGO()));
        holder.unidade.setText(worldpopulationlist.get(position).getPRD_UNIDADE());
        holder.valor.setText(nfVal.format(worldpopulationlist.get(position).getPRD_VALOR()));

        return view;
    }

    // Filter Class
    public void filter(String charText) {

        final String Texto;

        Texto = charText.toUpperCase(Locale.getDefault());
        worldpopulationlist.clear();

        if (Texto.length() == 0) {
            worldpopulationlist.addAll(arraylist);
        }else{
            for (CarregarProdutos wp : arraylist){
                if (String.format(Locale.getDefault(), "%01d", wp.getPRD_CODIGO()).contains(Texto)) {
                    worldpopulationlist.add(wp);
                }else{
                    if (wp.getPRD_DESCRICAO().contains(Texto)) {
                        worldpopulationlist.add(wp);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }
}
