package com.example.alan.fluxodetelas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alan.fluxodetelas.model.Cliente;
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.ProdutosReceitas;
import com.example.alan.fluxodetelas.model.Receita;
import com.example.alan.fluxodetelas.model.ReceitasFavoritas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaginaInicial extends AppCompatActivity {
    ImageView btReceitas;
    Cliente cliente;
    List<Receita> receitas;
    LinearLayout hv;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_inicial);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btReceitas=(ImageView)findViewById(R.id.receitas);
        cliente = ClienteSingleton.getInstance().getCliente();//(Cliente) it.getSerializableExtra("cliente");
        Toast.makeText(getApplicationContext(), "Bem vindo, "+cliente.getNome(), Toast.LENGTH_SHORT).show();

    }
    @Override
    protected void onStart() {
        super.onStart();
        rv = (RecyclerView) findViewById(R.id.receitasFavoritas);

        receitas=new ArrayList<>();
        AdapterReceitasHorizontal arh = new AdapterReceitasHorizontal(receitas);
        rv.setAdapter(arh);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(llm);

        if (cliente.getReceitasFavoritas().size()>0)
            pegarReceitas(cliente.getReceitasFavoritas(),0);

        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent it=new Intent(PaginaInicial.this,DetalhesReceita.class);
                        it.putExtra("receita",receitas.get(position));
                        startActivity(it);
                    }
                })
        );

    }

    public void abreReceitas (View view) {
        Intent intent = new Intent(this,Receitas.class);
        startActivity(intent);
    }
    public void abreDespensas (View view) {
        Intent intent = new Intent(this,Despensas.class);
        intent.putExtra("cliente",cliente);
        startActivity(intent);
    }
    public void abreProdutos (View view) {
        Intent intent = new Intent(this,Produtos.class);
        intent.putExtra("cliente",cliente);
        startActivity(intent);
    }

    void pegarReceitas(final List<ReceitasFavoritas> rf, final int position) {
        RequestQueue req = Volley.newRequestQueue(this);
        final Receita r = new Receita();
        String objURL = Constantes.url+"receitas/"+rf.get(position).getReceita();

        JsonObjectRequest objreq = new JsonObjectRequest(Request.Method.GET, objURL,null,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject receitaObj = response;
                            Receita receita = new Receita();
                            int id = receitaObj.getInt("id");
                            String titulo = receitaObj.getString("titulo");
                            int cliente = receitaObj.getInt("cliente");
                            double quantidade = receitaObj.getDouble("quantidade");
                            String modoPreparo = receitaObj.getString("modoPreparo");
                            JSONArray produtosReceitas = receitaObj.getJSONArray("produtosReceitas");
                            for (int j=0;j<produtosReceitas.length();j++) {
                                ProdutosReceitas pr = new ProdutosReceitas();
                                JSONObject obj = (JSONObject)produtosReceitas.get(j);
                                pr.setProduto(new Produto(obj.getInt("produto")));
                                pr.setQuantidade(obj.getDouble("quantidade"));
                                pr.setId(obj.getInt("id"));
                                receita.addProdutosReceitas(pr);
                            }
                            receita.setId(id);
                            receita.setTitulo(titulo);
                            receita.setCliente(cliente);
                            receita.setQuantidade(quantidade);
                            receita.setModoPreparo(modoPreparo);
                            receitas.add(receita);

                            int pos= 1+position;
                            if (pos<rf.size()) {
                                pegarReceitas(rf,pos);
                            }
                            else {
                                //criarListaFavoritas();
                                AdapterReceitasHorizontal arh = new AdapterReceitasHorizontal(receitas);
                                rv.setAdapter(arh);
                                LinearLayoutManager llm = new LinearLayoutManager(PaginaInicial.this);
                                llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                                rv.setLayoutManager(llm);
                            }

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }
                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", "Error");
                    }
                }
        );
        req.add(objreq);

    }

    void criarListaFavoritas() {
        for (int i=0;i<receitas.size();i++) {
            TextView rec = new TextView(this);
            rec.setText(receitas.get(i).getTitulo());

            rec.setTextAppearance(this, android.R.style.TextAppearance_Medium);
            rec.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            hv.addView(rec);
        }

    }

}
