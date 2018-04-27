package se.rcdotnet.udacity.pop1;


// This Activity shows the details of the selected movie.


import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize the activity and the variables,
        // get the movie details from the starting intent, stored as a parcelable MovieListItem object,
        // and populate te UI. The transition framework anymates the poster image into the final place if we are on > L
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mImageView = (ImageView) findViewById(R.id.thumb);
        mTitle = (TextView) findViewById(R.id.title);
        mRelease = (TextView) findViewById(R.id.released);
        mSynopsis = (TextView) findViewById(R.id.synopsis);
        mRating = (TextView) findViewById(R.id.rating);
        mReviewRecycler = (RecyclerView) findViewById(R.id.reviewRecycler);
        mReviewAdapter = new ReviewAdapter(this,null,this);
        mReviewLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
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
        }
    }
    @Override
    public void onClick(Uri lookup) {

    }
}
