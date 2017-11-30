package com.example.alan.fluxodetelas.model;

import java.io.Serializable;

/**
 * Created by Alan on 15/11/2017.
 */

public class ReceitasFavoritas implements Serializable {
    int cliente, receita, id;

    public ReceitasFavoritas() {
    }

    public ReceitasFavoritas(int cliente, int receita) {
        this.cliente = cliente;
        this.receita = receita;
    }

    public ReceitasFavoritas(int cliente, int receita, int id) {
        this.cliente = cliente;
        this.receita = receita;
        this.id = id;
    }

    public int getCliente() {
        return cliente;
    }

    public void setCliente(int cliente) {
        this.cliente = cliente;
    }

    public int getReceita() {
        return receita;
    }

    public void setReceita(int receita) {
        this.receita = receita;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
