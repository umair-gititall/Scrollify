package com.books.scrollify;

import android.content.Context;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.viewHolder> {
    List<Book> Books = new ArrayList<>();
    DatabaseHandler db;
    FragmentManager manager;
    public RVAdapter(List<Book> books, Context context, FragmentManager manager) {
        Books = books;
        db = new DatabaseHandler(context);
        this.manager = manager;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookplaceholder, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.Book.setImageResource(Books.get(position).Image);
        holder.BookBackground.setImageResource(Books.get(position).Image);
        holder.Title.setText(Books.get(position).Title);
        holder.AuthorName.setText("By " + Books.get(position).AuthorName);
        holder.ISBN.setText(Books.get(position).ISBN);
        holder.Year.setText(Integer.toString(Books.get(position).Year));

        holder.Delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                db.deleteBook(Books.get(holder.getAdapterPosition()).ISBN);
                PreferenceManager.getDefaultSharedPreferences(v.getContext()).edit().putBoolean("updated", true).apply();
                Books.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                return true;
            }
        });

        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Hold to Delete Book", Toast.LENGTH_SHORT).show();
            }
        });
        holder.Edit.setOnClickListener(v ->{
            manager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.frame, new AddBook(Books.get(holder.getAdapterPosition())))
                    .addToBackStack("book")
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return Books.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView Book, BookBackground;
        TextView Title, AuthorName, ISBN, Year;
        MaterialButton Delete, Edit;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            Book = itemView.findViewById(R.id.book);
            BookBackground = itemView.findViewById(R.id.bookBackground);
            Title = itemView.findViewById(R.id.bookName);
            AuthorName = itemView.findViewById(R.id.authorName);
            ISBN = itemView.findViewById(R.id.ISBN);
            Year = itemView.findViewById(R.id.year);
            Delete = itemView.findViewById(R.id.delete);
            Edit = itemView.findViewById(R.id.edit);
        }
    }

}
