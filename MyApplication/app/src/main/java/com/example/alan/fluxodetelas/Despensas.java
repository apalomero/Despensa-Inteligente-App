package com.example.alan.fluxodetelas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alan.fluxodetelas.model.Cliente;
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.Despensa;
import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.ProdutosDespensa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Despensas extends AppCompatActivity {

    List<Despensa> listaDespensas;
    ListView listView;
    private static AdapterDespensa adapter;
    ProdutosDespensa prodDesp;
    List<ProdutosDespensa> listaProdutos;
    RequestQueue requestQueue;
    String JsonURL = Constantes.url+"despensas/cliente/";
    Cliente cliente;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despensas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cliente = ClienteSingleton.getInstance().getCliente();//(Cliente) getIntent().getSerializableExtra("cliente");
        JsonURL+=cliente.getId();

        listView = (ListView) findViewById(R.id.listaDespensas);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Despensas.this, addDespensa.class);
                //it.putExtra("cliente",cliente);
                startActivity(it);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Despensa despensa = listaDespensas.get(position);
                Intent it = new Intent(Despensas.this,detalhesDespensa.class);
                it.putExtra("despensa",despensa);
                startActivity(it);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                excluirDespensa(listaDespensas.get(position),position);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(Despensas.this);
                builder.setMessage("Você deseja excluir essa despensa? Você perderá todos os produtos salvos nela")
                        .setPositiveButton("Sim", dialogClickListener)
                        .setNegativeButton("Não", dialogClickListener).show();


                return true;
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        requestQueue = Volley.newRequestQueue(Despensas.this);
        listaDespensas = new ArrayList<>();

        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURL,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for(int i=0; i<response.length();i++) {
                                // Retrieves all JSON objects in response array
                                JSONObject colorObj = response.getJSONObject(i);
                                // Recebe os dados da despensa da posição i

                                Despensa despensa = new Despensa();
                                despensa.setVencendo(false);

                                int id = colorObj.getInt("id");
                                String nome = colorObj.getString("nome");
                                String localizacao = colorObj.getString("localizacao");
                                int cliente=colorObj.getInt("cliente");
                                // Recebe os produtos da despensa
                                JSONArray produtosDespensas = colorObj.getJSONArray("produtosDespensas");
                                listaProdutos = new ArrayList<>();
                                for (int j=0;j<produtosDespensas.length();j++) {
                                    JSONObject prod = (JSONObject)produtosDespensas.get(j);
//                                    pegarProduto(prod.getInt("produto"));
//                                    Produto p = ppp;


                                    prodDesp = new ProdutosDespensa();
                                    prodDesp.setProduto(new Produto(prod.getInt("produto")));
                                    prodDesp.setQuantidade(prod.getInt("quantidade"));
                                    prodDesp.setValidade(prod.getString("validade"));
                                    prodDesp.setId(prod.getInt("id"));
                                    SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
                                    Date data1 =(Date)form.parse(prodDesp.getValidade());
                                    Calendar val = Calendar.getInstance();
                                    val.setTime(data1);
                                    prodDesp.setVal(val);
                                    if(prodDesp.isVencendo()&&prodDesp.getQuantidade()>0)
                                        despensa.setVencendo(prodDesp.isVencendo());
                                    if (prod.getInt("quantidade")>0)
                                        listaProdutos.add(prodDesp);

                                }

                                //Mapeando os dados da despensa em um objeto do tipo despensa
                                despensa.setId(id);
                                despensa.setCliente(cliente);
                                despensa.setNome(nome);
                                despensa.setLocalizacao(localizacao);
                                despensa.setProdutoDespensa(listaProdutos);

                                listaDespensas.add(despensa);

                            }
                            adapter=new AdapterDespensa(listaDespensas, getApplicationContext());
                            listView.setAdapter(adapter);

                        }

                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {

                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Falha ao transformar data", Toast.LENGTH_SHORT).show();

                        } finally {

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

    void excluirDespensa(Despensa despensa, final int position) {
        String url = Constantes.url+"despensas/"+despensa.getId();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        listaDespensas.remove(position);
                        adapter=new AdapterDespensa(listaDespensas, getApplicationContext());
                        listView.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "Despensa removida com sucesso", Toast.LENGTH_SHORT).show();


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Toast.makeText(getApplicationContext(), "Falha ao remover despensa", Toast.LENGTH_SHORT).show();

                    }
                }
        );
        queue.add(dr);
    }
}
