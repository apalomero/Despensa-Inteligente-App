package com.example.alan.fluxodetelas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.view.KeyEvent;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.alan.fluxodetelas.model.Categoria;
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.Produto;

import java.util.ArrayList;
import java.util.List;


public class Produtos extends AppCompatActivity {

    Spinner spinner;
    List<Produto> listaProdutos;
    ListView listView;
    TextView search;
    private static AdapterProduto adapter;
    RequestQueue requestQueue;
    String JsonURL = Constantes.url+"produtos";
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
        setContentView(R.layout.activity_produtos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        spinner = (Spinner) findViewById(R.id.spinner);
//        setarSpinner();
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    ((TextView) view).setTextColor(Color.BLACK); //Change selected text color
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//
//                }
//            });

        listaProdutos = new ArrayList<Produto>();
        search = (TextView) findViewById(R.id.search);
        search.setOnEditorActionListener(exampleListener);

        //se vier do add produto

        listView = (ListView) findViewById(R.id.listaProdutos);
        // listaProdutos = new ArrayList<>();
        Intent it =getIntent();
        if (it !=null) {
            Produto p = (Produto)it.getSerializableExtra("produto");
            if (it.getStringExtra("busca")!=null)
                JsonURL+="?nome="+it.getStringExtra("busca");

            if(p!=null) {
                addProduto(p);
            }
        }
//tentando chamar o webservice, deletar a partir daqui quando travar

        requestQueue = Volley.newRequestQueue(this);

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
                                // Recebe os dados do produto da posição i
                                int id = colorObj.getInt("id");
                                String nome = colorObj.getString("nome");
                                String marca = colorObj.getString("marca");
                                String tipo = colorObj.getString("tipo");
                                double peso = colorObj.getDouble("peso");
                                int categoria = colorObj.getInt("categoria");

                                //Mapeando os dados do produto em um objeto do tipo Produto
                                Produto produto = new Produto(nome, marca, tipo, categoria, peso, id);

                                listaProdutos.add(produto);
                            }
                            adapter = new AdapterProduto(listaProdutos, getApplicationContext());
                            listView.setAdapter(adapter);
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

//parar de deletar aqui

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreAddProdutos();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void addProduto(Produto produto) {
        listaProdutos.add(produto);
    }

    public void abreAddProdutos () {
        Intent intent = new Intent(this,addProduto.class);
        startActivity(intent);
    }

    void fazerBusca(String busca) {
        Intent it = new Intent(this,Produtos.class);
        it.putExtra("busca",busca);
        startActivity(it);
        finish();
    }

    public void fazerBuscaIco(View view) {
        fazerBusca(search.getText().toString());
    }
}


