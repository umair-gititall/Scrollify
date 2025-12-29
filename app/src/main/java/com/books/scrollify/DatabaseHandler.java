package com.books.scrollify;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.firestore.auth.User;

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
                    Toast.makeText(context, "Account Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void resetPassword(String email) {

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(result -> {
            if(!result.isSuccessful())
                Toast.makeText(context,"Email Not Found", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context,"Password Reset Email Sent", Toast.LENGTH_SHORT).show();

        });
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

    public interface bookAdded{
        void added(Boolean status);
    }
    public void addBook(Book book, bookAdded callback) {
        assert mAuth.getCurrentUser() != null;
        store.collection(mAuth.getCurrentUser().getUid()).document(book.ISBN).get().addOnSuccessListener(result -> {

            if (result.exists()) {
                Toast.makeText(context, "ISBN Already Exists", Toast.LENGTH_SHORT).show();
                callback.added(false);
                return;
            }
            assert mAuth.getCurrentUser() != null;
            store.collection(mAuth.getCurrentUser().getUid()).document(book.ISBN).set(book).addOnCompleteListener(result2 -> {
                if (!result2.isSuccessful()) {
                    Toast.makeText(context, Objects.requireNonNull(result2.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    callback.added(false);
                    return;
                }
                callback.added(true);
            });
        });
    }

    public interface getBooks {
        void get(List<Book> list);
    }

    public void getBooks(getBooks callback) {
        assert mAuth.getCurrentUser() != null;
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

    public void deleteBook(String ISBN) {
        assert mAuth.getCurrentUser() != null;
        store.collection(mAuth.getCurrentUser().getUid()).document(ISBN).get().addOnSuccessListener(result -> {
            result.getReference().delete();
            Toast.makeText(context, "Deleted Book: " + result.get("Title"), Toast.LENGTH_SHORT).show();
        });
    }

    public void updateBook(Book book) {
        assert mAuth.getCurrentUser() != null;
        store.collection(mAuth.getCurrentUser().getUid()).document(book.ISBN).set(book).addOnCompleteListener(result -> {
            if (!result.isSuccessful())
                Toast.makeText(context, Objects.requireNonNull(result.getException()).getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public interface deleteStatus {
        void status(Boolean s);
    }

    public void deleteAccount(String password, deleteStatus callback) {
        FirebaseUser User = mAuth.getCurrentUser();

        if (User != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(User.getEmail()), password);
            User.reauthenticate(credential).addOnCompleteListener(result -> {
                if (!result.isSuccessful()) {
                    Toast.makeText(context, Objects.requireNonNull(result.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    callback.status(false);
                    return;
                }
                store.collection(User.getUid()).get().addOnSuccessListener(result2 -> {
                    WriteBatch batch  = store.batch();
                    for (DocumentSnapshot d : result2.getDocuments())
                        batch.delete(d.getReference());

                    batch.commit().addOnCompleteListener(result3 ->{
                        if(result3.isSuccessful()) {
                            db.child("USERS").child(User.getUid()).child("username").get().addOnSuccessListener(result4 -> {
                                db.child("EMAILS").child(Objects.requireNonNull(result4.getValue(String.class))).setValue(null);
                                db.child("USERS").child(User.getUid()).setValue(null);
                                User.delete();
                            });
                            callback.status(true);
                        }
                        else
                            Toast.makeText(context, Objects.requireNonNull(result3.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    });
                });
            });
        }
    }

    public void logOut() {
        mAuth.signOut();
    }
}

