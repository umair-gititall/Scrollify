package com.books.scrollify;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
    MaterialButton addBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);
        addBook = findViewById(R.id.add);

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
    }
}