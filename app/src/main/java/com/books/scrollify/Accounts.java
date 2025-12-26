package com.books.scrollify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Accounts extends AppCompatActivity {

    FrameLayout frame;
    Fragment frag;
    FragmentManager manager;
    FragmentTransaction transaction;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accounts);

        Intent i = getIntent();
        if(i.getBooleanExtra("mode", true))
            frag = new Login();
        else
            frag = new Signup();
        frame = findViewById(R.id.frame);
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.replace(R.id.frame, frag);
        transaction.commit();
    }
}