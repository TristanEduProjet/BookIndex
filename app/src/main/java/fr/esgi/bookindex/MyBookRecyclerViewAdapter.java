package fr.esgi.bookindex;

import android.arch.persistence.room.Room;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fr.esgi.bookindex.BookFragment.OnListFragmentInteractionListener;
import fr.esgi.bookindex.database.DatabaseUtils;
import fr.esgi.bookindex.entities.Author;
import fr.esgi.bookindex.entities.Book;

import java.util.List;


public class MyBookRecyclerViewAdapter extends RecyclerView.Adapter<MyBookRecyclerViewAdapter.ViewHolder> {

    private final List<Book> mValues;
    private final OnListFragmentInteractionListener mListener;
    private AppDatabase db = null;

    public MyBookRecyclerViewAdapter(List<Book> books, OnListFragmentInteractionListener listener) {
        mValues = books;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Author[] author = new Author[1];

        if(null != db){
            db = DatabaseUtils.initialize(holder.mView.getContext().getApplicationContext());
        }

        new Thread(() -> {
            author[0] = db.authorDao().getAuthorById(mValues.get(position).getAuthorId());;
        }).start();

        holder.mBook = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTitle());
        holder.mAuthorView.setText(author[0].toString());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mBook);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mAuthorView;
        public Book mBook;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.book_title);
            mAuthorView = (TextView) view.findViewById(R.id.book_author);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + ", by " + mAuthorView.getText() + "'";
        }
    }
}
