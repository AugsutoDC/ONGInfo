package com.example.danco.onginfo.Entidades;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

public class Evento {
    private String id;
    private String ongId;
    private String descricao;
    private String data;
    private String endereco;
    private String titulo;

    public Evento() {
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("eventos").child(ongId).child(String.valueOf(getId())).setValue(this);
    }

    public String toString() {
        return titulo + " - " + data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getOngId() {
        return ongId;
    }

    public void setOngId(String ongId) {
        this.ongId = ongId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
