package com.example.alan.fluxodetelas;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alan.fluxodetelas.model.Despensa;

import java.util.List;

import static android.R.drawable.btn_star_big_on;
import static android.R.drawable.screen_background_light_transparent;

/**
 * Created by Alan on 03/09/2017.
 */

public class AdapterDespensa extends ArrayAdapter<Despensa> {

    private List<Despensa> dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtNome;
        TextView txtLocalizacao;
        ImageView img;
    }

    public AdapterDespensa(List<Despensa> data, Context context) {
        super (context,R.layout.row_despensa, data);
        this.dataSet = data;
        this.mContext = context;
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Despensa despensa = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_despensa, parent, false);
            viewHolder.txtNome = (TextView) convertView.findViewById(R.id.nomeDespensa);
            viewHolder.txtLocalizacao = (TextView) convertView.findViewById(R.id.localizacao);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.vencendo);

            result = convertView;

            convertView.setTag(viewHolder);
        }else {
                viewHolder = (ViewHolder) convertView.getTag();
                result=convertView;
            }
        lastPosition = position;

        viewHolder.txtNome.setText(despensa.getNome());
        viewHolder.txtLocalizacao.setText(despensa.getLocalizacao());
        viewHolder.txtNome.setTag(position);



        if (position % 2 == 1) {
            convertView.setBackgroundColor(0xffd0dae3);
        } else {
            convertView.setBackgroundColor(0xfff1f1f3);
        }

        ImageView viu = (ImageView)convertView.findViewById(R.id.vencendo);
        if (despensa.isVencendo()) {
            viu.setImageResource(R.drawable.exclmation);
        }
        else {
            viu.setImageResource(R.drawable.transparent);
        }

        return convertView;
    }
}
