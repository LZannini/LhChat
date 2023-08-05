package com.example.multichat.model;

import java.net.Socket;

public class Utente {

    private String username;
    private String password;
    private Socket currentChatConnection;

    public Utente(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setCurrentChatConnection(Socket connection) { this.currentChatConnection = connection; }
    public Socket getCurrentChatConnection() { return currentChatConnection; }
}
