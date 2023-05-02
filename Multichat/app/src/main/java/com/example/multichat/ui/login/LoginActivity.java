package com.example.multichat.ui.login;

import android.app.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multichat.HomeActivity;
import com.example.multichat.R;
import com.example.multichat.RegistrazioneActivity;
import com.example.multichat.databinding.ActivityRegistrazioneBinding;
import com.example.multichat.ui.login.LoginViewModel;
import com.example.multichat.ui.login.LoginViewModelFactory;
import com.example.multichat.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private androidx.appcompat.app.AlertDialog.Builder builder;
    private Button btnL;
    private TextView tvR;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.btnLogin;
        final ProgressBar loadingProgressBar = binding.loading;

        tvR = (TextView) findViewById(R.id.textView_registrazione);
        tvR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityRegistrazione();
            }
        });

        btnL = (Button) findViewById(R.id.btn_login);
        btnL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setMessage("Login effettuato con successo!")
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

    public void openActivityRegistrazione(){
        Intent intentR = new Intent(this, RegistrazioneActivity.class);
        startActivity(intentR);
    }

    public void openActivityHome(){
        Intent intentH = new Intent(this, HomeActivity.class);
        startActivity(intentH);
    }

    @Override
    public void onBackPressed() {
        builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("Sei sicuro di voler uscire?")
                .setCancelable(true)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
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