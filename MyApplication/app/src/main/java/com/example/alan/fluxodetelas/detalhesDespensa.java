package com.example.alan.fluxodetelas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.Despensa;
import com.example.alan.fluxodetelas.model.Produto;
import com.example.alan.fluxodetelas.model.ProdutosDespensa;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class detalhesDespensa extends AppCompatActivity {
    Despensa d;
    TextView titulo, dialogText;
    EditText nome, local, buscaProduto;
    Button dialogClose;
    int tamanhoInicial;
//    Produto ppp = new Produto();

    RequestQueue requestQueue, requestQueueDialog;
    String JsonURLProdutos;
    String JsonURLDespensa  = Constantes.url+"despensas";
    String JsonURLProdutosDespensas = Constantes.url+"produtosDespensas";
    String stringRequest, stringRequest2;

    List<Produto> listaProdutosDialog;
    List<ProdutosDespensa> produtosDespensas;
//    ArrayList<Produto> todosOsProdutos = new ArrayList<>();
//    int i;
    ListView listView, listViewDialog;
    private static AdapterProduto adapterDialog, adapter;
    private static AdapterProdutosDespensa adapterProdutosDespensa;

    Intent it;

    private Context context = this;


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_despensa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buscaProduto = (EditText) findViewById(R.id.buscaProduto);
        nome = (EditText) findViewById(R.id.txNomeDespensa);
        local = (EditText) findViewById(R.id.txLocalizacaoDespensa);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        listView = (ListView) findViewById(R.id.listaProdutosV);

        d=(Despensa) getIntent().getSerializableExtra("despensa");
        produtosDespensas=new ArrayList<>();
        produtosDespensas=d.getProdutoDespensa();
        tamanhoInicial = produtosDespensas.size();


        adapterProdutosDespensa = new AdapterProdutosDespensa(produtosDespensas, getApplicationContext());
        listView.setAdapter(adapterProdutosDespensa);


        //preencher o produtosDespensas
        if (produtosDespensas.size()>0)
            pegarProdutos(0);


        nome.setText(d.getNome());
        local.setText(d.getLocalizacao());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabOnClick();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProdutosDespensa pr = produtosDespensas.get(position);

                setarQtd(pr);


            }
        });


    }

    void setarQtd(final ProdutosDespensa pd) {


        final Dialog openDialog = new Dialog(context);
        openDialog.setContentView(R.layout.dialog_box_edit_produtos_despensas);
        openDialog.setTitle("Digite a quantidade e a validade");
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.btCancel);
        Button dialogOk = (Button)openDialog.findViewById(R.id.btOk);
        final EditText qtd = (EditText)openDialog.findViewById(R.id.qtd);
        final EditText validade = (EditText)openDialog.findViewById(R.id.val);
        new DateInputMask(validade);
        qtd.setText(Integer.toString(pd.getQuantidade()));
        validade.setText(pd.getValidade());

        dialogCloseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });

        dialogOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int d=0;
                if (qtd.getText().length()!=0){
                   d = Integer.parseInt(qtd.getText().toString());
                }
                pd.setQuantidade(d);
                Calendar cal = Calendar.getInstance();
                String vali= validade.getText().toString();
                pd.setValidade(vali);
                listView.setAdapter(adapterProdutosDespensa);
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }

    void pegarProdutos(final int posicao) {
        //pegar o id do produto da posicao indicada
        RequestQueue req = Volley.newRequestQueue(this);
        int id = produtosDespensas.get(posicao).getProduto().getId();
        String objURL = Constantes.url+"produtos/"+id;
        JsonObjectRequest objreq = new JsonObjectRequest(Request.Method.GET, objURL,null,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONObject>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject colorObj = response;
                            // Recebe os dados do produto
                            int id = colorObj.getInt("id");
                            String nome = colorObj.getString("nome");
                            String marca = colorObj.getString("marca");
                            String tipo = colorObj.getString("tipo");
                            double peso = colorObj.getDouble("peso");
                            int categoria = colorObj.getInt("categoria");

                            //Mapeando os dados do produto em um objeto do tipo Produto
                            Produto prod=new Produto();
                            produtosDespensas.get(posicao).getProduto().setMarca(marca);
                            produtosDespensas.get(posicao).getProduto().setPeso(peso);
                            produtosDespensas.get(posicao).getProduto().setTipo(tipo);
                            produtosDespensas.get(posicao).getProduto().setCategoria(categoria);
                            produtosDespensas.get(posicao).getProduto().setNome(nome);
                            produtosDespensas.get(posicao).getProduto().setId(id);

                            int pos= 1+posicao;
                            if (pos<produtosDespensas.size()) {
                                pegarProdutos(pos);
                            }
                            else {
                                adapterProdutosDespensa = new AdapterProdutosDespensa(produtosDespensas, getApplicationContext());
                                listView.setAdapter(adapterProdutosDespensa);
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
        //buscar o produto
        //botar no produtosDespensa[posicao]
        //posicao++
        //se nao acabou a lista {
            //chama pegarProdutos(posicao)
    }

    public void buscarProdutos(View view) { //Pega a lista de produtos para mostrar na caixa de diálogo

        JsonURLProdutos  = Constantes.url+"produtos?nome="+buscaProduto.getText();

        final Dialog openDialog = new Dialog(context);
        openDialog.setContentView(R.layout.dialog_box_produtos);
        openDialog.setTitle("Busca de produto");
        listViewDialog = (ListView) openDialog.findViewById(R.id.listProdutosDialog);
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialogCancel);

        listaProdutosDialog = new ArrayList<Produto>();

        requestQueueDialog = Volley.newRequestQueue(this);
        JsonArrayRequest arrayreq = new JsonArrayRequest(JsonURLProdutos,
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

                                //Mapeando os dados do produto em um objeto do tipo Produto
                                Produto produto = new Produto(nome, marca, tipo, 1,peso, id);
                                listaProdutosDialog.add(produto);

                            }
                            adapterDialog = new AdapterProduto(listaProdutosDialog, getApplicationContext());
                            listViewDialog.setAdapter(adapterDialog);
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
        requestQueueDialog.add(arrayreq);

        listViewDialog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Produto produto = listaProdutosDialog.get(position);
                ProdutosDespensa pd = new ProdutosDespensa();
                pd.setProduto(produto);
                pd.setValidade("2020-01-01");
                pd.setQuantidade(((Double)pd.getProduto().getPeso()).intValue());
                produtosDespensas.add(pd);

                listView.setAdapter(adapterProdutosDespensa);
                openDialog.dismiss();


            }
        });


        dialogCloseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });
        openDialog.show();
    }

    void fabOnClick() {
        //Salvar alterações
        if(nome.length()==0) {
            Toast.makeText(getApplicationContext(), "Insira um nome", Toast.LENGTH_SHORT).show();
        }
        else {
            if (!(nome.getText().toString().equals(d.getNome())) || !(local.getText().toString().equals(d.getLocalizacao()))) {
                d.setNome(nome.getText().toString());
                d.setLocalizacao(local.getText().toString());
                updateDespensa(d);
            }

            int operacao;
            if (tamanhoInicial>0) {
                //operação é PUT
                operacao=Request.Method.PUT;
            }
            else {
                //operação é POST
                operacao=Request.Method.POST;
            }
            if(produtosDespensas.size()>0)
                updateProdutosDespensas(operacao, produtosDespensas,0);

        }
    }

    void updateDespensa(Despensa despensa) {
        JSONObject jsonDespensa;
        RequestQueue queue = Volley.newRequestQueue(this);
        String JsonURL = Constantes.url+"despensas";

        try {
            jsonDespensa = new JSONObject();
            jsonDespensa.put("id",despensa.getId());
            jsonDespensa.put("nome", despensa.getNome());
            jsonDespensa.put("localizacao", despensa.getLocalizacao());
            jsonDespensa.put("cliente",despensa.getCliente());


            final String stringRequest = jsonDespensa.toString();


            StringRequest request= new StringRequest(Request.Method.PUT,JsonURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Falha ao atualizar despensa", Toast.LENGTH_SHORT).show();

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


        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void updateProdutosDespensas(final int op, final List<ProdutosDespensa> pd, final int posicao) {

        JSONObject jsonProdutosDespensa;
        RequestQueue queue = Volley.newRequestQueue(this);


        try {

            jsonProdutosDespensa = new JSONObject();
            jsonProdutosDespensa.put("produto", pd.get(posicao).getProduto().getId());
            jsonProdutosDespensa.put("despensa", d.getId());
            jsonProdutosDespensa.put("validade", pd.get(posicao).getValidade());
            jsonProdutosDespensa.put("quantidade", pd.get(posicao).getQuantidade());
            if (op==Request.Method.PUT)
                jsonProdutosDespensa.put("id", pd.get(posicao).getId());

            final String stringRequest = jsonProdutosDespensa.toString();

            StringRequest request= new StringRequest(op,JsonURLProdutosDespensas,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (op==Request.Method.POST)
                                tamanhoInicial++;
                            int pos = posicao+1;
                            int oper;
                            if(pos<tamanhoInicial) {
                                oper = Request.Method.PUT;
                            }
                            else {
                                oper=Request.Method.POST;
                            }
                            if (pos<pd.size()) {

                                updateProdutosDespensas(oper,pd,pos);
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Atualizado com sucesso", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Falha ao adicionar produto", Toast.LENGTH_SHORT).show();
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


        }catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void sugerirReceitas(View view) {
        ArrayList<Produto> produtosVencendo = new ArrayList<>();
        for (int i=0;i<produtosDespensas.size();i++) {
            if (produtosDespensas.get(i).isVencendo())
                    produtosVencendo.add(produtosDespensas.get(i).getProduto());
        }

        if (produtosVencendo.size()==0)
            Toast.makeText(getApplicationContext(), "Nenhum produto está próximo da data de validade", Toast.LENGTH_LONG).show();
        else {
            Intent it = new Intent(this,Receitas.class);
            it.putExtra("produtosVencendo",produtosVencendo);

            startActivity(it);
        }
        //mander eles pro Receitas
    }

}
