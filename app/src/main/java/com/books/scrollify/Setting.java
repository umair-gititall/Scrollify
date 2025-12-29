package com.books.scrollify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class Setting extends AppCompatActivity {

    SharedPreferences.Editor editor;
    DatabaseHandler db;
    TextView settingsMessage;
    MaterialButton delete, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_setting);

        editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        db = new DatabaseHandler(getApplicationContext());
        logout = findViewById(R.id.logout);
        delete = findViewById(R.id.delete);

        textPreview textpreview = new textPreview(new String[]{
                "Hey—where are you going?",
                "Just one more chapter?",
                "We were having such a good time",
                "Okay… but come back soon"
        }, findViewById(R.id.SettingsMessage));

        textpreview.run();


        logout.setOnClickListener(v -> {
            editor.putBoolean("logged in", false);
            editor.apply();
            db.logOut();
            Intent i = new Intent(Setting.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        });
        delete.setOnClickListener(v -> {

            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.passwordinput, null);
            TextInputEditText text = dialogView.findViewById(R.id.password_card);

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Delete Account?")
                    .setView(dialogView)
                    .setMessage("Enter Password to Delete Account")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        db.deleteAccount(Objects.requireNonNull(text.getText()).toString(), result ->{
                            if(result)
                            {
                                editor.putBoolean("logged in", false);
                                editor.apply();
                                Intent i = new Intent(Setting.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                            else
                                Toast.makeText(getApplicationContext(), "Invalid Password", Toast.LENGTH_SHORT).show();
                        });
                    }).setNegativeButton("Exit", (dialog, which) -> dialog.dismiss()).show();
        });
    }
}