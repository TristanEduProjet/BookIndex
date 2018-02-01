package fr.esgi.bookindex.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import fr.esgi.bookindex.entities.Author;


@Dao
public interface AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAuthor(Author author);

    @Query("SELECT * FROM authors")
    public Author[] getAllAuthors();

    @Query("SELECT * FROM authors WHERE id = :authorId")
    public Author getAuthorById(int authorId);

    @Update
    public void updateAuthor(Author author);

    @Delete
    public void deleteAuthor(Author author);
}
