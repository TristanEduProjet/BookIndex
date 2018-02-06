package fr.esgi.bookindex.database;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import fr.esgi.bookindex.database.dao.AuthorDao;
import fr.esgi.bookindex.database.entities.Author;
import fr.esgi.bookindex.database.entities.Book;
import fr.esgi.bookindex.databinding.FragmentBookBinding;

/**
 * LiveData RecyclerView Adapter for Books
 */
public class BookFragmentRecyclerViewAdapter extends RecyclerView.Adapter<BookFragmentRecyclerViewAdapter.BookFragmentRecyclerViewHolder> {
    private List<Book> books;
    private AppDatabase instance;

    public BookFragmentRecyclerViewAdapter(final @NonNull AppDatabase instance, final List<Book> books) {
        this.books = books;
        this.instance = instance;
    }

    @Override
    public BookFragmentRecyclerViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        FragmentBookBinding itemBinding = FragmentBookBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new BookFragmentRecyclerViewHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(final BookFragmentRecyclerViewHolder holder, final int position) {
        final Book book = this.books.get(position);
        try {
            final Integer aid = book.getAuthorId();
            final AuthorDao dao = this.instance.authorDao();
            final LiveData<List<Author>> result = dao.getAuthorById(aid);
            final List<Author> sub = result.getValue();
            try {
                final Author author = sub.get(0);
                holder.bind(book.getTitle(), author.getLastName(), author.getFirstName());
            } catch(final NullPointerException n) { throw new IndexOutOfBoundsException("No author found"); }
        } catch(final IndexOutOfBoundsException e) {
            holder.bind(book.getTitle(), null);
        }
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

        void bind(final String bookTitle, final String authorName, final String authorFirstName) {
            this.bind(bookTitle, authorFirstName+" "+authorName);
        }

        void bind(final String bookTitle, final String authorFullName) {
            binding.bookTitle.setText(bookTitle);
            binding.bookAuthor.setText(authorFullName);
            binding.executePendingBindings();
        }
    }
}
