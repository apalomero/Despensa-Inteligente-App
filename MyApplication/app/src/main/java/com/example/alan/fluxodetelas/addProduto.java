package com.example.alan.fluxodetelas;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.alan.fluxodetelas.model.Categoria;
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.Produto;

import java.io.UnsupportedEncodingException;

import static com.example.alan.fluxodetelas.R.id.spinnerCategorias;
import static com.example.alan.fluxodetelas.R.layout.activity_add_produto;

public class addProduto extends AppCompatActivity {
    Spinner spinner;
    EditText txtNome;
    EditText txtMarca;
    EditText txtTipo;
    EditText txtPeso;
    String JsonURL = Constantes.url+"produtos";
    String stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_add_produto);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (Spinner) findViewById(spinnerCategorias);
        setarSpinner();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) view).setTextColor(Color.BLACK); //Change selected text color
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtNome = (EditText) findViewById(R.id.produtoNome);
                txtMarca = (EditText) findViewById(R.id.produtoMarca);
                txtTipo = (EditText) findViewById(R.id.produtoTipo);
                txtPeso = (EditText) findViewById(R.id.produtoPeso);

                if (txtNome.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Insira um nome", Toast.LENGTH_SHORT).show();
                } else if (spinner == null || spinner.getSelectedItem().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Selecione uma categoria", Toast.LENGTH_SHORT).show();
                } else {
                    Produto p = new Produto(txtNome.getText().toString(),
                            txtMarca.getText().toString(),
                            txtTipo.getText().toString(),
                            (txtPeso.length() != 0) ? Double.parseDouble(txtPeso.getText().toString()) : 0);
                    p.setCategoria(((Categoria)spinner.getSelectedItem()).getId());

                    addProduto(p);

                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void addProduto(Produto p) {

        //Tentando chamar webservice, deletar a partir daqui quando travar


        JSONObject jsonProduto;
        RequestQueue queue = Volley.newRequestQueue(this);
        try {

            jsonProduto = new JSONObject();
            jsonProduto.put("nome", p.getNome());
            jsonProduto.put("marca", p.getMarca());
            jsonProduto.put("tipo", p.getTipo());
            jsonProduto.put("peso", p.getPeso());
            jsonProduto.put("categoria",p.getCategoria());


            stringRequest = jsonProduto.toString();

            StringRequest request= new StringRequest(Request.Method.POST,JsonURL,
                    new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), "Produto adicionado com sucesso", Toast.LENGTH_SHORT).show();
                    voltar();
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
            queue.add(request);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Parar de deletar
    }

    public void voltar() {
            Intent it = new Intent(this, Produtos.class);
            startActivity(it);
            finish();
        }

    void setarSpinner() {
        ArrayAdapter<Categoria> adapter =
                new ArrayAdapter<>(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, CategoriasSingleton.getInstance().getCategorias());
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }



}






