package com.example.multichat;

import static com.example.multichat.controller.Controller.MODUSERERR;
import static com.example.multichat.controller.Controller.MODUSEROK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.multichat.controller.Controller;

public class ModificaUsernameActivity extends AppCompatActivity {

    private Button btnC;
    private int codComando;
    private EditText usernameEditText;
    private EditText confUserEditText;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_username);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Modifica Username");
        actionBar.setDisplayHomeAsUpEnabled(true);

        usernameEditText = findViewById(R.id.username);
        confUserEditText = findViewById(R.id.conferma_username);

        btnC = (Button) findViewById(R.id.btn_confirm);
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String confUsername = confUserEditText.getText().toString();

                if (username.equals(confUsername)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModificaUsernameActivity.this);
                    builder.setMessage("Sei sicuro di voler cambiare nome utente?")
                            .setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Controller controller = new Controller();
                                    try {
                                        codComando = controller.aggiornaUsername(username);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if ((codComando == Integer.parseInt(MODUSEROK))) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ModificaUsernameActivity.this);
                                        builder2.setMessage("Cambio username effettuato con successo!")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        openActivityProfilo();
                                                    }
                                                });
                                        AlertDialog alert2 = builder2.create();
                                        alert2.show();
                                    }
                                    else if (codComando == Integer.parseInt(MODUSERERR)){
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ModificaUsernameActivity.this);
                                        builder.setMessage("Errore durante la modifica dell'username, riprova.")
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
                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModificaUsernameActivity.this);
                    builder.setMessage("Gli username inseriti non coincidono!")
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
        Intent myIntent = new Intent(getApplicationContext(), ProfiloActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void openActivityProfilo(){
        Intent intentP = new Intent(this, ProfiloActivity.class);
        startActivity(intentP);
    }
}