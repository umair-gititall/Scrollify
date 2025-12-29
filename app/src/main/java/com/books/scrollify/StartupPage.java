package com.books.scrollify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StartupPage extends AppCompatActivity {
    imagePreview imagepreview;
    textPreview textpreview;
    MaterialButton[] buttons = new MaterialButton[2];
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_startup_page);

        buttons[0] = findViewById(R.id.login);
        buttons[1] = findViewById(R.id.signup);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        imagepreview = new imagePreview(new int[]{
                R.drawable.a1,
                R.drawable.a2,
                R.drawable.a3,
                R.drawable.a4,
                R.drawable.a5,
                R.drawable.a6,
                R.drawable.a7,
                R.drawable.a8
        }, findViewById(R.id.preview));

        textpreview = new textPreview(new String[]{
                "Welcome to your reading space",
                "Stories are waiting for you",
                "Let’s turn the first page"
        }, findViewById(R.id.textbox));

        imagepreview.load();
        textpreview.run();

        for (MaterialButton btn : buttons)
            btn.setOnClickListener(v -> {
                Intent i = new Intent(StartupPage.this, Accounts.class);
                i.putExtra("mode", btn.getId() == R.id.login);
                startActivity(i);
            });
    }

}