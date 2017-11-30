package com.example.alan.fluxodetelas;

import com.example.alan.fluxodetelas.model.Categoria;
import com.example.alan.fluxodetelas.model.Cliente;

import java.util.List;

/**
 * Created by Alan on 16/11/2017.
 */

public class CategoriasSingleton {
    private List<Categoria> categorias;

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    private static final CategoriasSingleton categoriasSingleton = new CategoriasSingleton();
    public static CategoriasSingleton getInstance() {return categoriasSingleton;}
}
