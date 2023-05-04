package com.example.multichat.model;

public class Richiesta_stanza {

    private String utente;
    private int id_stanza;

    public Richiesta_stanza(String utente, int id_stanza) {
        setUtente(utente);
        setId_stanza(id_stanza);
    }

    public String getUtente() {
        return utente;
    }

    public void setUtente(String utente) {
        this.utente = utente;
    }

    public int getId_stanza() {
        return id_stanza;
    }

    public void setId_stanza(int id_stanza) {
        this.id_stanza = id_stanza;
    }
}
