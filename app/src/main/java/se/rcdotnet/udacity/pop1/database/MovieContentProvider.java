import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
private static final UriMatcher sUriMatcher = buildUriMatcher();

   // Uri matcher
    public static UriMatcher buildUriMatcher() {

        // no match by default
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        // Adding matches
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MoviesContract.AUTHORITY, MoviesContract.PATH_MOVIES + "/#", MOVIES_RECORD);

        return uriMatcher;
    }



    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
