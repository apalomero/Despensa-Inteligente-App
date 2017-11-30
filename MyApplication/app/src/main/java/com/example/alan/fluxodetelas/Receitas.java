package com.example.alan.fluxodetelas;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alan.fluxodetelas.model.Cliente;
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.ProdutosReceitas;
import com.example.alan.fluxodetelas.model.Receita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class Receitas extends AppCompatActivity {
    TextView results,search;
    private static AdapterReceita adapter;
    ListView listView;
    List<Receita> listaReceitas;
    List<ProdutosReceitas> toAdd;
    RequestQueue requestQueue;
    Cliente cliente;
    String JsonURL = Constantes.url+"receitas";
    String JsonURLProdutosReceitas = Constantes.url+"produtosReceitas";
    TextView.OnEditorActionListener exampleListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_NULL
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                fazerBusca(search.getText().toString());
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);

        listView = (ListView) findViewById(R.id.listaReceitas);
        results = (TextView)findViewById(R.id.umidqualquer);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddReceita();

            }
        });
        search = (TextView) findViewById(R.id.search);
        search.setOnEditorActionListener(exampleListener);

        cliente = ClienteSingleton.getInstance().getCliente();//(Cliente) getIntent().getSerializableExtra("cliente");
        if (getIntent().getStringExtra("busca")!=null)
            JsonURL+="?titulo="+getIntent().getStringExtra("busca");


    }

    @Override
    protected void onStart() {
        super.onStart();
        //Pegando lista de receitas do banco
        listaReceitas = new ArrayList<Receita>();
        ArrayList<Produto> produtosVencendo = (ArrayList)getIntent().getSerializableExtra("produtosVencendo");

        if (produtosVencendo==null) {
            requestQueue = Volley.newRequestQueue(this);


            JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURL,
                    // The second parameter Listener overrides the method onResponse() and passes
                    //JSONArray as a parameter
                    new Response.Listener<JSONArray>() {

                        // Takes the response from the JSON request
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    // Retrieves all JSON objects in response array
                                    JSONObject receitaObj = response.getJSONObject(i);
                                    Receita receita = new Receita();
                                    int id = receitaObj.getInt("id");
                                    String titulo = receitaObj.getString("titulo");
                                    int cliente = receitaObj.getInt("cliente");
                                    double quantidade = receitaObj.getDouble("quantidade");
                                    String modoPreparo = receitaObj.getString("modoPreparo");
                                    String te = receitaObj.getString("tempoExecucao");
                                    int tempoExecucao;
                                    if (te.equals("null") || te == null) {
                                        tempoExecucao = 0;
                                    } else {
                                        tempoExecucao = ((Double)Double.parseDouble(te)).intValue();
                                    }
                                    if (modoPreparo.equals("null"))
                                        modoPreparo = "";
                                    JSONArray produtosReceitas = receitaObj.getJSONArray("produtosReceitas");
                                    for (int j = 0; j < produtosReceitas.length(); j++) {
                                        ProdutosReceitas pr = new ProdutosReceitas();
                                        JSONObject obj = (JSONObject) produtosReceitas.get(j);
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
                                    receita.setTempoExecucao(Integer.toString(tempoExecucao));

                                    listaReceitas.add(receita);
                                    adapter = new AdapterReceita(listaReceitas, getApplicationContext());
                                    listView.setAdapter(adapter);

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
            // Adds the JSON array request "arrayreq" to the request queue
            requestQueue.add(arrayreq);


            //parando de pegar lista


            adapter = new AdapterReceita(listaReceitas, getApplicationContext());
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Receita receita = listaReceitas.get(position);
                    abreReceita(receita);
                }
            });
        } //fim do if 'se nao tiver produto vencendo'
        else {
            for (int i=0;i<produtosVencendo.size();i++)
                pegarReceitasPorProduto(produtosVencendo.get(i));
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    toAdd = (ArrayList) data.getSerializableExtra("produtosReceitas");
                    addProdutosReceitas();
                }
                break;
            }
        }
    }

    public void abreReceita(Receita receita) {
        Intent it = new Intent(this,DetalhesReceita.class);
        it.putExtra("receita",receita);
        //it.putExtra("cliente",cliente);
        startActivity(it);
    }

    void pegarReceitasPorProduto(Produto produto) {
        String URL = Constantes.url+"produtosReceitas/receita-por-produto/"+produto.getId();
        requestQueue = Volley.newRequestQueue(this);
        final ArrayList<Integer> idsReceitas = new ArrayList<>();
        JsonArrayRequest arrayreq = new JsonArrayRequest(URL,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                // Retrieves all JSON objects in response array
                                JSONObject obj = response.getJSONObject(i);
                                Integer receita = (Integer)obj.getInt("receita");
                                idsReceitas.add(receita);
                            }
                            if (idsReceitas.size()>0)
                                pegarReceitasPorIds(idsReceitas,0);
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
        // Adds the JSON array request "arrayreq" to the request queue
        requestQueue.add(arrayreq);
    }

    void pegarReceitasPorIds (final ArrayList<Integer> ids, final int position) {
        RequestQueue req = Volley.newRequestQueue(this);
        final Receita r = new Receita();
        String objURL = Constantes.url+"receitas/"+ids.get(position);

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
                            String te = receitaObj.getString("tempoExecucao");
                            int tempoExecucao;
                            if (te.equals("null") || te == null) {
                                tempoExecucao = 0;
                            } else {
                                tempoExecucao = ((Double)Double.parseDouble(te)).intValue();
                            }
                            if (modoPreparo.equals("null"))
                                modoPreparo = "";
                            JSONArray produtosReceitas = receitaObj.getJSONArray("produtosReceitas");
                            for (int j = 0; j < produtosReceitas.length(); j++) {
                                ProdutosReceitas pr = new ProdutosReceitas();
                                JSONObject obj = (JSONObject) produtosReceitas.get(j);
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
                            receita.setTempoExecucao(Integer.toString(tempoExecucao));

                            listaReceitas.add(receita);

                            int pos= 1+position;
                            if (pos<ids.size()) {
                                pegarReceitasPorIds(ids,pos);
                            }
                            else {
                                adapter = new AdapterReceita(listaReceitas, getApplicationContext());
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                        Receita receita = listaReceitas.get(position);
                                        abreReceita(receita);
                                    }
                                });
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

    void gotoAddReceita() {
        Intent it = new Intent(this,addReceita.class);
        startActivityForResult(it, 1);
    }

    void addProdutosReceitas() {
        int id=0;

        if (listaReceitas.size()>0) {
            id = listaReceitas.get(listaReceitas.size()-1).getId()+1;
        }
        //setar todos os campos receita de toAdd
        for(int i=0;i<toAdd.size();i++) {
            toAdd.get(i).setReceita(id);
        }
        //fazer request pra cada um

        for (int i=0;i<toAdd.size();i++) {ProdutosReceitas pr = toAdd.get(i);
            JSONObject jsonProdutosReceita;
            RequestQueue queue = Volley.newRequestQueue(Receitas.this);
            try {

                jsonProdutosReceita = new JSONObject();
                jsonProdutosReceita.put("quantidade", pr.getQuantidade());
                jsonProdutosReceita.put("produto", pr.getProduto().getId());
                jsonProdutosReceita.put("receita",pr.getReceita());

                final String stringRequest = jsonProdutosReceita.toString();

                StringRequest request= new StringRequest(Request.Method.POST,JsonURLProdutosReceitas,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Falha ao adicionar produtos na receita", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    public String getBodyContentType() {
                        return String.format("application/json; charset=utf-8");
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return stringRequest == null ? null : stringRequest.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    stringRequest, "utf-8");
                            return null;
                        }
                    }
                };

                ;
                queue.add(request);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void fazerBusca(String busca) {
        Intent it = new Intent(this,Receitas.class);
        it.putExtra("busca",busca);
        startActivity(it);
        finish();
    }


}
