package com.example.multichat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.multichat.ui.login.LoginActivity;

public class RegistrazioneActivity extends AppCompatActivity {

    private Button btnR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnR = (Button) findViewById(R.id.btn_registra);
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrazioneActivity.this);
                builder.setMessage("Registrazione effettuata con successo!")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openActivityHome();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    public void openActivityHome(){
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }
}