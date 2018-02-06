package fr.esgi.bookindex.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import fr.esgi.bookindex.database.dao.AuthorDao;
import fr.esgi.bookindex.database.dao.BookDao;
import fr.esgi.bookindex.database.entities.Author;
import fr.esgi.bookindex.database.entities.Book;

@Database(entities = {Book.class, Author.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public static /*synchronized*/ AppDatabase getInstance(final @NonNull Context context) {
        if (INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "BlogPostsDatabase").build();
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public abstract AuthorDao authorDao();
    public abstract BookDao bookDao();

    /*public Author createAuthorInDB(String firstName, String lastName){
        int newAuthorId = this.authorDao().countAllAuthors() + 1;
        Author newAuthor = new Author(newAuthorId, firstName, lastName);
        this.authorDao().insertAuthor(newAuthor);
        return newAuthor;
    }

    public Book createBookInDB(String title, Author author, String description){
        int newBookId = this.bookDao().countAllBooks() + 1;
        Book newBook = new Book(newBookId, title, author.getId(), description);
        this.bookDao().insertBook(newBook);
        return newBook;
    }*/

}
