package com.example.multichat.model;

public class Stanza {

    private int id_stanza;
    private String nome_stanza;
    private String nome_admin;

    public Stanza(int id_stanza, String nome_stanza, String nome_admin) {
        setId_stanza(id_stanza);
        setNome_stanza(nome_stanza);
        setNome_admin(nome_admin);
    }

    public int getId_stanza() {
        return id_stanza;
    }

    public void setId_stanza(int id_stanza) {
        this.id_stanza = id_stanza;
    }

    public String getNome_stanza() {
        return nome_stanza;
    }

    public void setNome_stanza(String nome_stanza) {
        this.nome_stanza = nome_stanza;
    }

    public String getNome_admin() {
        return nome_admin;
    }

    public void setNome_admin(String nome_admin) {
        this.nome_admin = nome_admin;
    }
}
