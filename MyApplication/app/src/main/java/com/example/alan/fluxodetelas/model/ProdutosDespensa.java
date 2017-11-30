package com.example.alan.fluxodetelas.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alan on 27/10/2017.
 */

public class ProdutosDespensa implements Serializable{
    private Produto produto;
    private int quantidade, id;
    private String validade;
    private Calendar val = Calendar.getInstance();


    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getVal() {
        return val;
    }

    public void setVal(Calendar val) {
        this.val = val;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public boolean isVencendo () {
        Calendar c1 = Calendar.getInstance(); // today
        c1.add(Calendar.DATE,7); //daqui a uma semana
        Calendar c2 = this.val;  //validade do produto

        int comp = c1.compareTo(c2);
        if (comp==0 || comp>0) {
            return true;
        }
        return false;
    }
}
