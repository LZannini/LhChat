package com.example.multichat;

import static com.example.multichat.controller.Controller.ALLSTANZEERR;
import static com.example.multichat.controller.Controller.ALLSTANZEOK;
import static com.example.multichat.controller.Controller.RICHIESTASTANZAERR;
import static com.example.multichat.controller.Controller.RICHIESTASTANZAOK;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.multichat.adapters.RoomsAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Stanza;

import java.util.ArrayList;

public class CercastanzaActivity extends AppCompatActivity implements RoomsAdapter.OnStanzaListener {

    private String[] risposta;
    private ArrayList<Stanza> lista_stanze = new ArrayList<Stanza>();
    private RecyclerView recyclerView;
    private Controller controller;
    private EditText stanza_cercata;
    private Button btnS;
    private int codComando;
    private static String nome_stanza;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cercastanza);
        recyclerView = findViewById(R.id.recyclerView);
        TextView errorTextView = findViewById(R.id.errorTextViewAllRooms);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cerca Stanza");
        actionBar.setDisplayHomeAsUpEnabled(true);

        controller = new Controller();

        stanza_cercata = findViewById(R.id.stanzacercata);
        btnS = (Button) findViewById(R.id.btn_search);
        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nome_stanza = stanza_cercata.getText().toString();

                if (nome_stanza.length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CercastanzaActivity.this);
                    builder.setMessage("Non hai digitato alcuna stanza!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    return;
                }
                openActivityStanzeTrovate();
            }
        });

        try {
            risposta = controller.esploraStanze();
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(CercastanzaActivity.this);
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(CercastanzaActivity.this);
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

    public void openActivityHome(){
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }

    private void openActivityStanzeTrovate() {
        Intent intent = new Intent(this, StanzeTrovateActivity.class);
        startActivity(intent);
    }

    public static String getNome_stanza() {
        return nome_stanza;
    }
}