package com.example.multichat;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CercastanzaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cercastanza);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Cerca Stanza");
    }
}