package com.example.alan.fluxodetelas.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alan on 23/09/2017.
 */



public class Receita implements Serializable{
    String titulo, modoPreparo;
    double quantidade;
    String tempoExecucao;
    int cliente, id;
    List<Produto> ingredientes;
    List<ProdutosReceitas> produtos = new ArrayList<>();

    public List<ProdutosReceitas> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutosReceitas> produtos) {
        this.produtos = produtos;
    }

    public Receita() {
    }

    public Receita(String titulo, String modoPreparo, double quantidade, int cliente, int id) {
        this.titulo = titulo;
        this.modoPreparo = modoPreparo;
        this.quantidade = quantidade;
        this.cliente = cliente;
        this.id = id;
    }

    public Receita(String titulo, String modoPreparo, int cliente, int id) {
        this.titulo = titulo;
        this.modoPreparo = modoPreparo;
        this.cliente = cliente;
        this.id = id;
    }

    public Receita(String titulo, String modoPreparo, double quantidade, String tempoExecucao) {
        this.titulo = titulo;
        this.modoPreparo = modoPreparo;
        this.quantidade = quantidade;
        this.tempoExecucao = tempoExecucao;
    }

    public Receita(String titulo, String modoPreparo, double quantidade, String tempoExecucao, List<Produto> ingredientes) {
        this.titulo = titulo;
        this.modoPreparo = modoPreparo;
        this.quantidade = quantidade;
        this.tempoExecucao = tempoExecucao;
        this.ingredientes = ingredientes;
    }

    public int getCliente() {
        return cliente;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public void setIngredientes(List<Produto> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void addIngrediente (Produto produto) {
        ingredientes.add(produto);
    }

    public List<Produto> getIngredientes () {
        return ingredientes;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getModoPreparo() {
        return modoPreparo;
    }

    public void setModoPreparo(String modoPreparo) {
        this.modoPreparo = modoPreparo;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getTempoExecucao() {
        return tempoExecucao;
    }

    public void setTempoExecucao(String tempoExecucao) {
        this.tempoExecucao = tempoExecucao;
    }

    public void addProdutosReceitas (ProdutosReceitas pr) {
        produtos.add(pr);
    }
}
