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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private ArrayList<String> messages;
    private EditText messageEditText;
    private Button sendButton;
    private ListView messageListView;
    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chat");
        actionBar.setDisplayHomeAsUpEnabled(true);

        messages = new ArrayList<>();
        messageEditText = findViewById(R.id.edit_text_message);
        sendButton = findViewById(R.id.button_send);
        messageListView = findViewById(R.id.list_view_messages);
        messageAdapter = new MessageAdapter(this, messages);
        messageListView.setAdapter(messageAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString().trim();
                if (!message.isEmpty()) {
                    inviaMessaggio(message);
                    messageEditText.setText("");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                openActivityPartecipanti();
                return true;
            default:
                openActivityHome();
                return true;
        }
    }

    public void openActivityPartecipanti(){
        Intent intentP = new Intent(this, PartecipantiActivity.class);
        startActivity(intentP);
    }

    public void openActivityHome(){
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }

    private void inviaMessaggio(String messaggio) {
        messages.add(messaggio);
        messageAdapter.notifyDataSetChanged();
    }
}