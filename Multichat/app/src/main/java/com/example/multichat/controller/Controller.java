package com.example.multichat.controller;
import static android.webkit.ConsoleMessage.MessageLevel.LOG;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.multichat.model.Stanza;
import com.example.multichat.model.Utente;

public class Controller {
    //comandi client
    public static final String LOGIN = "001";
    public static final String REG = "002";
    public static final String CREASTANZA = "003";
    public static final String CERCASTANZA = "004";
    public static final String INVIAMESS = "005";
    public static final String APRICHAT = "006";
    public static final String ACCETTARIC = "007";
    public static final String MODPASS = "008";
    public static final String MODUSER = "009";
    public static final String VEDISTANZE = "010";
    public static final String VEDIPART = "011";
    public static final String ELIMINAUSER = "012";
    public static final String ELIMINASTANZA = "013";
    public static final String ESCIDASTANZA = "014";
    public static final String RICHIESTASTANZA = "015";
    public static final String VEDIRICHIESTE = "016";
    public static final String ADMIN = "017";

    //comandi OK
    public static final String LOGINOK = "101";
    public static final String REGOK = "102";
    public static final String CREASTANZAOK = "103";
    public static final String CERCASTANZAOK = "104";
    public static final String INVIAMESSOK = "105";
    public static final String APRICHATOK = "106";
    public static final String ACCETTARICOK = "107";
    public static final String MODPASSOK = "108";
    public static final String MODUSEROK = "109";
    public static final String VEDISTANZEOK = "110";
    public static final String VEDIPARTOK = "111";
    public static final String ELIMINAUSEROK = "112";
    public static final String ELIMINASTANZAOK = "113";
    public static final String ESCIDASTANZAOK = "114";
    public static final String RICHIESTASTANZAOK = "115";
    public static final String VEDIRICHIESTEOK = "116";
    public static final String ADMINSI = "117";

    //comandi ERR
    public static final String LOGINERR = "201";
    public static final String REGERR = "202";
    public static final String CREASTANZAERR = "203";
    public static final String CERCASTANZAERR = "204";
    public static final String INVIAMESSERR = "205";
    public static final String APRICHATERR = "206";
    public static final String ACCETTARICERR = "207";
    public static final String MODPASSERR = "208";
    public static final String MODUSERERR = "209";
    public static final String VEDISTANZEERR = "210";
    public static final String VEDIPARTERR = "211";
    public static final String ELIMINAUSERERR = "212";
    public static final String ELIMINASTANZAERR = "213";
    public static final String ESCIDASTANZAERR = "214";
    public static final String RICHIESTASTANZAERR = "215";
    public static final String VEDIRICHIESTEERR = "216";
    public static final String ADMINERR = "217";

    //altri comandi
    public static final String LOGINNONTROVATO = "301";
    public static final String GIAREGISTRATO = "302";
    public static final String CHATVUOTA = "306";
    public static final String STANZENONTROVATE = "310";
    public static final String NOPART = "311";
    public static final String NORICHIESTE = "316";
    public static final String ADMINNO = "317";


    private Socket socket;
    private static Controller controller = null;

    private int codComando;
    private ArrayList<Stanza> lista_stanze;
    String[] dati = new String[0];
    private static Utente u = new Utente("", "");

    private static final int SERVERPORT = 5000;
    private static final String SERVER_IP = "192.168.178.24";

    public Controller() {
    }

    public static Controller getInstance() {
        if (controller != null)
            return controller;

        return new Controller();
    }

    public static Controller getNewInstance() {
        controller = null;
        controller = new Controller();
        return controller;
    }

    public int registrazione(String username, String password) throws Exception {
        String richiesta = REG + "|" + username + "|" + password;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    String[] dati = risposta.split("\\|");
                    codComando = Integer.parseInt(dati[0]);
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Registrazione non riuscita, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return codComando;
    }

    public int login(String username, String password) throws Exception{
        String richiesta = LOGIN + "|" + username + "|" + password;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    String[] dati = risposta.split("\\|");
                    codComando = Integer.parseInt(dati[0]);
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Login non riuscito, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return codComando;
    }

    public int creaStanza(String nome_stanza) throws Exception{
        String richiesta = CREASTANZA + "|" + nome_stanza + "|" + u.getUsername();
        System.out.println(richiesta);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    String[] dati = risposta.split("\\|");
                    codComando = Integer.parseInt(dati[0]);
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Creazione stanza non riuscita, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return codComando;
    }

    public int eliminaUtente(String username) throws InterruptedException {
        String richiesta = ELIMINAUSER + "|" + username;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    String[] dati = risposta.split("\\|");
                    codComando = Integer.parseInt(dati[0]);
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Eliminazione utente non riuscita, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return codComando;
    }

    public int aggiornaPassword(String new_password) throws InterruptedException {
        String richiesta = MODPASS + "|" + u.getUsername() + "|" + new_password;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    String[] dati = risposta.split("\\|");
                    codComando = Integer.parseInt(dati[0]);
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Aggiornamento password non riuscito, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return codComando;
    }

    public int aggiornaUsername(String new_username) throws InterruptedException {
        String richiesta = MODUSER + "|" + u.getUsername() + "|" + new_username;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    String[] dati = risposta.split("\\|");
                    codComando = Integer.parseInt(dati[0]);
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Aggiornamento username non riuscito, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return codComando;
    }

    public String[] vediStanze() throws Exception {
        String richiesta = VEDISTANZE + "|" + u.getUsername();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    dati = risposta.split("\\|");
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Aggiornamento username non riuscito, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return dati;
    }

    public String[] apriChat(int id_stanza) throws Exception {
        String richiesta = APRICHAT + "|" + id_stanza + "|" + u.getUsername();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    dati = risposta.split("\\|");
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Apertura chat non riuscita, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return dati;
    }

    public int inviaMessaggio(String testo, long orario, int id_stanza) throws Exception {
        String richiesta = INVIAMESS + "|" + u.getUsername() + "|" + id_stanza + "|" + orario + "|" + testo;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket(SERVER_IP, SERVERPORT);
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();
                    // Invio i dati
                    outputStream.write(richiesta.getBytes());
                    outputStream.flush();
                    // Ricezione risposta
                    byte[] buffer = new byte[1024];
                    int bytesRead = inputStream.read(buffer);
                    String risposta = new String(buffer, 0, bytesRead);
                    String[] dati = risposta.split("\\|");
                    codComando = Integer.parseInt(dati[0]);
                    socket.close();
                } catch (Exception e) {
                    System.out.println("Invio messaggio non riuscito, socket chiusa");
                }
            }
        });
        t.start(); // Avvio del thread
        t.join(); // Attendo la terminazione del thread
        return codComando;
    }

    public void setUtente(Utente utente) {
        this.u = utente;
    }

    public Utente getUtente() { return this.u; }


}
