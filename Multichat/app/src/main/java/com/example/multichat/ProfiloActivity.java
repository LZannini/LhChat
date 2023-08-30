package com.example.multichat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.multichat.controller.Controller;

public class ProfiloActivity extends AppCompatActivity {

    private Button btnPw;
    private Button btnLg;
    private int count;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Il mio Profilo");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Controller controller = new Controller();

        btnPw = (Button) findViewById(R.id.editProfileButton);
        btnPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityModificaPassword();
            }
        });

        btnLg = (Button) findViewById(R.id.logoutButton);
        btnLg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(ProfiloActivity.this);
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
            }
        });

        TextView textViewUsername = (TextView) findViewById(R.id.username);
        textViewUsername.setText(controller.getUtente().getUsername());
        TextView textViewPassword = (TextView) findViewById(R.id.password);
        textViewPassword.setText(controller.getUtente().getPassword());
        textViewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        count = 0;
        textViewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                if (count % 2 == 0)
                    textViewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD) ;
                else
                    textViewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
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