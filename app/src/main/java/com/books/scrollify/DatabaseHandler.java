package com.books.scrollify;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DatabaseHandler {
    private final Context context;
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore store;
    private final DatabaseReference db;
    private final HashMap<String, Object> data = new HashMap<>();

    public DatabaseHandler(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        store = FirebaseFirestore.getInstance();
        db = FirebaseDatabase.getInstance().getReference();
    }

    public interface userCreated {
        void onCreated(boolean result);
    }

    public void createUser(String username, String email, String password, String dob, userCreated callback) {
        checkUsernameExists(username, result -> {
            if (result) {
                Toast.makeText(context, "Username Already Exists", Toast.LENGTH_SHORT).show();
                callback.onCreated(false);
                return;
            }
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(result2 -> {

                if (!result2.isSuccessful()) {
                    Toast.makeText(context, Objects.requireNonNull(result2.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    callback.onCreated(false);
                    return;
                }

                callback.onCreated(true);
                String uid = Objects.requireNonNull(result2.getResult().getUser()).getUid();
                user u = new user(username, dob, email);
                db.child("USERS").child(uid).setValue(u).addOnFailureListener(v -> {
                    Toast.makeText(context, Objects.requireNonNull(v).getMessage(), Toast.LENGTH_SHORT).show();
                });

                data.clear();
                data.put("Email", email);
                db.child("EMAILS").child(username).setValue(data).addOnFailureListener(v -> {
                    Toast.makeText(context, Objects.requireNonNull(v).getMessage(), Toast.LENGTH_SHORT).show();
                });
            });
        });

    }

    public interface loggedIn {
        void onCheck(Boolean result);
    }

    public void login(String username, String password, loggedIn output) {
        db.child("EMAILS").child(username).child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String EMAIL = snapshot.getValue(String.class);
                if (EMAIL != null) {
                    mAuth.signInWithEmailAndPassword(EMAIL, password).addOnSuccessListener(result -> {
                        Toast.makeText(context, "Logged In SuccessFully", Toast.LENGTH_SHORT).show();
                        output.onCheck(true);
                    }).addOnFailureListener(result -> {
                        Toast.makeText(context, "Invalid Password", Toast.LENGTH_SHORT).show();
                        output.onCheck(false);
                    });
                } else {
                    output.onCheck(false);
                    Toast.makeText(context, "Email Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnFailureListener(result -> {
            Toast.makeText(context, result.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void deleteAccount(String email) {
    }

    public interface checkUser {
        void onCheck(Boolean result);
    }

    public void checkUsernameExists(String username, checkUser callback) {
        db.child("EMAILS").child(username).child("Email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String EMAIL = snapshot.getValue(String.class);
                callback.onCheck(EMAIL != null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onCheck(false);
            }
        });
    }

    public void addBook(Book book) {
        assert mAuth.getCurrentUser() != null;
        store.collection(mAuth.getCurrentUser().getUid()).document(book.ISBN).set(book).addOnCompleteListener(result -> {
            if (!result.isSuccessful())
                Toast.makeText(context, Objects.requireNonNull(result.getException()).getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public interface getBooks {
        void get(List<Book> list);
    }

    public void getBooks(getBooks callback) {
        store.collection(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(result -> {
            if (!result.isSuccessful()) {
                Toast.makeText(context, Objects.requireNonNull(result.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                callback.get(new ArrayList<>());
                return;
            }
            List<Book> list = new ArrayList<>();

            for (QueryDocumentSnapshot d : result.getResult()) {
                Book book = d.toObject(Book.class);
                list.add(book);
            }
            callback.get(list);
        });
    }

    public static class user {
        public String username, dob, email;

        public user() {
        }

        public user(String username, String dob, String email) {
            this.username = username;
            this.dob = dob;
            this.email = email;
        }
    }

}
