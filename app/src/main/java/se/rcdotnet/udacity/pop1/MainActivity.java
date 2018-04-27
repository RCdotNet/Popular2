package se.rcdotnet.udacity.pop1;

// The main activity

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements MoviesListAdapter.MoviesListClickHandler,
        LoaderManager.LoaderCallbacks<MovieList>,
        SharedPreferences.OnSharedPreferenceChangeListener{
    // Declaring the actvitis global variables
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private MoviesListAdapter mMovielistAdapter;
    private GridLayoutManager mLayoutManager;
    private SharedPreferences mPreferences;
    private String mErrorText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize variables, set up the RecyclerView, start the Loader, register Listener for preference changes
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView=findViewById(R.id.mainrecycler);
        mErrorTextView = (TextView) findViewById(R.id.errorTextView);
        mProgressBar = (ProgressBar) findViewById(R.id.mainprogressbar);
        mMovielistAdapter=new MoviesListAdapter(this,null, this);
        // Checking the device rotation and show 3 columns if it is in landscape mode for better utilizing the wide screen.
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180)
            mLayoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        else
            mLayoutManager=new GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false);
        mRecyclerView.setAdapter(mMovielistAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        LoaderManager.LoaderCallbacks<MovieList> callbacks = MainActivity.this;
        getSupportLoaderManager().initLoader(1,null,callbacks);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // deciding which action menu to show, depending on the users's previous prefernce
        // showing the appropriate subtitle as well
        getMenuInflater().inflate(R.menu.main_menu,menu);
        if (mPreferences.getBoolean("popular",true)){
            menu.findItem(R.id.menu_popular).setVisible(false);
            menu.findItem(R.id.menu_top_rated).setVisible(true);
            getSupportActionBar().setSubtitle(R.string.show_popular);
        }
        else
        {
            menu.findItem(R.id.menu_popular).setVisible(true);
            menu.findItem(R.id.menu_top_rated).setVisible(false);
            getSupportActionBar().setSubtitle(R.string.show_top_rated);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Here we manage only the menu, the action will be performed by the SheredPreferences onchange listener
        switch (item.getItemId()){



            case R.id.menu_popular:{
                mPreferences.edit().putBoolean("popular",true).apply();
                break;
            }
            case R.id.menu_top_rated:{
                mPreferences.edit().putBoolean("popular",false).apply();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        // Unregistering the preference change listener
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onClick(View v) {
        //The user clicked on a movie poster, start detail activity and pass the actual movie to the activity.
        //The actual movie passed as MovieListItem parcellable object, we got it via the clicked view's tag oject from our adapter.
        //We set up a transition between activities, which animates the thumbnail int the detail activity´s final position
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie",(MovieListItem) v.getTag());
        ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                new Pair<View, String>(v.findViewById(R.id.picture),
                        "main"
                ));
        ActivityCompat.startActivity(this, intent, activityOptions.toBundle());
    }

    @Override
    public Loader<MovieList> onCreateLoader(int id, Bundle args) {
        //Creating a loader to load data on a worker thread.
        // hide all other UI elements but show the progressbar.
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.GONE);
        Log.d("Loader","Loader created");
        return new AsyncTaskLoader<MovieList>(this) {
           MovieList mMovieList=null;
           @Override
           protected void onStartLoading() {
               // if we already have data just use it, otherways force the loading.
               if (mMovieList != null) deliverResult(mMovieList);
               else {
                   forceLoad();
               }
           }
           @Override
           public MovieList loadInBackground() {
               // Check the Internet connection,Load the data on a worker thread, and deliver it to the UI thread.
               // If missing internet connection or error happening during download, deliver null result and set the error text to the
               // appropriate error message.
               String data;
               try {
                   //Checking internet connectivity as suggested in implementation guide pointing out stack owerflow post
                   Socket sock = new Socket();
                   sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
                   sock.close();
                   try{
                       // choose endpoint corresponds to user's last selection
                       if (mPreferences.getBoolean("popular",true))
                           data = NetworkU.get_Popular();
                       else
                           data = NetworkU.get_Top_Rated();
                       mMovieList= new JsonParser().ParseMovieString(data);
                   }
                   catch (Exception e)
                   {
                       // error during fetching data from the server
                       e.printStackTrace();
                       mErrorText=getString(R.string.fetcherror);
                       mMovieList=null;
                       return null;
                   }
                   // check if the data is null, then there was an to get the data, f.ex. no api key.
                   if (mMovieList == null){
                       // we check if we got some erreor message from the server
                       if (!NetworkU.mStatus.equals("OK")){
                           mErrorText=new JsonParser().ParseError(NetworkU.mStatus);
                           mMovieList=null;
                           return null;
                       }
                       else
                       {
                           mErrorText="Unknown error";
                           mMovieList=null;
                           return null;
                       }
                   }
                   // All is OK, return the data
                   return mMovieList;
               } catch (IOException e) {
                   // there is no internet access return null.
                   mErrorText=getString(R.string.nonetwork);
                   mMovieList=null;
                   return null;
               }
           }
           @Override
           public void deliverResult (MovieList m){
               // deliver the result to the UI thread
               mMovieList = m;
               super.deliverResult(m);
               Log.d("Loader", "Result delivered");
           }
       };
    }

    @Override
    public void onLoadFinished(Loader<MovieList> loader, MovieList data) {
        // We got a result. If data is null, then something went wrong.
        // hide the progressbar and show the error TextView after the previously stored error text is set.
        if (data == null){
            mErrorTextView.setText(mErrorText);
            mErrorTextView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }
        else {
            // all was OK, hide the progressbar and show the recycler.
            mProgressBar.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
        // Pass the newly fetched data into adapter
        mMovielistAdapter.setData(data);
        mRecyclerView.scrollToPosition(0);      // send the recyclerview to 0 position when selection is changed, so we always start from top
        Log.d("Loader","Loader finished");
    }

    @Override
    public void onLoaderReset(Loader<MovieList> loader) {
        // logging loader reset for testing pourposes
        Log.d("Loader","Loader reset");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // When a prference item (we have only the display mode [popular or top rated]) changes
        // we reconstruct the menu and forcing our loader to reload data. (Loading is display mode aware, so will load
        // from the necessary endpoint.)
        invalidateOptionsMenu();
        getSupportLoaderManager().getLoader(1).forceLoad();
    }
}
