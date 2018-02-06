package fr.esgi.bookindex.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.List;

import fr.esgi.bookindex.database.entities.Book;

@Dao
public interface BookDao {

    @Query("SELECT * FROM books")
    LiveData<List<Book>> getAllBooks();

    @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
    LiveData<List<Book>> getBookById(final @NonNull Integer bookId);

    @Query("SELECT * FROM books WHERE title LIKE :title LIMIT 1")
    LiveData<List<Book>> getBookByTitle(final @NonNull String title);

    @Query("SELECT COUNT(*) FROM books")
    int booksCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooks(final Book... books);

    @Update
    void updateBooks(final Book... books);

    @Delete
    void deleteBooks(final Book... books);

}
