package com.books.scrollify;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    RecyclerView recyclerView;
    DatabaseHandler db;
    Book[] books;
    SharedPreferences prefs;
    FragmentManager manager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        db = new DatabaseHandler(getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        manager = getParentFragmentManager();

        if (prefs.getBoolean("updated", false))
            runit();
        else
            savedRunit();

    }

    public void savedRunit() {
        Toast.makeText(getContext(), "saved", Toast.LENGTH_SHORT).show();
        String data = prefs.getString("data", null);

        if (data != null) {
            List<Book> books;
            Gson gson = new Gson();
            Type type = new TypeToken<List<Book>>() {
            }.getType();
            books = gson.fromJson(data, type);
            RVAdapter adapter = new RVAdapter(books, getContext(), manager);
            PagerSnapHelper snapper = new PagerSnapHelper();
            snapper.attachToRecyclerView(recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    public void runit() {
        db.getBooks(result -> {
            Toast.makeText(getContext(), "online", Toast.LENGTH_SHORT).show();
            RVAdapter adapter = new RVAdapter(result, getContext(), manager);
            PagerSnapHelper snapper = new PagerSnapHelper();
            snapper.attachToRecyclerView(recyclerView);
            Gson gson = new Gson();
            String json = gson.toJson(result);
            prefs.edit().putString("data", json).apply();
            prefs.edit().putBoolean("updated", false).apply();
            recyclerView.setAdapter(adapter);
        });
    }
}