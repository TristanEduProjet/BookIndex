package fr.esgi.bookindex.ui;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.support.BypassRestricted;

import org.threeten.bp.Instant;

import java.io.IOException;

import fr.esgi.bookindex.AppDatabase;
import fr.esgi.bookindex.BookFragment;
import fr.esgi.bookindex.GDrive_export;
import fr.esgi.bookindex.R;
import fr.esgi.bookindex.About;
import fr.esgi.bookindex.dummy.DummyContent;
import fr.esgi.bookindex.entities.Author;
import fr.esgi.bookindex.entities.Book;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
                                                               BookFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main_activity);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        this.setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ((NavigationView) findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);

        this.testRetroLambda();
        Log.d("MainActivity", "It's " + Instant.now());

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "book-database")
                .allowMainThreadQueries()
                .build();

        Author VH = db.createAuthorInDB("Victor", "Hugo");
        Author HB = db.createAuthorInDB("Henri", "Beyle");

        db.createBookInDB("Les Misérables", VH, "Ceci est une description");
        db.createBookInDB("Claude Gueux", VH, "Ceci est une description");
        db.createBookInDB("Les Rouge et le Noir", HB, "Ceci est une description");



    }

    @Override
    public void onBackPressed() {
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        if (menu instanceof MenuBuilder)
            BypassRestricted.menuSetOptionalIconsVisible((MenuBuilder) menu, true);
        this.getMenuInflater().inflate(R.menu.main_actionbar_menu, menu); // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_share:
                return true;
            case R.id.action_export:{
                Intent myIntent = new Intent(MainActivity.this, GDrive_export.class);
                MainActivity.this.startActivity(myIntent);
                return true;
            }
            case R.id.action_import:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        switch(item.getItemId()) {
            case R.id.nav_search:
                break;
            case R.id.nav_bookloan:
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_about:
                Intent myIntent = new Intent(MainActivity.this, About.class);
                MainActivity.this.startActivity(myIntent);
                break;
            case R.id.nav_help:
                break;
        }

        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer(GravityCompat.START);
        return true;
    }

    private void testRetroLambda() {
        final int x = 42;
        this.runOnUiThread(() -> Log.d(this.getClass().getSimpleName(),  "from lambda : x="+x));
        try {
            if(this.getLocalClassName().startsWith("Main"))
                throw new IOException();
            else
                throw new InterruptedException();
        } catch (InterruptedException | IOException e) {
            Log.e(this.getClass().getSimpleName(), "Exception multi-catch : ", e);
        }
    }

    @Override
    public void onListFragmentInteraction(Book book) {

    }
}
