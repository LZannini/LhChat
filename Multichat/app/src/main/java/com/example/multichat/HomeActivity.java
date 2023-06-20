package com.example.multichat;

import static com.example.multichat.controller.Controller.STANZENONTROVATE;
import static com.example.multichat.controller.Controller.VEDISTANZEERR;
import static com.example.multichat.controller.Controller.VEDISTANZEOK;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.multichat.adapters.RoomsAdapter;
import com.example.multichat.controller.Controller;
import com.example.multichat.model.Stanza;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements RoomsAdapter.OnStanzaListener {

    private AlertDialog.Builder builder;
    private String[] risposta;
    private ArrayList<Stanza> lista_stanze = new ArrayList<Stanza>();
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.recyclerView);
        TextView errorTextView = findViewById(R.id.errorTextView);
        TextView nessunaStanzaTextView = findViewById(R.id.nessunaStanzaTextView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        Controller controller = new Controller();
        try {
            risposta = controller.vediStanze();
            int codComando = Integer.parseInt(risposta[0]);

            if(codComando == Integer.parseInt(VEDISTANZEOK)) {
                for(int i = 1; i < risposta.length; i++) {
                    String[] dati_stanze = risposta[i].split("\\,");
                    Stanza stanza = new Stanza(Integer.parseInt(dati_stanze[0]), dati_stanze[1], dati_stanze[2]);
                    lista_stanze.add(stanza);
                }
                RoomsAdapter adapter = new RoomsAdapter(lista_stanze, this);
                System.out.println(adapter.getItemCount());
                recyclerView.setAdapter(adapter);
            } else if(codComando == Integer.parseInt(VEDISTANZEERR)) {
                errorTextView.setText("Errore durante il caricamento delle stanze!");
                errorTextView.setVisibility(View.VISIBLE);
            } else if(codComando == Integer.parseInt(STANZENONTROVATE)) {
                nessunaStanzaTextView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    @Override
    public void onStanzaClick(int position) {
        lista_stanze.get(position);
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("nome_stanza", lista_stanze.get(position).getNome_stanza());
        intent.putExtra("room_id", lista_stanze.get(position).getId_stanza());
        startActivity(intent);
    }

}