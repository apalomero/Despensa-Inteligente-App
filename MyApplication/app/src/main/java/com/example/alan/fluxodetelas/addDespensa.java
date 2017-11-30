package com.example.alan.fluxodetelas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alan.fluxodetelas.model.Cliente;
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.Despensa;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class addDespensa extends AppCompatActivity {
    EditText nome, localizacao;
    String JsonURL = Constantes.url+"despensas";
    String stringRequest;
    Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_despensa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cliente = ClienteSingleton.getInstance().getCliente();//(Cliente) getIntent().getSerializableExtra("cliente");

        nome = (EditText) findViewById(R.id.nomeDespensa);
        localizacao = (EditText) findViewById(R.id.localDespensa);

        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome = (EditText) findViewById(R.id.nomeDespensa);
                localizacao = (EditText) findViewById(R.id.localDespensa);
                if (nome.length()==0) {
                    Toast.makeText(getApplicationContext(), "Insira um nome", Toast.LENGTH_SHORT).show();
                }
                else {
                    Despensa despensa = new Despensa(cliente.getId(),nome.getText().toString(), localizacao.getText().toString());

                    adicionarDespensa(despensa);
                }
            }
        });
    }

    public void adicionarDespensa(Despensa despensa) {
        JSONObject jsonDespensa;
        RequestQueue queue = Volley.newRequestQueue(this);



        try {
            jsonDespensa = new JSONObject();
            jsonDespensa.put("nome", despensa.getNome());
            jsonDespensa.put("localizacao", despensa.getLocalizacao());
            jsonDespensa.put("cliente",despensa.getCliente());

            stringRequest = jsonDespensa.toString();


            StringRequest request= new StringRequest(Request.Method.POST,JsonURL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Despensa criada com sucesso", Toast.LENGTH_SHORT).show();
                            voltar();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Falha ao criar despensa", Toast.LENGTH_SHORT).show();
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
    public void voltar() {
        finish();
    }
}
