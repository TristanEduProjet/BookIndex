package fr.esgi.bookindex.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fr.esgi.bookindex.R;
import fr.esgi.bookindex.database.AppDatabase;
import fr.esgi.bookindex.database.BookFragmentRecyclerViewAdapter;
import fr.esgi.bookindex.database.entities.Book;
import fr.esgi.bookindex.database.viewmodel.BooksViewModel;
import fr.esgi.bookindex.databinding.FragmentBookListBinding;

import java.util.ArrayList;

public class BookFragment extends Fragment {

    //private static final String ARG_COLUMN_COUNT = "column-count";
    //private int mColumnCount = 1;
    ///private OnListFragmentInteractionListener mListener;
    ///private AppDatabase db = DatabaseUtils.initialize(this.getContext().getApplicationContext());

    /**
     * Constructeur obligatoire pour le changement d'orientation
     */
    public BookFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentBookListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_list, container,  false);
        BookFragmentRecyclerViewAdapter recyclerViewAdapter = new BookFragmentRecyclerViewAdapter(AppDatabase.getInstance(this.getContext()), new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        binding.recyclerView.setAdapter(recyclerViewAdapter);
        ViewModelProviders.of(this).get(BooksViewModel.class).getBooks().observe(this, recyclerViewAdapter::setBooks);

        /*View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            //recyclerView.setAdapter(new MyBookRecyclerViewAdapter(db.bookDao().getAllBooks()., mListener));
        }*/
        return binding.getRoot();
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMa binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        BookFragmentRecyclerViewAdapter recyclerViewAdapter = new MainActivityFragmentRecyclerViewAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(recyclerViewAdapter);
        ViewModelProviders.of(this).get(BooksViewModel.class).getBooks().observe(this, recyclerViewAdapter::setBooks);
        return binding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * Must be implemented by activities using this fragment
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Book book);
    }
}
