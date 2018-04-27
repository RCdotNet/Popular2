package se.rcdotnet.udacity.pop1;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Laszlo_HP_Notebook on 2018-03-22.
 * Json parser to parse the returned JSON and store the data in a MovieList object.
 * The MovieList object contains an ArrayList<MovieListItem>, which stores data for the invidual movies.
 * Parsing only Stage 1 required data.
 * Returns null if no data or error occured.
 */

public class  JsonParser {
    public MovieList ParseMovieString(String toParse) {
        MovieList returnMovieList;
        MovieListItem listItem;
        JSONObject MovieList;
        JSONObject movieItem;
        JSONArray MovieItems;
        returnMovieList = new MovieList();
        if (toParse == null) return null; // there is nothing to parse
        try {
            MovieList = new JSONObject(toParse);
            returnMovieList.page = MovieList.getInt("page");
            returnMovieList.total_results = MovieList.getInt("total_results");
            returnMovieList.total_pages = MovieList.getInt("total_pages");
            MovieItems = MovieList.getJSONArray("results");
            // extracting each movie from embedded JSON object into the ArrayList<MovieListItem> element of the return object;
            listItem = null;
            for (int i = 0; i < MovieItems.length(); ++i) {
                movieItem = MovieItems.getJSONObject(i);
                listItem = new MovieListItem();
                listItem.posterPath = movieItem.getString("poster_path");
                listItem.adult = movieItem.getBoolean("adult");
                listItem.backdropPath = movieItem.getString("backdrop_path");
                listItem.title = movieItem.getString("original_title");
                listItem.releaseDate = movieItem.getString("release_date");
                listItem.voteAvarage = movieItem.getInt("vote_average");
                listItem.owerview = movieItem.getString("overview");
                listItem.id= movieItem.getInt("id");
                returnMovieList.movies.add(listItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return returnMovieList;
    }

    public ReviewList ParseReviewString (String stringToParse){
        ReviewList list;
        ReviewItem item;
        JSONObject reviewListJSON;
        JSONObject reviewItemJSON;
        JSONArray reviewArrayJSON;
        list = new ReviewList();
        if (stringToParse == null || stringToParse.isEmpty()) return null; // there is nothing to parse
        try {
            reviewListJSON = new JSONObject(stringToParse);
            list.id = reviewListJSON.getInt("id");
            list.page = reviewListJSON.getInt("page");
            list.total_pages = reviewListJSON.getInt("total_pages");
            list.total_results = reviewListJSON.getInt("total_results");
            // read the review array
            reviewArrayJSON = reviewListJSON.getJSONArray("results");
            item = null;
            for (int i = 0; i < reviewArrayJSON.length(); ++ i) {
                reviewItemJSON = reviewArrayJSON.getJSONObject(i);
                item = new ReviewItem();
                item.id = reviewItemJSON.getString("id");
                item.author = reviewItemJSON.getString("author");
                item.content = reviewItemJSON.getString("content");
                item.url = reviewItemJSON.getString("url");
                list.reviews.add(item);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public VideoList parseMoviesString (String stringToParse){
        VideoList list;
        VideoListItem item;
        JSONObject videoListJSON;
        JSONObject videoListItemJSON;
        JSONArray videoArray;
        list = new VideoList();
        if (stringToParse == null || stringToParse.isEmpty()) return null; // there is nothing to parse
        try {
            videoListJSON = new JSONObject(stringToParse);
            list.id = videoListJSON.getInt("id");
            // read the videos array;
            videoArray = videoListJSON.getJSONArray("results");
            item=null;
            for (int i = 0; i < videoArray.length(); i++){
                videoListItemJSON = videoArray.getJSONObject(i);
                item = new VideoListItem();
                item.id = videoListItemJSON.getString("id");
                item.iso_639_1 = videoListItemJSON.getString("iso_639_1");
                item.iso_3166_1 = videoListItemJSON.getString("iso_3166_1");
                item.key = videoListItemJSON.getString("key");
                item.name = videoListItemJSON.getString("name");
                item.site = videoListItemJSON.getString("site");
                item.size = videoListItemJSON.getInt("size");
                item.type = videoListItemJSON.getString("type");
                list.videos.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public String ParseError(String mStatus) {
        String retVal="";
        try {
            retVal=(new JSONObject(mStatus).getString("status_message"));
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
        return retVal;
    }
}
