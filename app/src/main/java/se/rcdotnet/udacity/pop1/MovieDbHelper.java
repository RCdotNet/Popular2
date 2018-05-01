package se.rcdotnet.udacity.pop1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Laszlo_HP_Notebook on 2018-04-26.
 */
    // DBHelper, we using the contract to build the database.
    // While we define the whole data objects as tables, we coming to use only the Movies table to store the favorit IDs.
    // The database gives place for further improvements.

public class MovieDbHelper extends SQLiteOpenHelper {

private static final int DB_VERSION = 1;
private static final String DB_NAME = "movie.db";
private static final String MOVIE_TABLE = "movies";
private static final String REWIEWS_TABLE = "reviews";
private static final String VIDEOS_TABLE = "videos";


    public MovieDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    create_tables(db);
    }

    private void create_tables(SQLiteDatabase db) {
    final String CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesContract.movies.TABLE_NAME + " (" +
            MoviesContract.movies.COLUMN_POSTER_PATH_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_ADULT_NAME + " INTEGER," +
            MoviesContract.movies.COLUMN_OVERVIEW_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_RELEASE_DATE_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_GENRE_IDS_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_ID_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            MoviesContract.movies.COLUMN_ORIGINAL_TITLE_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_ORIGINAL_LANGUAGE_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_TITLE_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_BACKDROP_PATH_NAME + " TEXT," +
            MoviesContract.movies.COLUMN_POPULARITY_NAME + " INTEGER," +
            MoviesContract.movies.COLUMN_VOTE_COUNT_NAME + "INTEGER," +
            MoviesContract.movies.COLUMN_VIDEO_NAME + " INTEGER," +
            MoviesContract.movies.COLUMN_VOTE_AVARAGE_NAME + " INTEGER" +
            "); ";
    final String CREATE_REVIEW_TABLE = "CREATE TABLE " + MoviesContract.reviews.TABLE_NAME + " (" +
                MoviesContract.reviews.COLUMN_ID_NAME + " INTEGER NOT NULL," +
                MoviesContract.reviews.COLUMN_AUTHOR_NAME + " TEXT," +
                MoviesContract.reviews.COLUMN_CONTENT_NAME + " TEXT," +
                MoviesContract.reviews.COLUMN_URL_NAME + " TEXT" +
                "); ";
    final String CREATE_VIDEOS_TABLE = "CREATE TABLE " + MoviesContract.videos.TABLE_NAME + " (" +
            MoviesContract.videos.COLUMN_ID_NAME + " INTEGER NOT NULL," +
            MoviesContract.videos.COLUMN_ISO_639_1_NAME + " TEXT," +
            MoviesContract.videos.COLUMN_ISO_3166_1_NAME + " TEXT," +
            MoviesContract.videos.COLUMN_KEY_NAME + " TEXT," +
  //          MoviesContract.videos.COLUMN_ID_NAME + " TEXT," +
            MoviesContract.videos.COLUMN_SITE_NAME + "TEXT," +
            MoviesContract.videos.COLUMN_SIZE_NAME + " INTEGER," +
            MoviesContract.videos.COLUMN_TYPE_NAME + " TEXT" +
            "); ";
    db.execSQL(CREATE_MOVIE_TABLE);
    db.execSQL(CREATE_REVIEW_TABLE);
    db.execSQL(CREATE_VIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
