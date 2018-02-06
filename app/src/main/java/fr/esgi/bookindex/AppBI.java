package fr.esgi.bookindex;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

import fr.esgi.bookindex.database.AppDatabase;

/**
 * Classe init application (before activities)
 */
public class AppBI extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        AppDatabase.getInstance(this);
    }

    @Override
    public void onTerminate() {
        AppDatabase.destroyInstance();;
        super.onTerminate();
    }
}
