package fr.esgi.bookindex.database;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import fr.esgi.bookindex.database.entities.Author;
import fr.esgi.bookindex.database.entities.Book;

public final class DatabaseUtils {
    private DatabaseUtils(){}

    public static AppDatabase initialize(final @NonNull Context appContext){
        return Room.databaseBuilder(appContext, AppDatabase.class, "book-database")
                //.allowMainThreadQueries()
                .build();
    }

    public static void populateAsync(final AppDatabase database) {
        new PopulateDbAsync(database).execute();
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {
        private final AppDatabase database;

        PopulateDbAsync(@NonNull final AppDatabase database) {
            this.database = database;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // If the Database is empty, add the initial data.
            if (this.database.bookDao().booksCount() == 0) {
                final List<Book> books = new ArrayList<>();
                books.add(new Book("Blog Post #1"));
                books.add(new Book("Blog Post #2"));
                books.add(new Book("Blog Post #3"));
                this.database.bookDao().insertBooks(books.toArray(new Book[books.size()]));

                final Author VH = new Author(1, "Victor", "Hugo"), HB = new Author(1, "Henri", "Beyle");
                this.database.authorDao().insertAuthors(VH, HB);
                this.database.bookDao().insertBooks(new Book("Les Misérables", VH.getId(), "Ceci est une description"),
                                                    new Book("Claude Gueux", VH.getId(), "Ceci est une description"),
                                                    new Book("Les Rouge et le Noir", HB.getId(), "Ceci est une description"));
            }
            return null;
        }
    }
}
