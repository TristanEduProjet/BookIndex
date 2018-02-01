package fr.esgi.bookindex;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import fr.esgi.bookindex.dao.BookDao;
import fr.esgi.bookindex.entities.Book;

@Database(entities = {Book.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BookDao bookdao();
}
