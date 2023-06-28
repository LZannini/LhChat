package com.example.multichat;

import static com.example.multichat.R.id.recyclerView;
import static com.example.multichat.controller.Controller.ALLSTANZEERR;
import static com.example.multichat.controller.Controller.ALLSTANZEOK;
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
import com.example.multichat.adapters.RoomsAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Appartenenza_stanza;
import com.example.multichat.model.Stanza;

import java.util.ArrayList;

public class CercastanzaActivity extends AppCompatActivity implements RoomsAdapter.OnStanzaListener {

    private String[] risposta;
    private ArrayList<Stanza> lista_stanze = new ArrayList<Stanza>();
    private RecyclerView recyclerView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cercastanza);
        recyclerView = findViewById(R.id.recyclerView);
        TextView errorTextView = findViewById(R.id.errorTextView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cerca Stanza");
        actionBar.setDisplayHomeAsUpEnabled(true);

        Controller controller = new Controller();

        try {
            risposta = controller.vediAllStanze();
            int codComando = Integer.parseInt(risposta[0]);

            if(codComando == Integer.parseInt(ALLSTANZEOK)) {
                for(int i = 1; i < risposta.length; i++) {
                    String[] dati_stanze = risposta[i].split("\\,");
                    Stanza stanza = new Stanza(Integer.parseInt(dati_stanze[0]), dati_stanze[1], dati_stanze[2]);
                    lista_stanze.add(stanza);
                }
                RoomsAdapter adapter = new RoomsAdapter(lista_stanze, this);
                System.out.println(adapter.getItemCount());
                recyclerView.setAdapter(adapter);
            } else if(codComando == Integer.parseInt(ALLSTANZEERR)) {
                errorTextView.setText("Errore durante il caricamento delle stanze!");
                errorTextView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        return;
    }

    @Override
    public void onStanzaClick(int position) {

    }
}