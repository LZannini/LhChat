package com.example.multichat;

import static com.example.multichat.controller.Controller.VEDIPARTERR;
import static com.example.multichat.controller.Controller.VEDIPARTOK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.multichat.adapters.PartecipantiAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Appartenenza_stanza;

import java.util.ArrayList;

public class PartecipantiActivity extends AppCompatActivity implements PartecipantiAdapter.OnUtenteListener {

    private String[] risposta;
    private ArrayList<Appartenenza_stanza> lista_partecipanti = new ArrayList<>();
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partecipanti);
        recyclerView = findViewById(R.id.recyclerView);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView errorTextView = findViewById(R.id.errorTextViewAllRooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Partecipanti");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Controller controller = new Controller();

        try {
            risposta = controller.vediPartecipanti(ChatActivity.getRoomId());
            int codComando = Integer.parseInt(risposta[0]);

            if(codComando == Integer.parseInt(VEDIPARTOK)) {
                for(int i = 1; i < risposta.length; i++) {
                    String[] dati_partecipanti = risposta[i].split("\\,");
                    Appartenenza_stanza partecipante = new Appartenenza_stanza(dati_partecipanti[0], -1);
                    lista_partecipanti.add(partecipante);
                }
                PartecipantiAdapter adapter = new PartecipantiAdapter(lista_partecipanti, this);
                System.out.println(adapter.getItemCount());
                recyclerView.setAdapter(adapter);
            } else if(codComando == Integer.parseInt(VEDIPARTERR)) {
                errorTextView.setText("Errore durante il caricamento dei partecipanti!");
                errorTextView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), ChatActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        return;
    }

    @Override
    public void onUtenteClick(int position) { }
}