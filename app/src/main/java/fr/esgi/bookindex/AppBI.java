package fr.esgi.bookindex;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

/**
 * Classe init application (before activities)
 */
public class AppBI extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
    }
}
