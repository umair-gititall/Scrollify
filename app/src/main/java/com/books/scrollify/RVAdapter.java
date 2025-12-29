package com.books.scrollify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.viewHolder> {
    Book[] Books;

    public RVAdapter(Book[] books) {
        this.Books = books;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookplaceholder, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.Book.setImageResource(Books[position].Image);
        holder.BookBackground.setImageResource(Books[position].Image);
        holder.Title.setText(Books[position].Title);
        holder.AuthorName.setText("By " + Books[position].AuthorName);
        holder.ISBN.setText(Books[position].ISBN);
        holder.Year.setText(Integer.toString(Books[position].Year));
    }

    @Override
    public int getItemCount() {
        return Books.length;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView Book, BookBackground;
        TextView Title, AuthorName, ISBN, Year;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            Book = itemView.findViewById(R.id.book);
            BookBackground = itemView.findViewById(R.id.bookBackground);
            Title = itemView.findViewById(R.id.bookName);
            AuthorName = itemView.findViewById(R.id.authorName);
            ISBN = itemView.findViewById(R.id.ISBN);
            Year = itemView.findViewById(R.id.year);
        }
    }

}
