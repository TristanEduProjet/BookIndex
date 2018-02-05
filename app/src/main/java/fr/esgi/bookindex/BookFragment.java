package fr.esgi.bookindex;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.esgi.bookindex.database.DatabaseUtils;
import fr.esgi.bookindex.dummy.DummyContent;
import fr.esgi.bookindex.dummy.DummyContent.DummyItem;
import fr.esgi.bookindex.entities.Book;

import java.util.Arrays;
import java.util.List;

public class BookFragment extends Fragment {

    //private static final String ARG_COLUMN_COUNT = "column-count";
    //private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private AppDatabase db = DatabaseUtils.initialize(this.getContext().getApplicationContext());

    /**
     * Constructeur obligatoire pour le changement d'orientation
     */
    public BookFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new MyBookRecyclerViewAdapter(Arrays.asList(db.bookDao().getAllBooks()), mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Must be implemented by activities using this fragment
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Book book);
    }
}
