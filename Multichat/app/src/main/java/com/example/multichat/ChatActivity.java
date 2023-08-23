package com.example.multichat;

import static com.example.multichat.controller.Controller.APRICHATERR;
import static com.example.multichat.controller.Controller.APRICHATOK;

import static com.example.multichat.controller.Controller.CHATVUOTA;
import static com.example.multichat.controller.Controller.ESCIDASTANZAERR;
import static com.example.multichat.controller.Controller.ESCIDASTANZAOK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.adapters.MessageAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Messaggio;
import com.example.multichat.model.Stanza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private int codComando;
    private static int roomId;
    private String[] risposta;
    private ArrayList<Messaggio> messages;
    private EditText messageEditText;
    private Button sendButton;
    private RecyclerView messageRecyclerView;
    private MessageAdapter messageAdapter;
    private Socket socket;
    private Controller controller;
    private volatile String notifica;
    private volatile Boolean connectionClosed = true;
    private Thread t;
    private String nome_admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageRecyclerView = findViewById(R.id.message_recyclerView);
        TextView errorTextView = findViewById(R.id.msg_errorTextView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String nome_stanza = intent.getStringExtra("nome_stanza");
        nome_admin = intent.getStringExtra("nome_admin");
        controller = new Controller();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nome_stanza);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (intent != null && intent.hasExtra("room_id")) {
            roomId = intent.getIntExtra("room_id", -1);

            messages = new ArrayList<>();
            messageEditText = findViewById(R.id.edit_text_message);
            sendButton = findViewById(R.id.button_send);

            try {
                risposta = controller.apriChat(roomId);
                codComando = Integer.parseInt(risposta[0]);

                if(codComando == Integer.parseInt(APRICHATOK)) {
                    String formatoData = "yyyy-MM-dd HH:mm:ss";
                    SimpleDateFormat sdf = new SimpleDateFormat(formatoData);
                    for (int i = 1; i < risposta.length; i++) {
                        String[] dati_messaggi = risposta[i].split("\\£");
                        Date orario = sdf.parse(dati_messaggi[1]);
                        Messaggio messaggio = new Messaggio(dati_messaggi[0], roomId, orario, dati_messaggi[2]);
                        if(messaggio.getMittente().equals(controller.getUtente().getUsername())) {
                            messaggio.setInviato(true);
                        } else {
                            messaggio.setInviato(false);
                        }
                        messages.add(messaggio);
                    }
                    messageRecyclerView.scrollToPosition(messages.size() - 1);
                    messageAdapter = new MessageAdapter(messages);
                    messageRecyclerView.setAdapter(messageAdapter);
                    connectionClosed = false;
                } else if(codComando == Integer.parseInt(APRICHATERR)){
                    errorTextView.setText("Errore durante il caricamento delle stanze!");
                    errorTextView.setVisibility(View.VISIBLE);
                } else if(codComando == Integer.parseInt(CHATVUOTA)) {
                    messageAdapter = new MessageAdapter(messages);
                    messageRecyclerView.setAdapter(messageAdapter);
                    connectionClosed = false;
                }
            }catch (Exception e) {
                e.printStackTrace(); // Registra la traccia dello stack dell'eccezione per identificare il problema
                errorTextView.setText("Si è verificato un errore durante l'apertura della chat!");
                errorTextView.setVisibility(View.VISIBLE);
            }


            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String testo_messaggio = messageEditText.getText().toString().trim();
                    long oraAttuale = System.currentTimeMillis();
                    Date data_messaggio = new Date(oraAttuale);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String data_formattata = sdf.format(data_messaggio);

                    if (!testo_messaggio.isEmpty()) {
                        try {
                            codComando = controller.inviaMessaggio(testo_messaggio, data_formattata, roomId);
                            if (codComando == Integer.parseInt(controller.INVIAMESSOK)) {
                                Messaggio messaggio = new Messaggio(controller.u.getUsername(), roomId, data_messaggio, testo_messaggio);
                                messaggio.setInviato(true);
                                messages.add(messaggio);
                                messageAdapter.notifyDataSetChanged();
                            } else {
                                //mostra messaggio d'errore all'utente
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        messageEditText.setText("");
                    }
                }
            });
        }
        connettiServer();
    }

    private void connettiServer() {
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                socket = controller.getUtente().getCurrentChatConnection();
                riceviMessaggiInRealTime();
            }
        });
        t.start();
    }

    private void riceviMessaggiInRealTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                    //System.out.println("wegbwejrg");
                    //InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    //BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    //BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                {
                    while (!connectionClosed) {
                        System.out.println("La connessione è ancora aperta.");
                        InputStream inputStream = socket.getInputStream();
                        byte[] buffer = new byte[2048];
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead == -1) {
                            System.out.println("La connessione è stata chiusa");
                            connectionClosed = true;
                        } else {
                            notifica = new String(buffer, 0, bytesRead);
                            System.out.println("stampo:" + notifica);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String formatoData = "yyyy-MM-dd HH:mm:ss";
                                    SimpleDateFormat sdf = new SimpleDateFormat(formatoData);
                                    String[] dati_messaggi = notifica.split("\\|");
                                    System.out.println("" + dati_messaggi[0] + "£" + dati_messaggi[1] + "£" + dati_messaggi[2] + "£" + dati_messaggi[3]);
                                    Messaggio messaggio;
                                    try {
                                        Date orario = sdf.parse(dati_messaggi[2]);
                                        messaggio = new Messaggio(dati_messaggi[0], roomId, orario, dati_messaggi[3]);
                                        messaggio.setInviato(false);
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                    messages.add(messaggio);
                                    messageAdapter.notifyItemInserted(messages.size() - 1);
                                    messageRecyclerView.scrollToPosition(messages.size() - 1);
                                }
                            });

                        }
                    }
                    System.out.println("Uscito dal while");
                } catch (IOException e) {
                    openActivityHome();
                    //throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                openActivityPartecipanti();
                return true;
            case R.id.item2:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Vuoi abbandonare questa stanza?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Controller controller = new Controller();
                                if (nome_admin.equals(controller.getUtente().getUsername())) {
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChatActivity.this);
                                    builder.setMessage("L'admin non può abbandonare la stanza!")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });
                                    android.app.AlertDialog alert = builder.create();
                                    alert.show();
                                    return;
                                }
                                try {
                                    codComando = controller.abbandonaStanza(roomId);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (codComando == Integer.parseInt(ESCIDASTANZAOK)) {
                                    openActivityHome();
                                }
                                else if (codComando == Integer.parseInt(ESCIDASTANZAERR)){
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChatActivity.this);
                                    builder.setMessage("Errore durante l'abbandono della stanza, riprova.")
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });
                                    android.app.AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .show();
                return true;
            case R.id.item3:
                if(nome_admin.equals(controller.getUtente().getUsername())) {
                    openActivityVediRichieste();
                } else {
                    System.out.println("nome_admin: "+nome_admin+"controller.bla.bla: "+controller.getUtente().getUsername());
                    builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setMessage("Solo l'admin può controllare se ci sono nuove richieste.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return true;
            default:
                try {
                    connectionClosed = true;
                    t.join();
                    controller.chiudiConnessione();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                openActivityHome();
                return true;
        }
    }


    public void openActivityPartecipanti(){
        Intent intentP = new Intent(this, PartecipantiActivity.class);
        startActivity(intentP);
    }

    public void openActivityHome(){
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }

    public void openActivityVediRichieste(){
        Intent intentH = new Intent(this, VediRichiesteActivity.class);
        intentH.putExtra("room_id", roomId);
        startActivity(intentH);
    }

    @Override
    public void onBackPressed() {
        try {
            connectionClosed = true;
            t.join();
            controller.chiudiConnessione();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        return;
    }

    public static int getRoomId() {
        return roomId;
    }
}