package com.books.scrollify;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class StartupPage extends AppCompatActivity {
    ImageView preview;
    TextView textbox;
    MaterialButton[] btns = new MaterialButton[2];
    int totalImages = 8, totaltexts = 3;
    int[] imageData = new int[totalImages];
    String[] textData = new String[]{"Let's Get Started...", "Let's Play this Game...", "Abdullah is here..."};
    int imageLoad = 0, textLoad = 0, loadedString = 0;
    StringBuilder builder = new StringBuilder();
    Boolean controller = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_startup_page);

        preview = findViewById(R.id.preview);
        textbox = findViewById(R.id.textbox);
        btns[0] = findViewById(R.id.login);
        btns[1] = findViewById(R.id.signup);
        imageData[0] = R.drawable.a1;
        imageData[1] = R.drawable.a2;
        imageData[2] = R.drawable.a3;
        imageData[3] = R.drawable.a4;
        imageData[4] = R.drawable.a5;
        imageData[5] = R.drawable.a6;
        imageData[6] = R.drawable.a7;
        imageData[7] = R.drawable.a8;

        preview.post(new Runnable() {
            @Override
            public void run() {
                preview.setImageResource(imageData[(++imageLoad) % totalImages]);
                preview.postDelayed(this, 3000);
            }
        });
        textbox.post(new Runnable() {
            @Override
            public void run() {
                if (controller) clear();
                else write();
                textbox.postDelayed(this, 70);
            }
        });

        for (MaterialButton btn : btns)
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(StartupPage.this, Accounts.class);
                    i.putExtra("mode", btn.getId() == R.id.login);
                    startActivity(i);
                }
            });
    }

    public void write() {
        builder.append(textData[loadedString].charAt(textLoad++));
        textbox.setText(builder + "|");
        if (textLoad == textData[loadedString].length()) {
            controller = true;
        }
    }

    public void clear() {
        if (textLoad == 1) {
            controller = false;
            loadedString = (++loadedString) % totaltexts;
        }
        builder.setLength(builder.length() - 1);
        textbox.setText(builder + "|");
        textLoad--;
    }
}