package com.example.alan.fluxodetelas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
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

public class addReceita extends AppCompatActivity {
    EditText nome, modoPreparo, porcoes, tempoPreparo, busca;
    private Context context = this;
    ListView listView, listViewDialog;
    List<Produto> listaProdutosDialog;
    List<ProdutosReceitas> produtosReceitas;
    Cliente cliente;
    Receita receita = new Receita();
    Receita receitaOutraTela;
    Intent it = new Intent();
    boolean nova=true;

    RequestQueue requestQueueDialog;
    String stringRequest;


    private AdapterProduto adapterDialog;
    private AdapterProdutosReceitas adapterProdutosReceitas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receita);
        produtosReceitas = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                criarReceita();
            }
        });

        adapterProdutosReceitas = new AdapterProdutosReceitas(produtosReceitas, getApplicationContext());
        nome = (EditText) findViewById(R.id.nomeReceita);
        modoPreparo = (EditText) findViewById(R.id.modoPreparo);
        tempoPreparo = (EditText) findViewById(R.id.tempoPreparo);
        porcoes = (EditText) findViewById(R.id.porcoes);
        busca = (EditText) findViewById(R.id.buscaProduto);
        listView = (ListView) findViewById(R.id.listaIngredientes);
        listView.setAdapter(adapterProdutosReceitas);
        cliente = ClienteSingleton.getInstance().getCliente();//(Cliente) getIntent().getSerializableExtra("cliente");

        receitaOutraTela = (Receita) getIntent().getSerializableExtra("receita");
        if (receitaOutraTela!=null) {
            nova=false;
            nome.setText(receitaOutraTela.getTitulo());
            modoPreparo.setText(receitaOutraTela.getModoPreparo());
            tempoPreparo.setText(receitaOutraTela.getTempoExecucao());
            porcoes.setText(Double.toString(receitaOutraTela.getQuantidade()));
            produtosReceitas=receitaOutraTela.getProdutos();
            adapterProdutosReceitas = new AdapterProdutosReceitas(produtosReceitas, getApplicationContext());
            listView.setAdapter(adapterProdutosReceitas);

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProdutosReceitas pr = produtosReceitas.get(position);
                setarQtd(pr);

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
                                excluirProduto(produtosReceitas.get(position),position);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(addReceita.this);
                builder.setMessage("Você deseja excluir ste produto desta receita?")
                        .setPositiveButton("Sim", dialogClickListener)
                        .setNegativeButton("Não", dialogClickListener).show();


                return true;
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    void excluirProduto(ProdutosReceitas prod,int position) {

    }

    void setarQtd(final ProdutosReceitas pr) {

        final Dialog openDialog = new Dialog(context);
        openDialog.setContentView(R.layout.dialog_box_edit_produtos_receitas);
        openDialog.setTitle("Digite a quantidade");
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.btCancel);
        Button dialogOk = (Button)openDialog.findViewById(R.id.btOk);
        final EditText qtd = (EditText)openDialog.findViewById(R.id.qtd);
        qtd.setText(Double.toString(pr.getQuantidade()));

        dialogCloseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                openDialog.dismiss();
            }
        });

        dialogOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                double d = Double.parseDouble(qtd.getText().toString());
                pr.setQuantidade(d);
                listView.setAdapter(adapterProdutosReceitas);
                openDialog.dismiss();
            }
        });




        openDialog.show();
    }

    void criarReceita() {
        String JsonURL = Constantes.url+"receitas";
        JSONObject jsonReceita;
        RequestQueue queue = Volley.newRequestQueue(this);

        String title = nome.getText().toString();
        String modoPrep = (modoPreparo.length()>0) ? modoPreparo.getText().toString() : null;
        Double qtd = (porcoes.length()>0)? Double.parseDouble(porcoes.getText().toString()):0;
        receita.setQuantidade(qtd);
        receita.setModoPreparo(modoPrep);
        receita.setTitulo(title);
        receita.setCliente(cliente.getId());
        receita.setTempoExecucao(tempoPreparo.getText().toString());
        if (nome.length()==0) {
            Toast.makeText(getApplicationContext(), "Digite um título para a receita", Toast.LENGTH_SHORT).show();

        }
        else{
            try {
                jsonReceita = new JSONObject();
                jsonReceita.put("titulo", receita.getTitulo());
                jsonReceita.put("modoPreparo", receita.getModoPreparo());
                jsonReceita.put("quantidade", receita.getQuantidade());
                jsonReceita.put("cliente", receita.getCliente());
                jsonReceita.put("tempoExecucao",receita.getTempoExecucao());
                if (!nova)
                    jsonReceita.put("id",receitaOutraTela.getId());

                stringRequest = jsonReceita.toString();
                int op;
                op=Request.Method.PUT;
                if(nova)
                    op= Request.Method.POST;
                StringRequest request = new StringRequest(op, JsonURL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String msg = "Receita atualizada";
                                if (nova)
                                    msg="Receita criada com sucesso";
                                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                                it.putExtra("produtosReceitas",(ArrayList)produtosReceitas);
                                setResult(Activity.RESULT_OK, it);
                                if(!nova)
                                    addProdutosReceitas();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Falha ao criar receita", Toast.LENGTH_SHORT).show();
                            }
                        }) {
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

    }
    void addProdutosReceitas() {
        int id=receitaOutraTela.getId();
        for(int i=0;i<produtosReceitas.size();i++) {
            produtosReceitas.get(i).setReceita(id);
        }
        //fazer request pra cada um

        for (int i=0;i<produtosReceitas.size();i++) {
            ProdutosReceitas pr = produtosReceitas.get(i);
            JSONObject jsonProdutosReceita;
            RequestQueue queue = Volley.newRequestQueue(addReceita.this);
            try {

                jsonProdutosReceita = new JSONObject();
                jsonProdutosReceita.put("quantidade", pr.getQuantidade());
                jsonProdutosReceita.put("produto", pr.getProduto().getId());
                jsonProdutosReceita.put("receita",pr.getReceita());
                int op=Request.Method.POST;

                if (pr.getId()!=0) {
                    jsonProdutosReceita.put("id", pr.getId());
                    op=Request.Method.PUT;

                }

                final String stringRequest = jsonProdutosReceita.toString();

                StringRequest request= new StringRequest(op, Constantes.url+"produtosReceitas",
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


                queue.add(request);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void buscarProdutos(View view) { //Pega a lista de produtos para mostrar na caixa de diálogo

        String JsonURLProdutos  = Constantes.url+"produtos?nome="+busca.getText();

        final Dialog openDialog = new Dialog(context);
        openDialog.setContentView(R.layout.dialog_box_produtos);
        openDialog.setTitle("Busca de produto");
        listViewDialog = (ListView) openDialog.findViewById(R.id.listProdutosDialog);
        Button dialogCloseButton = (Button)openDialog.findViewById(R.id.dialogCancel);

        listaProdutosDialog = new ArrayList<>();

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
                ProdutosReceitas pr = new ProdutosReceitas();
                pr.setProduto(produto);
                if(!nova)
                    pr.setReceita(receita.getId());
                produtosReceitas.add(pr);
                pr.setQuantidade(1);

                listView.setAdapter(adapterProdutosReceitas);

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

}
