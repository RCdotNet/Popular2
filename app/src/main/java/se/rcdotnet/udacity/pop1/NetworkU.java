package se.rcdotnet.udacity.pop1;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Laszlo_HP_Notebook on 2018-03-20.
 * Network utilities to get data from the datasource on the net.
 * This routines runs on worker thread as they are called from a loader after passed Internet availability test.
 */

public final class NetworkU {
    static final String BASEURL="http://api.themoviedb.org/3/movie/";
    static final String POPULAR="http://api.themoviedb.org/3/movie/popular";
    static final String TOP_RATED="http://api.themoviedb.org/3/movie/top_rated";
    static final String POSTER_BASE="http://image.tmdb.org/t/p/w185/";
    static  String mStatus = "OK"; // indicates thet the response was ok.
    static final String REVIEW="reviews";  // have to pass id and /reviews
    static final String VIDEOS="videos";

    public static String get_Popular(){
        // Build Uri for popular endpoint and get data
        URL popularURL;
        Uri returnuri = Uri.parse(POPULAR).buildUpon().appendQueryParameter("api_key",ApiKey.getAPIKEY()).build();
        try {
            popularURL = new URL(returnuri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
        try {
            return getResponse(popularURL);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String get_Top_Rated(){
        // Build Uri for top rated enpoint and get data
        URL topRatedURL;
        Uri returnuri = Uri.parse(TOP_RATED).buildUpon().appendQueryParameter("api_key",ApiKey.getAPIKEY()).build();
        try {
            topRatedURL = new URL(returnuri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
        try {
            return getResponse(topRatedURL);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getReviews(String id){
        // Build Uri for top rated enpoint and get data
        URL reviewsURL;
        Uri returnuri = Uri.parse(BASEURL).buildUpon().appendPath(id).appendPath(REVIEW).appendQueryParameter("api_key",ApiKey.getAPIKEY()).build();
        try {
            reviewsURL = new URL(returnuri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
        try {
            return getResponse(reviewsURL);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String getVideos(String id){
        // Build Uri for top rated enpoint and get data
        URL videosURL;
        Uri returnuri = Uri.parse(BASEURL).buildUpon().appendPath(id).appendPath(VIDEOS).appendQueryParameter("api_key",ApiKey.getAPIKEY()).build();
        try {
            videosURL = new URL(returnuri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "";
        }
        try {
            return getResponse(videosURL);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String getResponse(URL url) throws IOException {
        // Get response from the selected endpoint
        // Inherited from Sunshine project.
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream error = urlConnection.getErrorStream();
            if (error != null) {
                Scanner scanner = new Scanner(error);
                scanner.useDelimiter("\\A");
                boolean hasInput = scanner.hasNext();
                String errorString = null;
                if (hasInput) {
                    errorString = scanner.next();
                }
                scanner.close();
                mStatus = errorString;
                return null;
            } else {
                InputStream in = urlConnection.getInputStream();
                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");
                Boolean hasInput = scanner.hasNext();
                String response = null;
                if (hasInput) {
                    response = scanner.next();
                }
                scanner.close();
                mStatus="OK";
                return response;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}

