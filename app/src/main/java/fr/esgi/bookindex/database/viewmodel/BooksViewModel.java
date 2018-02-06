package fr.esgi.bookindex.database.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import fr.esgi.bookindex.database.AppDatabase;
import fr.esgi.bookindex.database.entities.Book;

/**
 * ViewModel of Book (LiveData)
 */
public class BooksViewModel extends AndroidViewModel {
    private final LiveData<List<Book>> books;

    public BooksViewModel(final @NonNull Application application) {
        super(application);
        this.books = AppDatabase.getInstance(this.getApplication()).bookDao().getAllBooks();
    }

    public LiveData<List<Book>> getBooks() {
        return this.books;
    }
}
