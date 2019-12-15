package com.techcore.appprevendastechcore.Listar_Vendas;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.techcore.appprevendastechcore.Modelos.ListarPreVendas;
import com.techcore.appprevendastechcore.R;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ADListarVendas extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<ListarPreVendas> worldpopulationlist;
    private NumberFormat nm = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));

    ADListarVendas(Context context, List<ListarPreVendas> worldpopulationlist) {
        mContext                 = context;
        inflater                 = LayoutInflater.from(mContext);
        this.worldpopulationlist = worldpopulationlist;
        ArrayList<ListarPreVendas> arraylist = new ArrayList<>();
        arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {
        TextView CodigoVenda;
        TextView VlrTotal;
        TextView Status;
        TextView ValorCup;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public ListarPreVendas getItem(int position) {
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
            view = inflater.inflate(R.layout.ls_cont_view_vendas, null);

            // Locate the TextViews in listview_item.xml
            holder.CodigoVenda = view.findViewById(R.id.txtVendasTitulo);
            holder.Status      = view.findViewById(R.id.txtLstVendasSts);
            holder.VlrTotal    = view.findViewById(R.id.txtLstVendasValor);
            holder.ValorCup    = view.findViewById(R.id.txtLstVendasValor2);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Insere os dados nas textviews
        holder.CodigoVenda.setText(
                "CÓDIGO: "
                        + worldpopulationlist.get(position).getPRE_CODIGO_VENDAS()
                        + "  "
                        + "DATA: "
                        + worldpopulationlist.get(position).getDATA_VENDA()

        );

        switch (worldpopulationlist.get(position).getPRE_STATUS()){
            case 1 :
                holder.Status.setText("Pendente");
                holder.Status.setTextColor(Color.parseColor("#CC0000"));
                break;

            case 2 :
                holder.Status.setText("Finalizado");
                holder.Status.setTextColor(Color.parseColor("#2E7D32"));
                break;
        }

        switch (worldpopulationlist.get(position).getCupStatus()){
            case 0 :
                holder.ValorCup.setText("Pendente");
                holder.ValorCup.setTextColor(Color.parseColor("#000000"));
                break;

            case 1 :
                holder.ValorCup.setText(nm.format(worldpopulationlist.get(position).getCupValor()));
                holder.ValorCup.setTextColor(Color.parseColor("#2E7D32"));
                break;

            case 2 :
                holder.ValorCup.setText("Cancelado");
                holder.ValorCup.setTextColor(Color.parseColor("#CC0000"));
                break;
        }

        holder.VlrTotal.setText(nm.format(worldpopulationlist.get(position).getPRE_VLR_TOTAL()));

        return view;
    }

}
