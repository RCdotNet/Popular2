package se.rcdotnet.udacity.pop1;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Laszlo_HP_Notebook on 2018-04-27.
 */

public class MovieContentProvider extends ContentProvider {

    // Defining the IDs
    public static final int MOVIES = 100;
    public static final int MOVIES_RECORD = 101;
    public static final int REVIEWS = 200;
    public static final int REVIEWS_RECORD = 201;
    public static final int VIDEOS = 300;
    public static final int VIDEOS_RECORD = 301;

// uri matcher
private static final UriMatcher mUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieDbHelper;

   // Uri matcher
    public static UriMatcher buildUriMatcher() {
        // no match by default
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Adding matches
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_RECORD);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_REVIEWS, REVIEWS);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_REVIEWS + "/#", REVIEWS_RECORD);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_VIDEOS, VIDEOS);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_VIDEOS + "/#", VIDEOS_RECORD);
        return uriMatcher;
    }



    @Override
    public boolean onCreate()
    {
        mMovieDbHelper = new MovieDbHelper(getContext());
       return true;
    }
    @Nullable
    @Override
    // QUERY
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase mDB = mMovieDbHelper.getReadableDatabase();
        Cursor rCursor;
        int op = mUriMatcher.match(uri);
        String table="";
        switch (op){
            case MOVIES:
                table =  MoviesContract.movies.TABLE_NAME;
                break;
            case REVIEWS:
                table =  MoviesContract.reviews.TABLE_NAME;
                break;
            case VIDEOS:
                table =  MoviesContract.videos.TABLE_NAME;
                break;
            case MOVIES_RECORD:
                table =  MoviesContract.movies.TABLE_NAME;
                break;
            case REVIEWS_RECORD:
                table =  MoviesContract.reviews.TABLE_NAME;
                break;
            case VIDEOS_RECORD:
                table =  MoviesContract.movies.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Uri not recognized: " + uri);
        }
        rCursor = mDB.query(table,projection,selection,selectionArgs,null,null,sortOrder);
        rCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return rCursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    //INSERT
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase mDB = mMovieDbHelper.getWritableDatabase();
        Uri rUri;
        int op = mUriMatcher.match(uri);
        String table="";
        switch (op){
            case MOVIES:
                table =  MoviesContract.movies.TABLE_NAME;
                rUri = MoviesContract.movies.MOVIES_CONTENT_URI;
                break;
            case REVIEWS:
                table =  MoviesContract.reviews.TABLE_NAME;
                rUri = MoviesContract.reviews.REVIEWS_CONTENT_URI;
                break;
            case VIDEOS:
                table =  MoviesContract.videos.TABLE_NAME;
                rUri = MoviesContract.videos.VIDEOS_CONTENT_URI;
                break;
            default:
                throw new UnsupportedOperationException("Uri not recognized: " + uri);
        }
        long id = mDB.insert(table, null, values);
        if ( id > 0 ) {
            rUri = ContentUris.withAppendedId(rUri, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rUri;
    }
    @Override
    //DELETE
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase mDB = mMovieDbHelper.getWritableDatabase();
        int deleted = 0;
        int op = mUriMatcher.match(uri);
        String table="";
        switch (op){
            case MOVIES_RECORD:
                table =  MoviesContract.movies.TABLE_NAME;
                break;
            case REVIEWS_RECORD:
                table =  MoviesContract.reviews.TABLE_NAME;
                break;
            case VIDEOS_RECORD:
                table =  MoviesContract.movies.TABLE_NAME;
                break;
            default:
                throw new UnsupportedOperationException("Uri not recognized: " + uri);
        }
        String record = uri.getPathSegments().get(1);
        deleted = mDB.delete(table, "id=?", new String[]{record});
        return deleted;
    }

    @Override
    //UPDATE
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // we does not support update operations as it is not necessary in the project
        return 0;
    }
}
