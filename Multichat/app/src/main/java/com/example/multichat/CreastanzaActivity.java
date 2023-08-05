package com.example.multichat;

import static com.example.multichat.controller.Controller.CREASTANZAERR;
import static com.example.multichat.controller.Controller.CREASTANZAOK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.multichat.controller.Controller;

public class CreastanzaActivity extends AppCompatActivity {

    private int codComando;
    private Button btnC;
    private EditText nomeStanzaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creastanza);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Crea Stanza");
        actionBar.setDisplayHomeAsUpEnabled(true);

        nomeStanzaEditText = findViewById(R.id.stanzacercata);
        Controller controller = new Controller();

        btnC = (Button) findViewById(R.id.btn_create);
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_stanza = nomeStanzaEditText.getText().toString();

                try {
                    codComando = controller.creaStanza(nome_stanza);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

                if (codComando == Integer.parseInt(CREASTANZAOK)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreastanzaActivity.this);
                    builder.setMessage("Stanza creata con successo!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    openActivityHome();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else if(codComando == Integer.parseInt(CREASTANZAERR)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreastanzaActivity.this);
                    builder.setMessage("Errore durante la creazione della stanza, riprova.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) { }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
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

    public void openActivityHome() {
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }
}