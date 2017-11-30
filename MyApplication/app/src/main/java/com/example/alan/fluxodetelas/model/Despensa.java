package com.example.alan.fluxodetelas.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alan on 17/09/2017.
 */

public class Despensa implements Serializable{

    private int id, cliente;
    private String nome;
    private String localizacao;
    private List<ProdutosDespensa> produtoDespensa;
    private boolean vencendo;

    public boolean isVencendo() {
        return vencendo;
    }

    public void setVencendo(boolean vencendo) {
        this.vencendo = vencendo;
    }

    public Despensa(String nome, String localizacao) {
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public Despensa(int id, int cliente, String nome, String localizacao, List<ProdutosDespensa> produtoDespensa) {
        this.id = id;
        this.cliente = cliente;
        this.nome = nome;
        this.localizacao = localizacao;
        this.produtoDespensa = produtoDespensa;
    }

    public Despensa(int cliente, String nome, String localizacao) {
        this.cliente = cliente;
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public Despensa(int id, String nome, String localizacao, List<ProdutosDespensa> produtoDespensa) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.produtoDespensa = produtoDespensa;
    }

    public Despensa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public List<ProdutosDespensa> getProdutoDespensa() {
        return produtoDespensa;
    }

    public void setProdutoDespensa(List<ProdutosDespensa> produtoDespensa) {
        this.produtoDespensa = produtoDespensa;
    }
}
