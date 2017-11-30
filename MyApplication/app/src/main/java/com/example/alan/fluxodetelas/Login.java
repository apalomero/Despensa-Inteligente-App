package com.example.alan.fluxodetelas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.alan.fluxodetelas.model.Categoria;
import com.example.alan.fluxodetelas.model.Cliente;
import com.example.alan.fluxodetelas.model.Constantes;
import com.example.alan.fluxodetelas.model.ReceitasFavoritas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    EditText usuario, senha;
    Button btLogin;
    List<Cliente> listaClientes;
    RequestQueue requestQueue;
    TextView.OnEditorActionListener exampleListener = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_NULL
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                btLogin.callOnClick();
            }
            return true;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btLogin = (Button)findViewById(R.id.login);
        listaClientes=new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        usuario = (EditText) findViewById(R.id.email);
        senha = (EditText) findViewById(R.id.senha);
        senha.setOnEditorActionListener(exampleListener);

        pegarCategorias();

    }
    @Override
    protected void onStart() {
        super.onStart();

    }

    public void logar (View view) {
        String login = usuario.getText().toString();
        String pass = senha.getText().toString();
        String JsonURL = Constantes.url+"clientes/autenticar?login="+login+"&senha="+pass;

        JsonObjectRequest arrayreq = new JsonObjectRequest(JsonURL,null,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Retrieves JSON object in response
                            JSONObject colorObj = response;
                            // Recebe os dados do produto da posição i
                            int id = colorObj.getInt("id");
                            String nome = colorObj.getString("nome");
                            String login = colorObj.getString("login");
                            JSONArray recFav = colorObj.getJSONArray("receitasFavoritas");
                            List<ReceitasFavoritas> favoritas = new ArrayList<>();
                            for (int j=0;j<recFav.length();j++) {
                                JSONObject objFav = (JSONObject) recFav.get(j);
                                ReceitasFavoritas fav = new ReceitasFavoritas();
                                fav.setReceita(objFav.getInt("receita"));
                                fav.setCliente(objFav.getInt("cliente"));
                                fav.setId(objFav.getInt("id"));
                                favoritas.add(fav);
                            }

                            Cliente cliente = new Cliente(nome,login,null,id);
                            cliente.setReceitasFavoritas(favoritas);

                            Intent intent = new Intent(Login.this, PaginaInicial.class);
                            ClienteSingleton.getInstance().setCliente(cliente);
                            pegarCategorias();
                            startActivity(intent);
                            finish();


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
                        if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Erro: falha ao se conectar com o servidor", Toast.LENGTH_SHORT).show();
                        } else {
                            TextView erro = (TextView) findViewById(R.id.senhaErrada);
                            erro.setVisibility(View.VISIBLE);
                            Log.e("Volley", "Error");
                        }
                    }
                }
        );
        // Adds the JSON array request "arrayreq" to the request queue
        requestQueue.add(arrayreq);





    }

    public void abrirTelaCadastro (View view) {
        Intent it = new Intent(this,CadastraCliente.class);
        startActivity(it);
    }

    void pegarCategorias () {
        String url = Constantes.url+"categorias";
        JsonArrayRequest arrayreq = new JsonArrayRequest(url,
                // The second parameter Listener overrides the method onResponse() and passes
                //JSONArray as a parameter
                new Response.Listener<JSONArray>() {

                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<Categoria> categorias = new ArrayList<>();
                            for(int i=0; i<response.length();i++) {
                                // Retrieves all JSON objects in response array
                                JSONObject colorObj = response.getJSONObject(i);
                                // Recebe os dados do produto da posição i
                                int id = colorObj.getInt("id");
                                String nome = colorObj.getString("nome");

                                Categoria categoria = new Categoria(nome,id);

                                categorias.add(categoria);
                            }
                            CategoriasSingleton.getInstance().setCategorias(categorias);
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
}
