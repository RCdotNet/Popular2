package se.rcdotnet.udacity.pop1;


// This Activity shows the details of the selected movie.


import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
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
    VideoAdapter mVideoAdapter;
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
        mImageView = findViewById(R.id.thumb);
        mTitle = findViewById(R.id.title);
        mRelease = findViewById(R.id.released);
        mSynopsis = findViewById(R.id.synopsis);
        mRating = findViewById(R.id.rating);
        mFavorit = findViewById(R.id.favorit_button);
        mReviewRecycler = findViewById(R.id.reviewRecycler);
        mReviewAdapter = new ReviewAdapter(this,null,this);
        mReviewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mReviewRecycler.setAdapter(mReviewAdapter);
        mReviewRecycler.setLayoutManager(mReviewLayoutManager);
        mVideoRecycler = findViewById(R.id.videosRecycler);
        mVideoAdapter = new VideoAdapter(this,null,this);
        mVideoLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mVideoRecycler.setAdapter(mVideoAdapter);
        mVideoRecycler.setLayoutManager(mVideoLayoutManager);
        String poster_path="";
        MovieListItem list;
        Intent startIntent;
        startIntent=getIntent();
        list = startIntent.getExtras().getParcelable("movie");
        ViewCompat.setTransitionName(mImageView, "main");
        poster_path=list.posterPath;
        Uri pictureUri = Uri.parse(PICTURE_BASE_URI+poster_path).buildUpon().build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && addTransitionListener())
        {
            // Hide the favorit button during animation, the lustener will restore it.
//            mFavorit.setVisibility(View.INVISIBLE);
        }
        Picasso.get()
                .load(pictureUri)
                .noFade()
                .placeholder(R.drawable.ic_launcher_background)
                .into(mImageView);

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
        iconFavorit = DrawableCompat.wrap(iconFavorit);
        int favorit = (getContentResolver().query(MoviesContract.movies.MOVIES_CONTENT_URI,new String [] {String.valueOf(list.getId())},
                "id="+String.valueOf(list.getId()),null,null)).getCount();
        if ( favorit >0 ) {
            // yes it does
            mFavorit.setTag(true);  // indicating the state for the onClick
            DrawableCompat.setTint(iconFavorit.mutate(),Color.RED);
            mFavorit.setBackground(iconFavorit);
        }
        else {
            mFavorit.setTag(false);
            DrawableCompat.setTint(iconFavorit.mutate(),Color.LTGRAY);
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
                        DrawableCompat.setTint(iconFavorit.mutate(),Color.LTGRAY);
                        mFavorit.setBackground(iconFavorit);
                    }
                    else{
                     // Not favorit, do make it
                        getContentResolver().insert(MoviesContract.movies.MOVIES_CONTENT_URI,cv);
                        mFavorit.setTag(true);  // indicating the state for the onClick
                        DrawableCompat.setTint(iconFavorit.mutate(),Color.RED);
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
        //Restoring the states for using later when the data is populated.
        reviewsState = savedInstanceState.getParcelable("Reviews");
        videosState = savedInstanceState.getParcelable("Videos");
    }
//Listener to listen when the animation finishes
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
                    //Remove the listener
                    transition.removeListener(this);
                    // set the favorit button visibility
  //                  mFavorit.setVisibility(View.VISIBLE);
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
            mVideoAdapter.SwapData(s);
            if (videosState != null) mVideoLayoutManager.onRestoreInstanceState(videosState);
        }
    }

    // handle the click events from the Review and Videos adapters.
    @Override
    public void onClick(Object data) {
        if (data.getClass() == VideoListItem.class){
            // show the video in youtube or in a browser
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + ((VideoListItem)data).getKey()));
            // Check if youtube app installed
            if (intent.resolveActivity(getPackageManager()) != null)
                startActivity(intent);
            else
            {
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + ((VideoListItem)data).getKey()));
                startActivity(intent);
            }
        }
        else {
            Intent intent = new Intent(this,ReviewActivity.class);
            intent.putExtra("review", (ReviewItem)data);
            startActivity(intent);
        }
    }
}
