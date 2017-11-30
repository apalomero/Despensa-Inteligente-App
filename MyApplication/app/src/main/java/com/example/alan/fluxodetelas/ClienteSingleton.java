package com.example.alan.fluxodetelas;

import android.app.Application;

import com.example.alan.fluxodetelas.model.Cliente;

/**
 * Created by Alan on 16/11/2017.
 */

public class ClienteSingleton {
    private Cliente cliente;

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    private static final ClienteSingleton clienteSingleton = new ClienteSingleton();
    public static ClienteSingleton getInstance() {return clienteSingleton;}
}
