package com.example.multichat;

import static com.example.multichat.controller.Controller.ACCETTARICERR;
import static com.example.multichat.controller.Controller.ACCETTARICOK;
import static com.example.multichat.controller.Controller.RIFIUTARICERR;
import static com.example.multichat.controller.Controller.RIFIUTARICOK;
import static com.example.multichat.controller.Controller.VEDIRICHIESTEERR;
import static com.example.multichat.controller.Controller.VEDIRICHIESTEOK;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
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
    private Controller controller;
    private RichiesteAdapter adapter;
    private int id_stanza;

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

        Intent intent = getIntent();
        id_stanza = intent.getIntExtra("room_id", -1);
        Controller controller = new Controller();

        try {
            risposta = controller.vediRichieste(ChatActivity.getRoomId());
            int codComando = Integer.parseInt(risposta[0]);

            if(codComando == Integer.parseInt(VEDIRICHIESTEOK)) {
                for(int i = 1; i < risposta.length; i++) {
                    String[] dati_partecipanti = risposta[i].split("\\,");
                    Richiesta_stanza richiesta = new Richiesta_stanza(dati_partecipanti[0], id_stanza);
                    lista_richieste.add(richiesta);
                }
                adapter = new RichiesteAdapter(lista_richieste, this);
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
    public void onUtenteClick(int position) {
        Richiesta_stanza selectedRichiesta = lista_richieste.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vuoi accettare la richiesta d'accesso di "+selectedRichiesta.getUtente())
                .setCancelable(false)
                .setPositiveButton("Accetta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        controller = new Controller();
                        try {
                            int codComando = controller.accetta_richiesta(selectedRichiesta.getId_stanza(), selectedRichiesta.getUtente());

                            if (codComando == Integer.parseInt(ACCETTARICOK)) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VediRichiesteActivity.this);
                                builder.setMessage("Richiesta accettata con successo!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                openActivityVediRichieste();
                                            }
                                        });
                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            } else if (codComando == Integer.parseInt(ACCETTARICERR)){
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VediRichiesteActivity.this);
                                builder.setMessage("Si è verificato un errore durante l'accettazione della richiesta d'accesso alla stanza!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNegativeButton("Rifiuta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        controller = new Controller();
                        try {
                            int codComando = controller.rifiuta_richiesta(selectedRichiesta.getId_stanza(), selectedRichiesta.getUtente());

                            if (codComando == Integer.parseInt(RIFIUTARICOK)) {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VediRichiesteActivity.this);
                                builder.setMessage("Richiesta rifiutata con successo!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                openActivityVediRichieste();
                                            }
                                        });
                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            } else if (codComando == Integer.parseInt(RIFIUTARICERR)){
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(VediRichiesteActivity.this);
                                builder.setMessage("Si è verificato un errore durante il rifiuto della richiesta d'accesso alla stanza!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                adapter.rimuoviRichiesta(position);
                                            }
                                        });
                                android.app.AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.show();
    }

    public void openActivityVediRichieste(){
        Intent intentH = new Intent(this, VediRichiesteActivity.class);
        intentH.putExtra("room_id", id_stanza);
        startActivity(intentH);
    }
}
