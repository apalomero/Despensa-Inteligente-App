package com.example.alan.fluxodetelas;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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
import com.example.alan.fluxodetelas.model.Despensa;
import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.ProdutosDespensa;
import com.example.alan.fluxodetelas.model.ProdutosReceitas;
import com.example.alan.fluxodetelas.model.Receita;
import com.example.alan.fluxodetelas.model.ReceitasFavoritas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;

public class DetalhesReceita extends AppCompatActivity {
    TextView titulo, modoPreparo, servePorcoes, tempoPreparo;
    ListView listView;
    ImageView favorite;
    private  static AdapterProdutoDetalhesReceita adapter;
    Receita receita;
    Intent it;
    List<ProdutosReceitas> produtosReceitas;
    List<ReceitasFavoritas> rf;
    List<Despensa> despensas;
    List<String> nomesDespensas;
//    int tamanhoInicial,i;
    boolean isFavorito;
    Cliente cliente;
//    Produto ppp = new Produto();
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_receita);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        titulo = (TextView) findViewById(R.id.nomeReceita);
        servePorcoes = (TextView) findViewById(R.id.servePorcoes);
        tempoPreparo = (TextView) findViewById(R.id.tempoDePreparo);
        modoPreparo = (TextView) findViewById(R.id.modoDePreparo);
        listView = (ListView) findViewById(R.id.listaIngredientes);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        favorite = (ImageView) findViewById(R.id.starFav);
        spinner = (Spinner) findViewById(R.id.spinnerDespensas);

        it = getIntent();
        receita = (Receita)it.getSerializableExtra("receita");
        cliente = ClienteSingleton.getInstance().getCliente();//(Cliente) it.getSerializableExtra("cliente");
        produtosReceitas=new ArrayList<>();
        produtosReceitas=receita.getProdutos();
        despensas=new ArrayList<>();
        nomesDespensas = new ArrayList<>();

        titulo.setText(receita.getTitulo());
        servePorcoes.setText("Serve "+receita.getQuantidade()+" porções");
        tempoPreparo.setText("Tempo de Preparo: "+receita.getTempoExecucao());
        if (receita.getTempoExecucao()==null)
            tempoPreparo.setText("Tempo de Preparo: ");
        modoPreparo.setText(receita.getModoPreparo());
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abreAddReceita();
            }
        });
        if (produtosReceitas.size()>0)
          pegarProdutos(produtosReceitas,0);
        pegarDespensas();
        if (cliente.getId()!=receita.getCliente()) {
            fab.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        rf = ClienteSingleton.getInstance().getCliente().getReceitasFavoritas();
        isFavorito=false;
        for (int i=0;i<ClienteSingleton.getInstance().getCliente().getReceitasFavoritas().size();i++) {
            if(receita.getId()==ClienteSingleton.getInstance().getCliente().getReceitasFavoritas().get(i).getReceita())
                isFavorito = true;
        }
        if (isFavorito) {
            favorite.setImageResource(btn_star_big_on);
        }
        else {
            favorite.setImageResource(btn_star_big_off);
        }
    }

    void pegarProdutos(final List<ProdutosReceitas> pr, final int position) {
        RequestQueue req = Volley.newRequestQueue(this);
        final Produto p=new Produto();
        String objURL = Constantes.url+"produtos/"+pr.get(position).getProduto().getId();

        JsonObjectRequest objreq = new JsonObjectRequest(Request.Method.GET, objURL,null,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject colorObj = response;
                            // Recebe os dados do produto da posição i
                            int id = colorObj.getInt("id");
                            String nome = colorObj.getString("nome");
                            String marca = colorObj.getString("marca");
                            String tipo = colorObj.getString("tipo");
                            double peso = colorObj.getDouble("peso");
                            int categoria = colorObj.getInt("categoria");

                            //Mapeando os dados do produto em um objeto do tipo Produto
                            p.setMarca(marca);
                            p.setPeso(peso);
                            p.setTipo(tipo);
                            p.setCategoria(categoria);
                            p.setNome(nome);
                            p.setId(id);

                            pr.get(position).setProduto(p);

                            int pos= 1+position;
                            if (pos<pr.size()) {
                                pegarProdutos(pr,pos);
                            }
                            else {
                                adapter = new AdapterProdutoDetalhesReceita(produtosReceitas, getApplicationContext());
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
                        Toast.makeText(getApplicationContext(), "Erro ao preencher lista de produtos", Toast.LENGTH_SHORT).show();
                        Log.e("Volley", "Error");
                    }
                }
        );
        req.add(objreq);

    }

    void pegarDespensas() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String JsonURL = Constantes.url+"despensas/cliente/"+cliente.getId();
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

                                int id = colorObj.getInt("id");
                                String nome = colorObj.getString("nome");
                                // Recebe os produtos da despensa

                                //Mapeando os dados da despensa em um objeto do tipo despensa
                                Despensa despens = new Despensa();
                                despens.setId(id);
                                despens.setNome(nome);
                                despensas.add(despens);
                                nomesDespensas.add(nome);
                            }
                            ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(
                                    DetalhesReceita.this, android.R.layout.simple_spinner_item, nomesDespensas);

                            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinAdapter);
                        }

                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {

                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                        finally {

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

    void fazerReceita(View view) {
        if (spinner.getSelectedItem()!=null) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            for (int i = 0; i < produtosReceitas.size(); i++) {
                                tirarProdutoDaDepensa(despensas.get(spinner.getSelectedItemPosition()), produtosReceitas.get(i));

                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(DetalhesReceita.this);
            builder.setMessage("Essa operação irá descontar os ingredientes da receita da despensa selecionada. Os produtos serão descontados mesmo que a quantidade existente na despensa for menor que o necessário para realizar a receita.")
                    .setPositiveButton("OK", dialogClickListener)
                    .setNegativeButton("Cancelar", dialogClickListener).show();

        }
        else {
            Toast.makeText(getApplicationContext(), "Nenhuma despensa selecionada", Toast.LENGTH_SHORT).show();

        }
    }

    void tirarProdutoDaDepensa(Despensa desp, ProdutosReceitas pr) {
        String url = Constantes.url+"fazer-receita/descontar?" +
                "despensa="+desp.getId()+"&"+
                "produto="+pr.getProduto().getId()+"&"+
                "quantidade="+pr.getQuantidade();
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(getApplicationContext(), "Produtos descontados da despensa", Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Erro ao descontar produtos da despensa", Toast.LENGTH_SHORT).show();

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    void abreAddReceita() {
        if (cliente.getId()==receita.getCliente()) {
            Intent it = new Intent(this, addReceita.class);
            //it.putExtra("cliente", cliente);
            it.putExtra("receita", receita);
            startActivityForResult(it,1);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), "Você não é o autor dessa receita", Toast.LENGTH_SHORT).show();

        }

    }

    public void favoritar(View view) {
        favorite.setClickable(false);

        //se não estiver favoritado
        if (!isFavorito)
            //adicionar nos favoritos
            addFavorito(receita,cliente);
        //se estiver nos favoritos
        else {
            //remover dos favoritos
            removeFavorito(receita);
        }
    }

    void addFavorito(final Receita rec, final Cliente cli) {
        String URLFavoritas=Constantes.url+"favoritas";
        final String stringRequest;


        JSONObject jsonFavorito;
        RequestQueue queue = Volley.newRequestQueue(this);
        try {

            jsonFavorito = new JSONObject();
            jsonFavorito.put("cliente", cli.getId());
            jsonFavorito.put("receita", rec.getId());

            stringRequest = jsonFavorito.toString();

            StringRequest request= new StringRequest(Request.Method.POST,URLFavoritas,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Adicionado as favoritas", Toast.LENGTH_SHORT).show();
                            ClienteSingleton.getInstance().getCliente().addFavorito(new ReceitasFavoritas(cli.getId(),rec.getId()));
                            favorite.setImageResource(btn_star_big_on);
                            isFavorito=true;
                            favorite.setClickable(true);

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Erro ao adicionar favorito", Toast.LENGTH_SHORT).show();
                            favorite.setClickable(true);
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
            queue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void removeFavorito(final Receita rec) {
        String url = Constantes.url+"favoritas?"+
                "cliente="+cliente.getId()+
                "&receita="+receita.getId();

        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest dr = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        favorite.setImageResource(btn_star_big_off);
                        for(int i=0;i<ClienteSingleton.getInstance().getCliente().getReceitasFavoritas().size();i++) {
                            if (ClienteSingleton.getInstance().getCliente().getReceitasFavoritas().get(i).getReceita()==rec.getId()) {
                                ClienteSingleton.getInstance().getCliente().getReceitasFavoritas().remove(i);
                                i+=ClienteSingleton.getInstance().getCliente().getReceitasFavoritas().size();
                            }
                        }
                        isFavorito=false;
                        favorite.setClickable(true);

                        List<ReceitasFavoritas> fav = ClienteSingleton.getInstance().getCliente().getReceitasFavoritas();
                        for(int i=0;i<fav.size();i++) {
                            if (fav.get(i).getCliente()==cliente.getId() && fav.get(i).getReceita()==receita.getId()) {
                                ClienteSingleton.getInstance().getCliente().getReceitasFavoritas().remove(i);
                            }
                        }



                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        Toast.makeText(getApplicationContext(), "Falha ao remover favorito", Toast.LENGTH_SHORT).show();
                        favorite.setClickable(true);

                    }
                }
        );
        queue.add(dr);

    }

}
