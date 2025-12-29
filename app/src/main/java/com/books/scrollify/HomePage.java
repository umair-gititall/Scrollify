package com.books.scrollify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;


public class HomePage extends AppCompatActivity {

    Fragment frag;
    FragmentManager manager;
    FragmentTransaction transaction;
    MaterialButton addBook, settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        addBook = findViewById(R.id.add);
        settings = findViewById(R.id.settings);
        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("updated", true);
        editor.putInt("images", 0);
        editor.putString("data", null);
        editor.apply();

        textPreview textpreview = new textPreview(new String[]{
                "One more chapter won’t hurt…",
                "Your next favorite book is here",
                "Ready to get lost in words?",
                "Every book is a new universe",
                "Adventures begin with a page",
                "Choose your story",
                "Read.",
                "Discover.",
                "Repeat.",
                "Silence the world",
                "Open a book",
                "Begin the journey"
        }, findViewById(R.id.message));

        textpreview.run();
        frag = new Home();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.frame, frag);
        transaction.commit();

        addBook.setOnClickListener(v -> {
            if(addBook.getText().equals("Add Book")) {
                frag = new AddBook();
                transaction = manager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.replace(R.id.frame, frag);
                transaction.addToBackStack("book");
                transaction.commit();
            }
            else
                Toast.makeText(getApplicationContext(), "Hold to Save", Toast.LENGTH_SHORT).show();
        });

        settings.setOnClickListener(v ->{
            Intent i = new Intent(HomePage.this, Setting.class);
            startActivity(i);
        });
    }
}