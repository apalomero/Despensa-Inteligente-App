package com.example.alan.fluxodetelas.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alan on 17/09/2017.
 */

public class DespensaCopiaSeguranca implements Serializable{

    private int id;
    private String nome;
    private String localizacao;
    private List<Produto> produtos;
    private List<Integer> quantidades;
    private List<Integer> idProdutosQuantidades;


    public void addQuantidade (int q) {
        quantidades.add(q);
    }

    public void addIdProdutosQuantidadese (int id) {
        quantidades.add(id);
    }

    public DespensaCopiaSeguranca(int id, String nome, String localizacao, List<Produto> produtos) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.produtos = produtos;
    }

    public DespensaCopiaSeguranca(int id, String nome, String localizacao, List<Produto> produtos, List<Integer> quantidades, List<Integer> ids) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.produtos = produtos;
        this.quantidades = quantidades;
        this.idProdutosQuantidades=ids;

    }

    public DespensaCopiaSeguranca(String nome, String localizacao) {
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public DespensaCopiaSeguranca(int id, String nome, String localizacao) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
    }

    public DespensaCopiaSeguranca(String nome, String localizacao, List<Produto> produtos) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.produtos = produtos;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getIdProdutosQuantidades() {
        return idProdutosQuantidades;
    }

    public void setIdProdutosQuantidades(List<Integer> idProdutosQuantidades) {
        this.idProdutosQuantidades = idProdutosQuantidades;
    }

    public List<Integer> getQuantidades() {
        return quantidades;
    }

    public void setQuantidades(List<Integer> quantidades) {
        this.quantidades = quantidades;
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
    public String getLocalizacao() {
        return localizacao;
    }
    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
    public List<Produto> getProdutos() {
        return produtos;
    }
    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }
}
