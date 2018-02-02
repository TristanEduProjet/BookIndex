package fr.esgi.bookindex;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import fr.esgi.bookindex.dao.AuthorDao;
import fr.esgi.bookindex.dao.BookDao;
import fr.esgi.bookindex.entities.Author;
import fr.esgi.bookindex.entities.Book;

@Database(entities = {Book.class, Author.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AuthorDao authorDao();
    public abstract BookDao bookdao();
}
