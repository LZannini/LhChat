package com.example.multichat;

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

public class HomeActivity extends AppCompatActivity {

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                openActivityCerca();
                return true;
            case R.id.item2:
                openActivityCrea();
                return true;
            case R.id.item3:
                openActivityMieStanze();
                return true;
            case R.id.item4:
                openActivityProfilo();
                return true;
            case R.id.item5:
                builder = new AlertDialog.Builder(this);
                builder.setMessage("Sei sicuro di voler disconnetterti?")
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

    public void openActivityCerca(){
        Intent intentH = new Intent(this, CercastanzaActivity.class);
        startActivity(intentH);
    }

    public void openActivityCrea(){
        Intent intentH = new Intent(this, CreastanzaActivity.class);
        startActivity(intentH);
    }

    public void openActivityMieStanze(){
        Intent intentH = new Intent(this, MystanzeActivity.class);
        startActivity(intentH);
    }

    public void openActivityProfilo(){
        Intent intentP = new Intent(this, ProfiloActivity.class);
        startActivity(intentP);
    }

    public void openActivityLogin(){
        Intent intentH = new Intent(this, LoginActivity.class);
        startActivity(intentH);
    }

    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Sei sicuro di voler disconnetterti?")
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
        return;
    }
}