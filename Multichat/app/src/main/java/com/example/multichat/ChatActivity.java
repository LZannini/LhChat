package com.example.multichat;

import static com.example.multichat.controller.Controller.APRICHATERR;
import static com.example.multichat.controller.Controller.APRICHATOK;

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
        Controller controller = new Controller();

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
                        String[] dati_messaggi = risposta[i].split("\\,");
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
                } else if(codComando == Integer.parseInt(APRICHATERR)){
                    errorTextView.setText("Errore durante il caricamento delle stanze!");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                throw new RuntimeException();
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
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        messageEditText.setText("");
                    }
                }
            });
        }
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
            default:
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        return;
    }

    public static int getRoomId() {
        return roomId;
    }
}