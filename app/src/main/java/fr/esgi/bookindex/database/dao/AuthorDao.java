package fr.esgi.bookindex.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import fr.esgi.bookindex.database.entities.Author;

@Dao
public interface AuthorDao {

    @Query("SELECT * FROM authors")
    public LiveData<List<Author>> getAuthors();

    @Query("SELECT * FROM authors WHERE id = :authorId LIMIT 1")
    public LiveData<List<Author>> getAuthorById(final int authorId);

    @Query("SELECT COUNT(*) FROM authors")
    public int authorsCount();

    @Update
    public void updateAuthors(final Author... authors);

    @Delete
    public void deleteAuthors(final Author... authors);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAuthors(final Author... authors);

}
