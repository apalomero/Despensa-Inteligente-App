package com.example.alan.fluxodetelas;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.Receita;

import java.util.List;

import static android.R.drawable.btn_star_big_on;

/**
 * Created by Alan on 03/09/2017.
 */

public class AdapterReceita extends ArrayAdapter<Receita>{

    private List<Receita> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtNome;
        TextView txtPorcoes;
        TextView txtTempoPreparo;
    }

    public AdapterReceita(List<Receita> data, Context context) {
        super (context,R.layout.row_receita, data);
        this.dataSet = data;
        this.mContext = context;
    }
    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Receita receita = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_receita, parent, false);
            viewHolder.txtNome = (TextView) convertView.findViewById(R.id.nomeReceita);
            viewHolder.txtPorcoes = (TextView) convertView.findViewById(R.id.porcoes);
            viewHolder.txtTempoPreparo = (TextView) convertView.findViewById(R.id.tempoPreparo);

            result = convertView;

            convertView.setTag(viewHolder);
        }else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }
        lastPosition = position;

        viewHolder.txtNome.setText(receita.getTitulo());
        viewHolder.txtTempoPreparo.setText(receita.getTempoExecucao());
        viewHolder.txtPorcoes.setText(Double.toString(receita.getQuantidade()));
        viewHolder.txtNome.setTag(position);



        if (position % 2 == 1) {
            convertView.setBackgroundColor(0xffd0dae3);
        } else {
            convertView.setBackgroundColor(0xfff1f1f3);
        }




        return convertView;
    }
}
