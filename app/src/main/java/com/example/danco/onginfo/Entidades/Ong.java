package com.example.danco.onginfo.Entidades;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ong {
    private String id;
    private String email;
    private String senha;
    private String nome;
    private String local;
    private String telefone;
    private String sobre;


    public Ong() {
    }

    public void salvar(){
        DatabaseReference referenciaFirebase = ConfiguracaoFirebase.getFirebase();
        referenciaFirebase.child("ong").child(String.valueOf(getId())).setValue(this);
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> hashMapOng = new HashMap<>();

        hashMapOng.put("id", getId());
        hashMapOng.put("email", getEmail());
        hashMapOng.put("senha", getSenha());
        hashMapOng.put("nome", getNome());
        hashMapOng.put("local", getLocal());
        hashMapOng.put("telefone", getTelefone());
        hashMapOng.put("sobre", getSobre());


        return hashMapOng;
    }

    public String toString() {
        return nome.toUpperCase() + " \n  " + local;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocal() { return local; }

    public void setLocal(String local) { this.local = local; }

    public String getTelefone() { return telefone; }

    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getSobre() { return sobre; }

    public void setSobre(String sobre) { this.sobre = sobre; }

}
