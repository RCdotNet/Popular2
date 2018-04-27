package se.rcdotnet.udacity.pop1;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Laszlo_HP_Notebook on 2018-03-20.
 * This is the adapter for the RecyclerView
 */
public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> {

    //Declare members
    private static final String PICTURE_BASE_URI ="http://image.tmdb.org/t/p/w185/" ;
    private Cursor mCursor = null;
    private Context mContext;
    private MovieList mMovieList = null;
    final private MoviesListClickHandler mClickHandler;

    public interface MoviesListClickHandler {
        void onClick(View item);
    }

    public MoviesListAdapter(Context context, Cursor cursor, MoviesListClickHandler listener) {
        //Constructs the adapter and sets the members.
        mCursor = cursor;
        mContext = context;
        mClickHandler = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // ViewHolder creation
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View retView = inflater.inflate(R.layout.movieitem, parent, false);
        return new MovieViewHolder(retView);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        // Bind the data into the holder, in this case the photo correspons the movie at position
        // Useing Picasso to make the data fetching and populating the ImageView in background
        Uri pictureUri = Uri.parse(PICTURE_BASE_URI+mMovieList.movies.get(position).posterPath).buildUpon().build();
        Picasso.get()
                .load(pictureUri)
                .into(holder.mImageView);
        // set the items tag to hold a copy of the MovieListItem object for further reference (in click handler)
        holder.itemView.setTag(mMovieList.movies.get(position));
    }

    public void setData(MovieList data){
        // Pass in data to the adapter, and notify it that the data changed. It causes to refreshes it with the new data.
        mMovieList = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // Returns the elements count
        if (mMovieList == null) return 0;
        else return mMovieList.movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
       // Declaring the ImageView variable for the viewholder
        ImageView mImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            // set the variable point the itemView's ImageView and set onClicListener on the itemView received
            mImageView=(ImageView) itemView.findViewById(R.id.picture);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // handle the Click event on this paricular View.
            // Call the handler and pass in the actual movie.
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(v);
        }
    }

}
