package com.example.alan.fluxodetelas;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.fluxodetelas.model.Produto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 03/09/2017.
 */

public class AdapterProduto extends ArrayAdapter<Produto>  {

    private List<Produto> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtNome;
        TextView txtMarca;
        TextView txtTipo;
        TextView txtPeso;

    }

    public AdapterProduto(List<Produto> data, Context context) {
        super (context,R.layout.row_produto, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Produto produto = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_produto, parent, false);
            viewHolder.txtNome = (TextView) convertView.findViewById(R.id.nomeProduto);
            viewHolder.txtMarca = (TextView) convertView.findViewById(R.id.marca);
            viewHolder.txtTipo = (TextView) convertView.findViewById(R.id.tipo);
            viewHolder.txtPeso = (TextView) convertView.findViewById(R.id.peso);

            result = convertView;

            convertView.setTag(viewHolder);
        }else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }

        lastPosition = position;

        viewHolder.txtNome.setText(produto.getNome());
        viewHolder.txtMarca.setText(produto.getMarca());
        viewHolder.txtTipo.setText(produto.getTipo());
        viewHolder.txtPeso.setText(Double.toString(produto.getPeso()));
        viewHolder.txtNome.setTag(position);


        if (position % 2 == 1) {
            convertView.setBackgroundColor(0xffd0dae3);
        } else {
            convertView.setBackgroundColor(0xfff1f1f3);
        }
        ImageView viu = (ImageView)convertView.findViewById(R.id.categoria); //TODO: verificar a categoria e setar a imagem certa

        switch (produto.getCategoria()) {
            case 1:  viu.setImageResource(R.drawable.acogue);
                break;
            case 2:  viu.setImageResource(R.drawable.adega);
                break;
            case 3:  viu.setImageResource(R.drawable.bebidas);
                break;
            case 4:  viu.setImageResource(R.drawable.cereais);
                break;
            case 5:  viu.setImageResource(R.drawable.churrascaria);
                break;
            case 6:  viu.setImageResource(R.drawable.congelados);
                break;
            case 7:  viu.setImageResource(R.drawable.conservas);
                break;
            case 8:  viu.setImageResource(R.drawable.conservas);
                break;
            case 9:  viu.setImageResource(R.drawable.search);
                break;
            case 10: viu.setImageResource(R.drawable.search);
                break;
            case 11: viu.setImageResource(R.drawable.search);
                break;
            case 12: viu.setImageResource(R.drawable.search);
                break;
            default: viu.setImageResource(R.drawable.search);
                break;
        }


        return convertView;
    }
}
