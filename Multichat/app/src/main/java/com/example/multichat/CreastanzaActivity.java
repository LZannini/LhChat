package com.example.multichat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CreastanzaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creastanza);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Crea Stanza");
    }
}