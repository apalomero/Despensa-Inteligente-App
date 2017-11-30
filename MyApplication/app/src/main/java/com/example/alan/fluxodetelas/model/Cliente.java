package com.example.alan.fluxodetelas.model;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alan on 17/09/2017.
 */

public class Cliente implements Serializable{
    private String nome, login, senha;
    List<ReceitasFavoritas> receitasFavoritas;
    private int id;

    public Cliente(String nome, String login, String senha, int id) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.id = id;
    }

    public Cliente(String nome, String login, String senha, List<ReceitasFavoritas> receitasFavoritas, int id) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.receitasFavoritas = receitasFavoritas;
        this.id = id;
    }

    public List<ReceitasFavoritas> getReceitasFavoritas() {
        return receitasFavoritas;
    }

    public void setReceitasFavoritas(List<ReceitasFavoritas> receitasFavoritas) {
        this.receitasFavoritas = receitasFavoritas;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void addFavorito(ReceitasFavoritas fav) {
        this.receitasFavoritas.add(fav);
    }
}
