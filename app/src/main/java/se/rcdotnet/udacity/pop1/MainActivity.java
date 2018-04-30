package se.rcdotnet.udacity.pop1;

// The main activity

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.PersistableBundle;
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

import se.rcdotnet.udacity.pop1.database.MoviesContract;



public class MainActivity extends AppCompatActivity implements MoviesListAdapter.MoviesListClickHandler,
        LoaderManager.LoaderCallbacks<MovieList>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    static final String TAG = "Main";

    // Declaring the actvitis global variables
    private RecyclerView mRecyclerView;
    private TextView mErrorTextView;
    private ProgressBar mProgressBar;
    private MoviesListAdapter mMovielistAdapter;
    private GridLayoutManager mLayoutManager;
    private SharedPreferences mPreferences;
    private String mErrorText;
    private MovieList mMovieList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize variables, set up the RecyclerView, start the Loader, register Listener for preference changes
        super.onCreate(savedInstanceState);
        mMovieList = null;
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
        if (savedInstanceState != null)
            Log.d(TAG,"OnCreate" + savedInstanceState.toString());
        else
            Log.d(TAG,"OnCreate");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"OnStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"OnStop");
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"OnPause");
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        // Unregistering the preference change listener
        super.onDestroy();
        Log.d(TAG,"OnDestroy");
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG,"OnResume");
    }
    @Override
    protected  void onRestart(){
        super.onRestart();
        Log.d(TAG,"OnRestart");
    }
    @Override protected void onSaveInstanceState (Bundle state){
        super.onSaveInstanceState(state);
        Log.d(TAG,"OnSaveInstance" + state.toString());
    }
    @Override public void onSaveInstanceState (Bundle state, PersistableBundle pstate){
        super.onSaveInstanceState(state,pstate);
        Log.d(TAG,"OnSaveInstance with persistable");
    }
    @Override
    protected void onRestoreInstanceState (Bundle state){
        super.onRestoreInstanceState(state);
        Log.d(TAG,"OnRestoreInstance" + state);
    }
//    @Override
//    protected void onRestoreInsatnceState (Bundle state, PersistableBundle pstate){
//        super.onRestoreInstanceState(state,pstate);
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // deciding which action menu to show, depending on the users's previous prefernce
        // showing the appropriate subtitle as well
        getMenuInflater().inflate(R.menu.main_menu,menu);
        if (mPreferences.getString("display","popular").equals("popular")){
            getSupportActionBar().setSubtitle(R.string.show_popular);
        }
        if (mPreferences.getString("display","popular").equals("top_rated")){
            getSupportActionBar().setSubtitle(R.string.show_top_rated);
        }
        if (mPreferences.getString("display","popular").equals("favorit")){
            getSupportActionBar().setSubtitle(R.string.show_top_favorit);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Here we manage only the menu, the action will be performed by the SheredPreferences onchange listener
        switch (item.getItemId()){
            case R.id.menu_popular:{
                mPreferences.edit().putString("display","popular").apply();
                break;
            }
            case R.id.menu_top_rated:{
                mPreferences.edit().putString("display","top_rated").apply();
                break;
            }
            case R.id.menu_favorit:{
                mPreferences.edit().putString("display","favorit").apply();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onClick(View v) {
        //The user clicked on a movie poster, start detail activity and pass the actual movie to the activity.
        //The actual movie passed as MovieListItem parcellable object, we got it via the clicked view's tag oject from our adapter.
        //We set up a transition between activities, which animates the thumbnail int the detail activity´s final position
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movie",mMovieList.movies.get((int) v.getTag()));
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
        Log.d("TAG","Loader created");
        return new AsyncTaskLoader<MovieList>(this) {
            public ContentObserver obs;
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
               Log.d(TAG,"Loader loading");
               // Check the Internet connection,Load the data on a worker thread, and deliver it to the UI thread.
               // If missing internet connection or error happening during download, deliver null result and set the error text to the
               // appropriate error message.
               String data=null;
               String display = mPreferences.getString("display","popular");
               try {
                   //Checking internet connectivity as suggested in implementation guide pointing out stack owerflow post
                   Socket sock = new Socket();
                   sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
                   sock.close();
                   int x = Integer.MAX_VALUE;
                   for (int i=1 ; i<= 10; i++) {  // we have a request count limit at 40
                       try {
                           // choose endpoint corresponds to user's last selection
                           if (display.equals("favorit")){
                               Cursor cursor = getContentResolver().query(MoviesContract.movies.MOVIES_CONTENT_URI,null,
                                       null,null,null);
                               mMovieList = new MovieList();
                               MovieListItem item;
                            while (cursor.moveToNext() ){
                               data = NetworkU.get_Details(cursor.getInt(cursor.getColumnIndex("id")));
                               item = new JsonParser().ParseMovieItem(data);
                               mMovieList.movies.add(item);
                            }
                            break;
                           }
                           else {
                               if (display.equals("popular"))
                                   data = NetworkU.get_Popular(i);
                               if (display.equals("top_rated"))
                                   data = NetworkU.get_Top_Rated(i);
                               if (i == 1)
                                   mMovieList = new JsonParser().ParseMovieString(data, null);
                               else
                                   mMovieList = new JsonParser().ParseMovieString(data, mMovieList);
                               x = mMovieList.total_pages;
                           }
                       } catch (Exception e) {
                           // error during fetching data from the server
                           e.printStackTrace();
                           mErrorText = getString(R.string.fetcherror);
                           mMovieList = null;
                           return null;
                       }
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
               Log.d(TAG, "Result delivered");
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
        mMovieList = data;
        Log.d(TAG,"Loader finished");
    }

    @Override
    public void onLoaderReset(Loader<MovieList> loader) {
        // logging loader reset for testing pourposes
        Log.d(TAG,"Loader reset");
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // When a prference item (we have only the display mode [popular or top rated]) changes
        // we reconstruct the menu and forcing our loader to reload data. (Loading is display mode aware, so will load
        // from the necessary endpoint.)
        invalidateOptionsMenu();
        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        getSupportLoaderManager().getLoader(1).forceLoad();
    }
}
