package com.books.scrollify;

import android.animation.Animator;
import android.animation.ValueAnimator;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Signup#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Signup extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Signup() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Signup.
     */
    // TODO: Rename and change types and number of parameters
    public static Signup newInstance(String param1, String param2) {
        Signup fragment = new Signup();
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
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    Fragment frag;
    FragmentManager manager;
    FragmentTransaction transaction;
    MaterialButton SignUp, SignIn;
    TextInputEditText username, email, password, dob;
    TextView forget;
    DatabaseHandler databaseHandler;
    DatePicker datePicker;
    Calendar cal;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SignUp = view.findViewById(R.id.signup);
        SignIn = view.findViewById(R.id.signin);
        datePicker = view.findViewById(R.id.calender);
        username = view.findViewById(R.id.username);
        cal = Calendar.getInstance();
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        forget = view.findViewById(R.id.forget);
        dob = view.findViewById(R.id.dob);
        databaseHandler = new DatabaseHandler(getContext());

        dob.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (dob.isFocused()) {
                    dob.clearFocus();
                    datePicker.setVisibility(View.VISIBLE);
                    datePicker.setElevation(30);
                    datePicker.animate().translationY(-40);

                    datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            datePicker.animate().translationY(30);
                            datePicker.setElevation(-1);
                            datePicker.setVisibility(View.GONE);
                            StringBuilder date = new StringBuilder();
                            date.append(dayOfMonth);
                            date.append("/");
                            date.append((monthOfYear + 1));
                            date.append("/");
                            date.append(year);
                            dob.setText(date);
                        }
                    });
                }
            }
        });

        SignUp.setOnClickListener(v -> {
            String USERNAME = Objects.requireNonNull(username.getText()).toString();
            String EMAIL = Objects.requireNonNull(email.getText()).toString();
            String PASSWORD = Objects.requireNonNull(password.getText()).toString();
            String DOB = Objects.requireNonNull(dob.getText()).toString();

            if (USERNAME.isEmpty() || EMAIL.isEmpty() || PASSWORD.isEmpty() || DOB.isEmpty()) {
                Toast.makeText(getContext(), "Enter Valid Data", Toast.LENGTH_SHORT).show();
                return;
            }
            databaseHandler.createUser(USERNAME, EMAIL, PASSWORD, DOB, result -> {
                if(result) {
                    PreferenceManager.getDefaultSharedPreferences(getContext())
                            .edit().putBoolean("logged in", true).apply();
                    Intent i = new Intent(getActivity(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }
            });
        });
        SignIn.setOnClickListener(v ->{
            frag = new Login();
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