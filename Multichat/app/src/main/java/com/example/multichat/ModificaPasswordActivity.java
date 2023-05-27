package com.example.multichat;

import static com.example.multichat.controller.Controller.MODPASSERR;
import static com.example.multichat.controller.Controller.MODPASSOK;

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

public class ModificaPasswordActivity extends AppCompatActivity {

    private Button btnC;
    private int codComando;
    private EditText passwordEditText;
    private EditText confPassEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Modifica Password");
        actionBar.setDisplayHomeAsUpEnabled(true);

        passwordEditText = findViewById(R.id.password);
        confPassEditText = findViewById(R.id.conferma_password);

        btnC = (Button) findViewById(R.id.btn_confirm);
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = passwordEditText.getText().toString();
                String conferma_password = confPassEditText.getText().toString();

                if (password.equals(conferma_password)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModificaPasswordActivity.this);
                    builder.setMessage("Sei sicuro di voler cambiare password?")
                            .setCancelable(false)
                            .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Controller controller = new Controller();
                                    try {
                                        codComando = controller.aggiornaPassword(password);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    if ((codComando == Integer.parseInt(MODPASSOK))) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(ModificaPasswordActivity.this);
                                        builder2.setMessage("Cambio password effettuato con successo!")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        openActivityProfilo();
                                                    }
                                                });
                                        AlertDialog alert2 = builder2.create();
                                        alert2.show();
                                    }
                                    else if (codComando == Integer.parseInt(MODPASSERR)){
                                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ModificaPasswordActivity.this);
                                        builder.setMessage("Errore durante la modifica della password, riprova.")
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
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ModificaPasswordActivity.this);
                    builder.setMessage("Le password inserite non coincidono!")
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