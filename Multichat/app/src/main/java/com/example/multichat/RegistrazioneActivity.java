package com.example.multichat;

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
import com.example.multichat.model.Utente;

public class RegistrazioneActivity extends AppCompatActivity {

    private int codComando;
    private Button btnR;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText confPassEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        confPassEditText = findViewById(R.id.conferma_password);

        Controller controller = new Controller();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Registrazione");
        actionBar.setDisplayHomeAsUpEnabled(true);

        btnR = (Button) findViewById(R.id.btn_registra);
        btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confPass = confPassEditText.getText().toString();

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrazioneActivity.this);

                if (username.length() == 0 || password.length() == 0 || confPass.length() == 0) {
                    builder.setMessage("Bisogna riempire tutti i campi!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    confPassEditText.setText("");
                    return;
                }

                if (password.equals(confPass)) {
                    try {
                        codComando = controller.registrazione(username, password);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    if(codComando == Integer.parseInt(controller.REGOK)) {
                        Utente u = new Utente(username, password);
                        controller.setUtente(u);
                        builder.setMessage("Registrazione effettuata con successo!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        openActivityHome();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }else if(codComando == Integer.parseInt(controller.REGERR)) {
                        builder.setMessage("Errore durante la fase di registrazione, riprova!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                        usernameEditText.setText("");
                        passwordEditText.setText("");
                        confPassEditText.setText("");
                    }else if(codComando == Integer.parseInt(controller.GIAREGISTRATO)) {
                        builder.setMessage("Questo utente risulta già registrato, effettua il login!")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        openActivityLogin();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                } else {
                    builder.setMessage("Le password non corrispondono, riprova")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    usernameEditText.setText("");
                    passwordEditText.setText("");
                    confPassEditText.setText("");
                }
            }
        });
    }

    public void openActivityHome(){
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void openActivityRegistrazione(){
        Intent intentR = new Intent(this, RegistrazioneActivity.class);
        startActivity(intentR);
    }

    public void openActivityLogin(){
        Intent intentR = new Intent(this, LoginActivity.class);
        startActivity(intentR);
    }
}