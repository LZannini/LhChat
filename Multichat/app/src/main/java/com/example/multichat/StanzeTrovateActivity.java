package com.example.multichat;

import static com.example.multichat.controller.Controller.CERCASTANZAOK;
import static com.example.multichat.controller.Controller.CERCASTANZAERR;
import static com.example.multichat.controller.Controller.RICHIESTASTANZAERR;
import static com.example.multichat.controller.Controller.RICHIESTASTANZAOK;
import static com.example.multichat.controller.Controller.STANZENONTROVATE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.multichat.adapters.RoomsAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Stanza;

import java.util.ArrayList;


public class StanzeTrovateActivity extends AppCompatActivity implements RoomsAdapter.OnStanzaListener {

    private String[] risposta;
    private int codComando;
    private ArrayList<Stanza> lista_stanze;
    private RecyclerView recyclerView;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stanzetrovate);
        recyclerView = findViewById(R.id.recyclerView);
        TextView errorTextView = findViewById(R.id.errorTextViewAllRooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Stanze Trovate");
        actionBar.setDisplayHomeAsUpEnabled(true);
        lista_stanze = new ArrayList<>();

        controller = new Controller();;

        try {
            risposta = controller.cercaStanza(CercastanzaActivity.getNome_stanza());
            codComando = Integer.parseInt(risposta[0]);
            if(codComando == Integer.parseInt(CERCASTANZAOK)) {
                for(int i = 1; i < risposta.length; i++) {
                    String[] dati_stanze = risposta[i].split("\\,");
                    Stanza stanza = new Stanza(Integer.parseInt(dati_stanze[0]), dati_stanze[1], dati_stanze[2]);
                    lista_stanze.add(stanza);
                }
                RoomsAdapter adapter = new RoomsAdapter(lista_stanze, this);
                System.out.println(adapter.getItemCount());
                recyclerView.setAdapter(adapter);
            } else if(codComando == Integer.parseInt(CERCASTANZAERR)) {
                errorTextView.setText("Errore durante il caricamento delle stanze!");
                errorTextView.setVisibility(View.VISIBLE);
            }
            else if (codComando == Integer.parseInt(STANZENONTROVATE)) {
                errorTextView.setText("Stanze non trovate!");
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
    public void onStanzaClick(int position) {
        Stanza selectedStanza = lista_stanze.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vuoi richiedere l'accesso per questa stanza?\nnome stanza: "+selectedStanza.getNome_stanza()+"\nadmin: "+selectedStanza.getNome_admin())
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        controller = new Controller();
                        try {
                            int codComando = controller.richiesta_stanza(selectedStanza.getId_stanza());

                            if (codComando == Integer.parseInt(RICHIESTASTANZAOK)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StanzeTrovateActivity.this);
                                builder.setMessage("Richiesta effettuata con successo!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                openActivityHome();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else if (codComando == Integer.parseInt(RICHIESTASTANZAERR)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(StanzeTrovateActivity.this);
                                builder.setMessage("Si è verificato un errore durante la richiesta d'accesso alla stanza!")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                openActivityHome();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //non fa nulla e chiude il dialog
                    }
                });
        builder.show();
    }

    private void openActivityHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}