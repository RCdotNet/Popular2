package se.rcdotnet.udacity.pop1;


// This Activity shows the details of the selected movie.


import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import se.rcdotnet.udacity.pop1.database.MoviesContract;

import static android.support.v4.graphics.drawable.DrawableCompat.setTint;

public class DetailActivity extends AppCompatActivity implements ReviewAdapter.ReviewAdapterClickHandler,
        VideoAdapter.VideoAdapterClickHandler {
    // Declare the activitis public variables.
    private static final String PICTURE_BASE_URI ="http://image.tmdb.org/t/p/w185/" ;
    ImageView mImageView;
    TextView mTitle, mRelease,mSynopsis,mRating;
    VideoList videoList;
    ReviewList reviewList;
    RecyclerView mReviewRecycler;
    RecyclerView mVideoRecycler;
    ReviewAdapter mReviewAdapter;
    RecyclerView.LayoutManager mReviewLayoutManager;
    VideoAdapter mVideoAdpeter;
    RecyclerView.LayoutManager mVideoLayoutManager;
    Button mFavorit;
    Drawable iconFavorit;
    Parcelable reviewsState;
    Parcelable videosState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize the activity and the variables,
        // get the movie details from the starting intent, stored as a parcelable MovieListItem object,
        // and populate te UI. The transition framework animates the poster image into the final place if we are on > L
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mImageView = (ImageView) findViewById(R.id.thumb);
        mTitle = (TextView) findViewById(R.id.title);
        mRelease = (TextView) findViewById(R.id.released);
        mSynopsis = (TextView) findViewById(R.id.synopsis);
        mRating = (TextView) findViewById(R.id.rating);
        mFavorit = (Button) findViewById(R.id.favorit_button);
        mReviewRecycler = (RecyclerView) findViewById(R.id.reviewRecycler);
        mReviewAdapter = new ReviewAdapter(this,null,this);
        mReviewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mReviewRecycler.setAdapter(mReviewAdapter);
        mReviewRecycler.setLayoutManager(mReviewLayoutManager);
        mVideoRecycler = (RecyclerView) findViewById(R.id.videosRecycler);
        mVideoAdpeter = new VideoAdapter(this,null,this);
        mVideoLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mVideoRecycler.setAdapter(mVideoAdpeter);
        mVideoRecycler.setLayoutManager(mVideoLayoutManager);
        String poster_path="";
        MovieListItem list;
        Intent startIntent;
        startIntent=getIntent();
        list=(MovieListItem) startIntent.getExtras().getParcelable("movie");
        ViewCompat.setTransitionName(mImageView, "main");
        poster_path=list.posterPath;
        Uri pictureUri = Uri.parse(PICTURE_BASE_URI+poster_path).buildUpon().build();
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener())
        {
            // If we're running on Lollipop and we have added a listener to the shared element
            // transition, load the thumbnail. The listener will load the full-size image when
            // the transition is complete.
        //    loadThumbnail();
            Picasso.get()
                    .load(pictureUri)
                    .noFade()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(mImageView);
        }
//        else {
//            // If all other cases we should just load the full-size image now
//            Picasso.get()
//                    .load(pictureUri)
//                    .noFade()
//                    .noPlaceholder()
//                    .into(mImageView);
//        }
        mTitle.setText(list.getTitle());
        mRelease.setText(list.getReleaseDate());
        mSynopsis.setText(list.getOwerview());
        mRating.setText(getResources().getString(R.string.rating)+String.valueOf(list.getVoteAvarage()));
        AsyncTask <String,Void,ReviewList> reviews= new loadReviews();
        reviews.execute(String.valueOf(list.getId()));
        AsyncTask <String,Void,VideoList> videos= new loadVideos();
        videos.execute(String.valueOf(list.getId()));
        // Check our ContentProvider, if the actual movie belongs to favorits
        iconFavorit = getResources().getDrawable(R.drawable.ic_heart);
        int favorit = (getContentResolver().query(MoviesContract.movies.MOVIES_CONTENT_URI,new String [] {String.valueOf(list.getId())},
                "id="+String.valueOf(list.getId()),null,null)).getCount();
        if ( favorit >0 ) {
            // yes it does
            mFavorit.setTag(true);  // indicating the state for the onClick
            setTint(iconFavorit,Color.RED);
            mFavorit.setBackground(iconFavorit);
        }
        else {
            mFavorit.setTag(false);
            setTint(iconFavorit,Color.LTGRAY);
            mFavorit.setBackground(iconFavorit);
        }
        final Integer id = list.getId();
        final ContentValues cv = new ContentValues();
        cv.put(String.valueOf(MoviesContract.movies.COLUMN_ID_NAME),id);
        mFavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if ((boolean)v.getTag()){
                        // the movie belongs to favrites, delete it from them
                        Uri uri = MoviesContract.movies.MOVIES_CONTENT_URI;
                        uri = uri.buildUpon().appendPath(String.valueOf(id)).build();
                        getContentResolver().delete(uri,null,null);
                        mFavorit.setTag(false);
                        setTint(iconFavorit,Color.LTGRAY);
                        mFavorit.setBackground(iconFavorit);
                    }
                    else{
                     // Not favorit, do make it
                        getContentResolver().insert(MoviesContract.movies.MOVIES_CONTENT_URI,cv);
                        mFavorit.setTag(true);  // indicating the state for the onClick
                        setTint(iconFavorit,Color.RED);
                        mFavorit.setBackground(iconFavorit);

                    }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // saving the RecyclerView's state
        outState.putParcelable("Reviews",mReviewLayoutManager.onSaveInstanceState());
        outState.putParcelable("Videos",mVideoLayoutManager.onSaveInstanceState());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //Restoring the states for using laater when the data is populated.
        reviewsState = savedInstanceState.getParcelable("Reviews");
        videosState = savedInstanceState.getParcelable("Videos");
    }

    private boolean addTransitionListener() {
        Transition transition;
        transition=null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            transition = getWindow().getSharedElementEnterTransition();
        }

        if (transition != null) {
            // There is an entering shared element transition so add a listener to it
            transition.addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    // As the transition has ended, we can now load the full-size image
          //          loadFullSizeImage();
//                    Picasso.with(mImageView.getContext())
//                            .load(list.)
//                            .into(mImageView);

                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionStart(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionCancel(Transition transition) {
                    // Make sure we remove ourselves as a listener
                    transition.removeListener(this);
                }

                @Override
                public void onTransitionPause(Transition transition) {
                    // No-op
                }

                @Override
                public void onTransitionResume(Transition transition) {
                    // No-op
                }
            });
            return true;
        }

        // If we reach here then we have not added a listener
        return false;
    }



    private class loadReviews extends AsyncTask<String,Void,ReviewList>{

        @Override
        protected ReviewList doInBackground(String... strings) {
            JsonParser parser = new JsonParser();
            return parser.ParseReviewString(NetworkU.getReviews(strings[0]));
        }

        @Override
        protected void onPostExecute(ReviewList s) {
            super.onPostExecute(s);
            reviewList = s;
            mReviewAdapter.SwapData(s);
            if (reviewsState !=null) mReviewLayoutManager.onRestoreInstanceState(reviewsState);
        }
    }
    private class loadVideos extends AsyncTask<String,Void,VideoList>{

        @Override
        protected VideoList doInBackground(String... strings) {
            JsonParser parser = new JsonParser();
            return parser.parseMoviesString(NetworkU.getVideos(strings[0]));
        }

        @Override
        protected void onPostExecute(VideoList s) {
            super.onPostExecute(s);
            videoList = s;
            mVideoAdpeter.SwapData(s);
            if (videosState != null) mVideoLayoutManager.onRestoreInstanceState(videosState);
        }
    }
    @Override
    public void onClick(Uri lookup) {

    }
}
