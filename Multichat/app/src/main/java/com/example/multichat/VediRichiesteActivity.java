package com.example.multichat;

import static com.example.multichat.controller.Controller.VEDIRICHIESTEERR;
import static com.example.multichat.controller.Controller.VEDIRICHIESTEOK;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multichat.adapters.PartecipantiAdapter;
import com.example.multichat.adapters.RichiesteAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Richiesta_stanza;

import java.util.ArrayList;

public class VediRichiesteActivity extends AppCompatActivity implements RichiesteAdapter.OnUtenteListener {

    private String[] risposta;
    private ArrayList<Richiesta_stanza> lista_richieste = new ArrayList<>();
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedirichieste);
        recyclerView = findViewById(R.id.recyclerView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView errorTextView = findViewById(R.id.errorTextViewAllRooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Richieste");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Controller controller = new Controller();

        try {
            risposta = controller.vediPartecipanti(ChatActivity.getRoomId());
            int codComando = Integer.parseInt(risposta[0]);

            if(codComando == Integer.parseInt(VEDIRICHIESTEOK)) {
                for(int i = 1; i < risposta.length; i++) {
                    String[] dati_partecipanti = risposta[i].split("\\,");
                    Richiesta_stanza richiesta = new Richiesta_stanza(dati_partecipanti[0], -1);
                    lista_richieste.add(richiesta);
                }
                RichiesteAdapter adapter = new RichiesteAdapter(lista_richieste, this) {
                    @Override
                    public void onUtenteClick(int position) {

                    }
                };
                System.out.println(adapter.getItemCount());
                recyclerView.setAdapter(adapter);
            } else if(codComando == Integer.parseInt(VEDIRICHIESTEERR)) {
                errorTextView.setText("Errore durante il caricamento delle richieste!");
                errorTextView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //Intent myIntent = new Intent(getApplicationContext(), ChatActivity.class);
        //startActivityForResult(myIntent, 0);
        finish();
        return true;
    }

    /*@Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        return;
    }*/

    @Override
    public void onUtenteClick(int position) { }

}
