package com.example.alan.fluxodetelas.model;

import java.io.Serializable;

/**
 * Created by Alan on 07/11/2017.
 */

public class ProdutosReceitas implements Serializable{
    Produto produto;
    double quantidade;
    int id,receita;

    public ProdutosReceitas() {
    }

    public ProdutosReceitas(Produto produto, double quantidade, int id) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.id = id;
    }

    public int getReceita() {
        return receita;
    }

    public void setReceita(int receita) {
        this.receita = receita;
    }

    public ProdutosReceitas(Produto produto, double quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
}
