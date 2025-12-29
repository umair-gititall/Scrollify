package com.books.scrollify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    SharedPreferences prefs;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        i = new Intent(MainActivity.this, HomePage.class);

        if (!prefs.getBoolean("logged in", false))
            i = new Intent(MainActivity.this, StartupPage.class);
        startActivity(i);
        finish();
    }
}