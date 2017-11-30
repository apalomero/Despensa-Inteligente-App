package com.example.alan.fluxodetelas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.alan.fluxodetelas.model.Constantes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CadastraCliente extends AppCompatActivity {
    EditText login, senha, nome;
    Button btn;
    RequestQueue requestQueue;
    String JsonURL = Constantes.url+"clientes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastra_cliente);

        login = (EditText) findViewById(R.id.login);
        senha = (EditText) findViewById(R.id.senha);
        nome = (EditText) findViewById(R.id.nomeCliente);
    }

    public void cadastrarCliente(View view) {

        if (login.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Preencha o campo Login", Toast.LENGTH_SHORT).show();
        }
        else if (senha.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Escolha uma senha", Toast.LENGTH_SHORT).show();
        }
        else if (nome.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Digite seu nome", Toast.LENGTH_SHORT).show();
        }
        else {
            //Chamando webservice

            JSONObject jsonCliente = new JSONObject();
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                jsonCliente.put("id", 4);
                jsonCliente.put("nome", nome.getText().toString());
                jsonCliente.put("login", login.getText().toString());
                jsonCliente.put("senha", senha.getText().toString());

                final String requestBody = jsonCliente.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, JsonURL, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Cliente cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Falha ao cadastrar cliente", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return String.format("application/json; charset=utf-8");
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                                    requestBody, "utf-8");
                            return null;
                        }
                    }
                };
                queue.add(stringRequest);
            } catch (Exception e) {}
//            catch (JSONException e) {
//               e.printStackTrace();
//       }
        }
        //parando de chamar webservice



    }
}
