package fr.esgi.bookindex.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import fr.esgi.bookindex.entities.Book;


@Dao
public interface BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertBook(Book book);

    @Query("SELECT * FROM books")
    public Book[] getAllBooks();

    @Query("SELECT * FROM books WHERE id = :bookId")
    public Book getBookById(int bookId);

    @Update
    public void updateBook(Book book);

    @Delete
    public void deleteUsers(Book Book);

}
