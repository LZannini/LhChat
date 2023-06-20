package com.example.multichat;

import static com.example.multichat.controller.Controller.ELIMINAUSERERR;
import static com.example.multichat.controller.Controller.ELIMINAUSEROK;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.multichat.controller.Controller;

public class ProfiloActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;
    private int codComando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Il mio Profilo");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Controller controller = new Controller();

        TextView textViewUsername = (TextView) findViewById(R.id.tv_username);
        TextView textViewPassword = (TextView) findViewById(R.id.tv_password);
        textViewUsername.setText(controller.getUtente().getUsername());
        textViewPassword.setText(controller.getUtente().getPassword());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profilo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                openActivityModificaUsername();
                return true;
            case R.id.item2:
                openActivityModificaPassword();
                return true;
            case R.id.item3:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Vuoi davvero eliminare il tuo profilo?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Controller controller = new Controller();
                                try {
                                    codComando = controller.eliminaUtente(controller.getUtente().getUsername());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (codComando == Integer.parseInt(ELIMINAUSEROK)) {
                                    Context ctx = getApplicationContext();
                                    PackageManager pm = ctx.getPackageManager();
                                    Intent intent = pm.getLaunchIntentForPackage(ctx.getPackageName());
                                    Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
                                    ctx.startActivity(mainIntent);
                                    Runtime.getRuntime().exit(0);
                                }
                                else if (codComando == Integer.parseInt(ELIMINAUSERERR)){
                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfiloActivity.this);
                                    builder.setMessage("Errore durante l'eliminazione dell'utente, riprova.")
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
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .show();
                return true;
            case R.id.item4:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Sei sicuro di voler uscire?")
                        .setCancelable(true)
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Context ctx = getApplicationContext();
                                PackageManager pm = ctx.getPackageManager();
                                Intent intent = pm.getLaunchIntentForPackage(ctx.getPackageName());
                                Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
                                ctx.startActivity(mainIntent);
                                Runtime.getRuntime().exit(0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) { }
                        })
                        .show();
                return true;
            default:
                openActivityHome();
                return true;
        }

    }

    public void openActivityModificaUsername(){
        Intent intentMU = new Intent(this, ModificaUsernameActivity.class);
        startActivity(intentMU);
    }

    public void openActivityModificaPassword(){
        Intent intentMP = new Intent(this, ModificaPasswordActivity.class);
        startActivity(intentMP);
    }

    public void openActivityHome(){
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        return;
    }
}