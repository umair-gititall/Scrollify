package com.books.scrollify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Accounts extends AppCompatActivity {

    imagePreview imagepreview;
    FrameLayout frame;
    Fragment frag;
    FragmentManager manager;
    FragmentTransaction transaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accounts);

        imagepreview = new imagePreview( new int[]{
                R.drawable.a1,
                R.drawable.a2,
                R.drawable.a3,
                R.drawable.a4,
                R.drawable.a5,
                R.drawable.a6,
                R.drawable.a7,
                R.drawable.a8
        }, findViewById(R.id.preview));
        imagepreview.load();

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