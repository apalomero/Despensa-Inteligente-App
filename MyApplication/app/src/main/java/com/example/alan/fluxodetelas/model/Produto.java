package com.example.alan.fluxodetelas.model;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 03/09/2017.
 */

public class Produto implements Serializable{
    private String nome, marca, tipo;
    private double peso;
    private int id, categoria;


    public Produto(String nome, String marca, String tipo, int categoria, double peso, int id) {
        this.nome = nome;
        this.marca = marca;
        this.tipo = tipo;
        this.categoria = categoria;
        this.peso = peso;
        this.id = id;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getCategoria() {
        return categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Produto() {
    }

    public Produto(String nome, String marca, String tipo, double peso) {
        this.nome = nome;
        this.marca = marca;
        this.tipo = tipo;
        this.peso = peso;
    }

    public Produto(String nome, String marca, String tipo, double peso, int id) {
        this.nome = nome;
        this.marca = marca;
        this.tipo = tipo;
        this.peso = peso;
        this.id = id;
    }

    public Produto(int id) {
        this.id = id;
    }
}
