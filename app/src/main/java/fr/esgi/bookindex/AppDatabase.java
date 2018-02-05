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
    public abstract BookDao bookDao();

    public Author createAuthorInDB(String firstName, String lastName){
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
    }


}
