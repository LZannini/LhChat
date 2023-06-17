package com.example.multichat;

import static com.example.multichat.controller.Controller.APRICHATERR;
import static com.example.multichat.controller.Controller.APRICHATOK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.adapters.MessageAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Messaggio;
import com.example.multichat.model.Stanza;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private int codComando;
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
        messageRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String nome_stanza = intent.getStringExtra("nome_stanza");
        Controller controller = new Controller();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(nome_stanza);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (intent != null && intent.hasExtra("room_id")) {
            int roomId = intent.getIntExtra("room_id", -1);
            long oraAttuale = System.currentTimeMillis();
            long oraInSecondi = oraAttuale/1000;

            messages = new ArrayList<>();
            messageEditText = findViewById(R.id.edit_text_message);
            sendButton = findViewById(R.id.button_send);

            try {
                risposta = controller.apriChat(roomId);
                codComando = Integer.parseInt(risposta[0]);

                if(codComando == Integer.parseInt(APRICHATOK)) {
                    for (int i = 1; i < risposta.length; i++) {
                        String[] dati_messaggi = risposta[i].split("\\,");
                        Messaggio messaggio = new Messaggio(dati_messaggi[0], roomId, null, dati_messaggi[2]);
                        messages.add(messaggio);
                    }
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
                    if (!testo_messaggio.isEmpty()) {
                        try {
                            codComando = controller.inviaMessaggio(testo_messaggio, oraInSecondi, roomId);
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


}