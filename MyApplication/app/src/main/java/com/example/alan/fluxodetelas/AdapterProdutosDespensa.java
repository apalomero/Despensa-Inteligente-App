package com.example.alan.fluxodetelas;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.ProdutosDespensa;

import java.util.Calendar;
import java.util.List;

import static android.R.drawable.screen_background_light_transparent;

/**
 * Created by Alan on 03/09/2017.
 */

public class AdapterProdutosDespensa extends ArrayAdapter<ProdutosDespensa>  {

    private List<ProdutosDespensa> dataSet;
    Context mContext;

    private static class ViewHolder { //acho que ele guarda as views que foram scrolladas
        TextView txtNome;
        TextView txtMarca;
        TextView txtTipo;
        TextView txtPeso;
        TextView qtde;
        TextView validade;

    }

    public AdapterProdutosDespensa(List<ProdutosDespensa> data, Context context) {
        super (context,R.layout.row_produtos_despensa, data);
        this.dataSet = data;
        this.mContext = context;
    }
    private int lastPosition = -1;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProdutosDespensa pd = getItem(position);
        AdapterProdutosDespensa.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new AdapterProdutosDespensa.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_produtos_despensa, parent, false);
            viewHolder.txtNome = (TextView) convertView.findViewById(R.id.nomeProduto);
            viewHolder.txtMarca = (TextView) convertView.findViewById(R.id.marca);
            viewHolder.txtTipo = (TextView) convertView.findViewById(R.id.tipo);
            viewHolder.txtPeso = (TextView) convertView.findViewById(R.id.peso);
            viewHolder.validade= (TextView) convertView.findViewById(R.id.validade);
            viewHolder.qtde = (TextView) convertView.findViewById(R.id.quantidadeRow);

            result = convertView;

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (AdapterProdutosDespensa.ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        Produto produto = pd.getProduto();
        viewHolder.txtNome.setText(produto.getNome());
        viewHolder.txtMarca.setText(produto.getMarca());
        viewHolder.txtTipo.setText(produto.getTipo());
        viewHolder.txtPeso.setText(Double.toString(produto.getPeso()));
        String string = String.valueOf(pd.getQuantidade());
        viewHolder.qtde.setText(string);
        string = pd.getValidade();
        viewHolder.validade.setText(string);
        viewHolder.txtNome.setTag(position);


        if (position % 2 == 1) {
            convertView.setBackgroundColor(0xffd0dae3);
        } else {
            convertView.setBackgroundColor(0xfff1f1f3);
        }
        ImageView viu = (ImageView)convertView.findViewById(R.id.categoria);
        viu.setImageResource(R.drawable.transparent);
        if (pd.isVencendo()) {
            viu.setImageResource(R.drawable.exclmation);

        }
        else {
            viu.setImageResource(R.drawable.transparent);
        }

        return convertView;
    }
}
