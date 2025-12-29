package com.books.scrollify;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    Fragment frag;
    FragmentManager manager;
    FragmentTransaction transaction;
    MaterialButton SignIn, SignUp;
    TextInputEditText username, password;
    TextView forget;
    DatabaseHandler db;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignIn = view.findViewById(R.id.signin);
        SignUp = view.findViewById(R.id.signup);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        forget = view.findViewById(R.id.forget);
        db = new DatabaseHandler(getContext());

        SignIn.setOnClickListener(v -> {

            String USERNAME = Objects.requireNonNull(username.getText()).toString();
            String PASSWORD = Objects.requireNonNull(password.getText()).toString();

            db.login(USERNAME, PASSWORD, result ->{
                if(result)
                {
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putBoolean("logged in", true).apply();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
        });
        SignUp.setOnClickListener(v ->{
            frag = new Signup();
            manager = requireActivity().getSupportFragmentManager();
            transaction = manager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.frame, frag);
            transaction.commit();
        });
        forget.setOnClickListener(v ->{
            frag = new ForgetPassword();
            manager = requireActivity().getSupportFragmentManager();
            transaction = manager.beginTransaction();
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            transaction.replace(R.id.frame, frag);
            transaction.commit();
        });
    }
}