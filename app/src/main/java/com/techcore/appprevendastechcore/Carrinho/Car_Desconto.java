package com.techcore.appprevendastechcore.Carrinho;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import com.techcore.appprevendastechcore.Configurar.StsSys;
import com.techcore.appprevendastechcore.Databases.CarrinhoBD;
import com.techcore.appprevendastechcore.R;
import java.text.NumberFormat;
import java.util.Locale;

public class Car_Desconto extends AppCompatActivity {

    NumberFormat nmFormat;
    NumberFormat nmPorcent;
    TextView edtTotal;
    TextView txtDescontoVlr;
    Switch optDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descontos);

        // Formatação para monotário
        nmFormat = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
        nmFormat.setMaximumFractionDigits(2);
        nmFormat.setMaximumIntegerDigits(8);

        // Formatação para porcentagem
        nmPorcent = NumberFormat.getPercentInstance(new Locale("pt","BR"));
        nmPorcent.setMaximumFractionDigits(2);
        nmPorcent.setMinimumFractionDigits(2);
        nmPorcent.setMaximumIntegerDigits(3);

        // Exibe o valor atual da venda
        edtTotal = findViewById(R.id.txtVlrSemDesconto);
        edtTotal.setText(nmFormat.format(StsSys.vlrSemDesconto));

        // Prepara a variavel
        txtDescontoVlr = findViewById(R.id.txtDescontoVlr);

        // Tipo de desconto
        optDesc = findViewById(R.id.optDesc);

        optDesc.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if (optDesc.isChecked()){
                    // Atualiza o Switch
                    optDesc.setText("Desconto (%)");

                    // Zera o valor do campo de digitação
                    txtDescontoVlr.setText("");

                    // Alterar o hint do campo de digitação
                    txtDescontoVlr.setHint("0,00%");

                    // Atualiza o valor de exibição
                    edtTotal.setText(nmFormat.format(StsSys.vlrSemDesconto));
                }else{
                    // Atualiza o Switch
                    optDesc.setText("Desconto (R$)");

                    // Zera o valor do campo de digitação
                    txtDescontoVlr.setText("");

                    // Alterar o hint do campo de digitação
                    txtDescontoVlr.setHint("R$0,00");

                    // Atualiza o valor de exibição
                    edtTotal.setText(nmFormat.format(StsSys.vlrSemDesconto));
                }
            }
        });

        txtDescontoVlr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!optDesc.isChecked()){
                    DescontoPorValor();
                }else{
                    DescontoPorcentagem();
                }
            }
        });

        Button btn = findViewById(R.id.btnAplicar);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt = findViewById(R.id.txtDescontoVlr);
                if (!txt.getText().toString().isEmpty()) {

                    /*
                      Efetua o calculo do desconto e antes de efetivar
                      checa se o percentual não ultrpassa o limte máximo
                     */
                    CalcularDesconto();

                } else {
                    new AlertDialog.Builder(Car_Desconto.this)
                            .setTitle("Atenção")
                            .setMessage("Informe o valor do desconto")
                            .setNeutralButton("OK", null)
                            .show();
                    txt.requestFocus();
                }
            }
        });

        Button btnCancelar = findViewById(R.id.btnDesconto);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void CalcularDesconto() {
        CarrinhoBD DbHelper = CarrinhoBD.getInstance(Car_Desconto.this);
        SQLiteDatabase db   = DbHelper.getWritableDatabase();
        ContentValues val   = new ContentValues();

        // Se a margem ofertada é maior que a margem permitida, impede o desconto
        if (StsSys.mrgDoDesconto > StsSys.gMargemMaxDesconto) {
            new AlertDialog.Builder(Car_Desconto.this)
                    .setTitle("Atenção")
                    .setMessage("O valor do desconto é maior do que o permitido")
                    .setNeutralButton("OK", null)
                    .show();
        } else {

            /*
              Atualiza o pedido atual com os valores obtidos no desconto
             */
            val.put("ValorSD",  StsSys.vlrSemDesconto);
            val.put("ValorCD",  StsSys.vlrComDesconto);
            val.put("Desconto", StsSys.vlrDoDesconto);

            db.update("tbPedidos",val,null,null);

            loadPedido();
            finish();
        }
    }

    public void loadPedido(){

        TextView vlrSemDesconto = StsSys.mActivity.findViewById(R.id.dspValSemDesc);
        TextView vlrComDesconto = StsSys.mActivity.findViewById(R.id.txtTotalPedido);
        TextView vlrDoDesconto  = StsSys.mActivity.findViewById(R.id.txtValDesc);

        CarrinhoBD DbHelper = CarrinhoBD.getInstance(Car_Desconto.this);
        SQLiteDatabase db  = DbHelper.getWritableDatabase();
        Cursor cur;

        // Carrega os dados do pedido
        String[] colunas = new String[]{"ValorSD","ValorCD","Desconto"};
        cur = db.query("tbPedidos",colunas,null,null,null,null,null);

        if (cur.moveToFirst()){
            vlrSemDesconto.setText(nmFormat.format(cur.getDouble(0)));
            vlrComDesconto.setText(nmFormat.format(cur.getDouble(1)));
            vlrDoDesconto.setText(nmFormat.format(cur.getDouble(2)));
        }
        cur.close();
    }

    private void DescontoPorValor(){

        // Exibe o valor do desconto em porcentagem
        if (txtDescontoVlr.getText().toString().isEmpty()) {
            edtTotal.setText(nmFormat.format(StsSys.vlrSemDesconto));
        } else {
            // Desconto em valor
            StsSys.vlrDoDesconto = Double.parseDouble(txtDescontoVlr.getText().toString().replace(',','.'));

            // Valor atualizado com desconto
            StsSys.vlrComDesconto = (StsSys.vlrSemDesconto - StsSys.vlrDoDesconto);

            // Armazena e exibe o valor com desconto
            edtTotal.setText(nmFormat.format(StsSys.vlrComDesconto));

            // Gera o valor em percentual
            StsSys.mrgDoDesconto = (StsSys.vlrComDesconto / StsSys.vlrSemDesconto) * 100;
            StsSys.mrgDoDesconto = (100 - StsSys.mrgDoDesconto);
        }
    }

    private void DescontoPorcentagem(){
        // Exibe o valor do desconto em porcentagem
        if (txtDescontoVlr.getText().toString().isEmpty()) {
            edtTotal.setText(nmFormat.format(StsSys.vlrSemDesconto));
        } else {
            // Desconto em porcentagem
            StsSys.mrgDoDesconto = Double.parseDouble(txtDescontoVlr.getText().toString().replace(',','.'));

            // Valor atualizado com desconto
            StsSys.vlrDoDesconto  = (StsSys.vlrSemDesconto * StsSys.mrgDoDesconto) / 100;
            StsSys.vlrComDesconto = (StsSys.vlrSemDesconto - StsSys.vlrDoDesconto);

            // Armazena e exibe o valor com desconto
            edtTotal.setText(nmFormat.format(StsSys.vlrComDesconto));
        }
    }
}
