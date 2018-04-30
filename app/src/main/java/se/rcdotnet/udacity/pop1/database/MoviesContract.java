package se.rcdotnet.udacity.pop1.database;

import android.net.Uri;
import android.provider.BaseColumns;


/**
 * Created by Laszlo_HP_Notebook on 2018-04-26.
 */

public class MoviesContract  {
    // Content Provider constans
    // Authority name
    public static final String AUTHORITY = "se.rcdotnet.udacity.pop1.movies";

    // content uri base
    public static final Uri CONTENT_URI_BASE = Uri.parse("content://" + AUTHORITY);

    // the pats in the databas
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";


        public static final class movies implements BaseColumns {
        // movies content uri
        public  static final Uri MOVIES_CONTENT_URI = CONTENT_URI_BASE.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_POSTER_PATH_NAME ="poster_path";
        public static final String COLUMN_ADULT_NAME ="adult";
        public static final String COLUMN_OVERVIEW_NAME ="overview";
        public static final String COLUMN_RELEASE_DATE_NAME ="release";
        public static final String COLUMN_GENRE_IDS_NAME ="genres";
        public static final String COLUMN_ID_NAME ="id";
        public static final String COLUMN_ORIGINAL_TITLE_NAME ="original_title";
        public static final String COLUMN_ORIGINAL_LANGUAGE_NAME ="original_language";
        public static final String COLUMN_TITLE_NAME ="title";
        public static final String COLUMN_BACKDROP_PATH_NAME ="backdrop_path";
        public static final String COLUMN_POPULARITY_NAME ="popularity";
        public static final String COLUMN_VOTE_COUNT_NAME ="vote_count";
        public static final String COLUMN_VIDEO_NAME ="video";
        public static final String COLUMN_VOTE_AVARAGE_NAME ="vote_avarage";

    }
    public  static final class reviews implements BaseColumns {
        // Reviews content uri
        public  static final Uri REVIEWS_CONTENT_URI = CONTENT_URI_BASE.buildUpon().appendPath(PATH_REVIEWS).build();

        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_ID_NAME ="id";
        public static final String COLUMN_AUTHOR_NAME ="author";
        public static final String COLUMN_CONTENT_NAME ="content";
        public static final String COLUMN_URL_NAME ="url";
    }
    public static final class videos implements BaseColumns {
        // videos content uri
        public  static final Uri VIDEOS_CONTENT_URI = CONTENT_URI_BASE.buildUpon().appendPath(PATH_VIDEOS).build();

        public static final String TABLE_NAME = "videos";
        public static final String COLUMN_ID_NAME ="id";
        public static final String COLUMN_ISO_639_1_NAME ="iso_639_1";
        public static final String COLUMN_ISO_3166_1_NAME ="iso_3166_1";
        public static final String COLUMN_KEY_NAME ="key";
        public static final String COLUMN_NAME_NAME ="name";
        public static final String COLUMN_SITE_NAME ="site";
        public static final String COLUMN_SIZE_NAME ="size";
        public static final String COLUMN_TYPE_NAME ="type";
    }
}
