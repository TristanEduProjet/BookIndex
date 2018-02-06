package fr.esgi.bookindex.database;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import fr.esgi.bookindex.database.entities.Book;
import fr.esgi.bookindex.databinding.FragmentBookBinding;

/**
 * LiveData RecyclerView Adapter for Books
 */
public class BookFragmentRecyclerViewAdapter extends RecyclerView.Adapter<BookFragmentRecyclerViewAdapter.BookFragmentRecyclerViewHolder> {
    private List<Book> books;

    public BookFragmentRecyclerViewAdapter(final List<Book> books) {
        this.books = books;
    }

    @Override
    public BookFragmentRecyclerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        FragmentBookBinding itemBinding = FragmentBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookFragmentRecyclerViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final BookFragmentRecyclerViewHolder holder, final int position) {
        holder.bind(this.books.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return this.books.size();
    }

    public void setBooks(final List<Book> books) {
        this.books = books;
        this.notifyDataSetChanged();
    }

    static class BookFragmentRecyclerViewHolder extends RecyclerView.ViewHolder {
        FragmentBookBinding binding;

        BookFragmentRecyclerViewHolder(final FragmentBookBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(final String bookTitle) {
            binding.bookTitle.setText(bookTitle);
            binding.executePendingBindings();
        }
    }
}
