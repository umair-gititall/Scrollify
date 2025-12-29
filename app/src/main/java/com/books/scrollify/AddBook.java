package com.books.scrollify;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddBook#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBook extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Book book;


    public AddBook(Book book) {
        this.book = book;
    }

    public AddBook() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBook.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBook newInstance(String param1, String param2) {
        AddBook fragment = new AddBook();
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
        return inflater.inflate(R.layout.fragment_add_book, container, false);
    }

    TextInputEditText Title, Author, ISBNn, Year;
    TextView title, author, ISBN, year;
    MaterialButton apply;
    DatabaseHandler db;
    Boolean Control = true;
    SharedPreferences prefs;
    int image;
    int[] images = new int[]{
            R.drawable.a1,
            R.drawable.a2,
            R.drawable.a3,
            R.drawable.a4,
            R.drawable.a5,
            R.drawable.a6,
            R.drawable.a7,
            R.drawable.a8};

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Title = view.findViewById(R.id.Title);
        Author = view.findViewById(R.id.AuthorName);
        ISBNn = view.findViewById(R.id.ISBNn);
        Year = view.findViewById(R.id.Year);

        title = view.findViewById(R.id.bookName);
        author = view.findViewById(R.id.authorName);
        ISBN = view.findViewById(R.id.ISBN);
        year = view.findViewById(R.id.year);

        apply = requireActivity().findViewById(R.id.add);
        db = new DatabaseHandler(getContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());

        image = prefs.getInt("images", 0);
        imagePreview temp = new imagePreview(new int[]{images[image]}, view.findViewById(R.id.bookBackground));
        temp.load();

        if (book != null) {
            Title.setText(book.Title);
            title.setText(book.Title);

            Author.setText(book.AuthorName);
            author.setText(book.AuthorName);

            ISBNn.setText(book.ISBN);
            ISBN.setText(book.ISBN);
            ISBNn.setEnabled(false);

            Year.setText(String.valueOf(book.Year));
            year.setText(String.valueOf(book.Year));
            temp.images[0] = book.Image;
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                if (!Objects.requireNonNull(Title.getText()).isEmpty())
                    title.setText(Title.getText());
                else
                    title.setText("Book Name");
                if (!Objects.requireNonNull(Author.getText()).isEmpty())
                    author.setText(Author.getText());
                else
                    author.setText("By Author");
                if (!Objects.requireNonNull(ISBNn.getText()).isEmpty())
                    ISBN.setText(ISBNn.getText());
                else
                    ISBN.setText("ISBN");
                if (!Objects.requireNonNull(Year.getText()).isEmpty())
                    year.setText(Year.getText());
                else
                    year.setText("Publication Year");
                title.postDelayed(this, 100);
            }
        });

        apply.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (apply.getText().equals("Save") && book == null) {
                    if (Title.getText().toString().isEmpty() || Author.getText().toString().isEmpty()
                            || ISBNn.getText().toString().isEmpty() || Year.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Fill in the Fields", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    db.addBook(new Book(Title.getText().toString(),
                            Author.getText().toString(),
                            ISBNn.getText().toString(),
                            Integer.valueOf(Year.getText().toString()), images[image]), result ->{
                        if(result) {
                            prefs.edit().putInt("images", (++image) % 8).apply();
                            prefs.edit().putBoolean("updated", true).apply();
                            getParentFragmentManager().popBackStack();
                        }
                    });
                } else {
                    if (Title.getText().toString().isEmpty() || Author.getText().toString().isEmpty()
                            || ISBNn.getText().toString().isEmpty() || Year.getText().toString().isEmpty()) {
                        Toast.makeText(getContext(), "Fill in the Fields", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    book.Title = Title.getText().toString();
                    book.AuthorName = Author.getText().toString();
                    book.ISBN = ISBNn.getText().toString();
                    book.Year = Integer.valueOf(Year.getText().toString());
                    db.updateBook(book);
                    prefs.edit().putBoolean("updated", true).apply();
                    getParentFragmentManager().popBackStack();
                }

                return true;
            }
        });

        getParentFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (Control) {
                    apply.setText("Save");
                    Control = false;
                } else
                    apply.setText("Add Book");
            }
        });
    }
}