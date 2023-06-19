package com.example.multichat.model;

import java.util.Date;

public class Messaggio {

    private String mittente;
    private int id_stanza;
    private Date ora_invio;
    private String testo;
    private Boolean inviato;

    public Messaggio(String mittente, int id_stanza, Date ora_invio, String testo) {
        setMittente(mittente);
        setId_stanza(id_stanza);
        setOra_invio(ora_invio);
        setTesto(testo);
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public int getId_stanza() {
        return id_stanza;
    }

    public void setId_stanza(int id_stanza) {
        this.id_stanza = id_stanza;
    }

    public Date getOra_invio() {
        return ora_invio;
    }

    public void setOra_invio(Date ora_invio) {
        this.ora_invio = ora_invio;
    }

    public String getTesto() {
        return testo;
    }

    public void setTesto(String testo) {
        this.testo = testo;
    }

    public Boolean isInviato() {
        return inviato;
    }

    public void setInviato(boolean inviato) {
        this.inviato = inviato;
    }
}
