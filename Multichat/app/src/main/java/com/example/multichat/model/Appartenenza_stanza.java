package com.example.multichat.model;

public class Appartenenza_stanza {

    private String username;
    private int id_stanza;

    public Appartenenza_stanza(String username, int id_stanza) {
        setUsername(username);
        setId_stanza(id_stanza);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId_stanza() {
        return id_stanza;
    }

    public void setId_stanza(int id_stanza) {
        this.id_stanza = id_stanza;
    }
}
