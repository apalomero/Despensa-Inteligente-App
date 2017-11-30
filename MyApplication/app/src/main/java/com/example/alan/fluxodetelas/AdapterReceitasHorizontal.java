package com.example.alan.fluxodetelas;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alan.fluxodetelas.model.Receita;

import java.util.List;

/**
 * Created by Alan on 17/11/2017.
 */

public class AdapterReceitasHorizontal extends RecyclerView.Adapter<AdapterReceitasHorizontal.MyViewHolder> {

    private List<Receita> receitas;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeReceita;

        public MyViewHolder(View view) {
            super(view);
            nomeReceita = (TextView) view.findViewById(R.id.nomeFav);
        }
    }

    public AdapterReceitasHorizontal(List<Receita> countryList) {
        this.receitas = countryList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Receita c = receitas.get(position);
        holder.nomeReceita.setText(c.getTitulo());
    }

    @Override
    public int getItemCount() {
        return receitas.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.col_fav,parent, false);
        return new MyViewHolder(v);
    }
}
