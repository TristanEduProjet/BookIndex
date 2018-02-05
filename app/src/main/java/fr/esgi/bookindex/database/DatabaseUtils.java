package fr.esgi.bookindex.database;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import fr.esgi.bookindex.AppDatabase;

public final class DatabaseUtils {
    private DatabaseUtils(){}

    public static AppDatabase initialize(final @NonNull Context appContext){
        return Room.databaseBuilder(appContext, AppDatabase.class, "book-database")
                //.allowMainThreadQueries()
                .build();
    }
}
