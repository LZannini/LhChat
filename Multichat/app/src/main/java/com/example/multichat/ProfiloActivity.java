package com.example.multichat;

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

public class ProfiloActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Il mio Profilo");
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

                return true;
            case R.id.item2:

                return true;
            case R.id.item3:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Vuoi davvero eliminare il tuo profilo?")
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
                return super.onOptionsItemSelected(item);
        }

    }
}