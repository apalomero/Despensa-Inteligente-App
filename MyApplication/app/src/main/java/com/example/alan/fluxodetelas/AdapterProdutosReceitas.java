package com.example.alan.fluxodetelas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.ProdutosDespensa;
import com.example.alan.fluxodetelas.model.ProdutosReceitas;

import java.util.List;

/**
 * Created by Alan on 03/09/2017.
 */

public class AdapterProdutosReceitas extends ArrayAdapter<ProdutosReceitas>  {

    private List<ProdutosReceitas> dataSet;
    Context mContext;

    private static class ViewHolder { //acho que ele guarda as views que foram scrolladas
        TextView txtNome;
        TextView txtTipo;
        TextView qtde;

    }

    public AdapterProdutosReceitas(List<ProdutosReceitas> data, Context context) {
        super (context,R.layout.row_produtos_receitas, data);
        this.dataSet = data;
        this.mContext = context;
    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProdutosReceitas pr = getItem(position);
        AdapterProdutosReceitas.ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new AdapterProdutosReceitas.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_produtos_receitas, parent, false);
            viewHolder.txtNome = (TextView) convertView.findViewById(R.id.nomeProduto);
            viewHolder.txtTipo = (TextView) convertView.findViewById(R.id.tipo);
            viewHolder.qtde = (TextView) convertView.findViewById(R.id.qtd);

            result = convertView;

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (AdapterProdutosReceitas.ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        Produto produto = pr.getProduto();
        viewHolder.txtNome.setText(produto.getNome());
        viewHolder.txtTipo.setText(produto.getTipo());
        String string = String.valueOf(pr.getQuantidade());
        viewHolder.qtde.setText(string);
        viewHolder.txtNome.setTag(position);


        if (position % 2 == 1) {
            convertView.setBackgroundColor(0xffd0dae3);
        } else {
            convertView.setBackgroundColor(0xfff1f1f3);
        }
        ImageView viu = (ImageView)convertView.findViewById(R.id.categoria);
        if (position==2) {
            viu.setImageResource(R.drawable.search);
        }
        else {
            viu.setImageResource(R.drawable.edit);
        }

        return convertView;
    }
}
