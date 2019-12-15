package com.techcore.appprevendastechcore.Vendedores;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.techcore.appprevendastechcore.Modelos.Vendedores;
import com.techcore.appprevendastechcore.R;
import java.util.ArrayList;
import java.util.List;

public class Adapter_Vendedores extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<Vendedores> worldpopulationlist;

    Adapter_Vendedores(Context context, List<Vendedores> worldpopulationlist) {
        mContext                 = context;
        inflater                 = LayoutInflater.from(mContext);
        this.worldpopulationlist = worldpopulationlist;
        ArrayList<Vendedores> arraylist = new ArrayList<>();
        arraylist.addAll(worldpopulationlist);
    }

    public class ViewHolder {
        TextView Vendedor;
        TextView Apelido;
        TextView Codigo;
    }

    @Override
    public int getCount() {
        return worldpopulationlist.size();
    }

    @Override
    public Vendedores getItem(int position) {
        return worldpopulationlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.lsview_vendedores, null);

            // Localizar os campos de textos no list view
            holder.Vendedor = view.findViewById(R.id.txtVendedor);
            holder.Apelido  = view.findViewById(R.id.txtApelido);
            holder.Codigo   = view.findViewById(R.id.txtCodigo);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.Vendedor.setText(worldpopulationlist.get(position).getFUN_NOME());
        holder.Apelido.setText(worldpopulationlist.get(position).getFUN_APELIDO());
        holder.Codigo.setText(worldpopulationlist.get(position).getFUN_CODIGO());

        return view;
    }

}
