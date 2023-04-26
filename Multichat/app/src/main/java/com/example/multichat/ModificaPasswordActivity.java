package com.example.multichat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ModificaPasswordActivity extends AppCompatActivity {

    private Button btnC;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Modifica Password");

        btnC = (Button) findViewById(R.id.btn_confirm);
        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ModificaPasswordActivity.this);
                builder.setMessage("Sei sicuro di voler cambiare password?")
                        .setCancelable(false)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
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
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void openActivityProfilo(){
        Intent intentP = new Intent(this, ProfiloActivity.class);
        startActivity(intentP);
    }
}