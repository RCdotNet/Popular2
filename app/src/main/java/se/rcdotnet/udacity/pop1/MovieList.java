package se.rcdotnet.udacity.pop1;

import java.util.ArrayList;

/**
 * Created by Laszlo_HP_Notebook on 2018-03-22.
 * The response object. We parse the response (JSON) into this object.
 */

public class MovieList {
    int page;
    ArrayList<MovieListItem> movies;
    int total_results;
    int total_pages;
    public MovieList(){
    movies = new ArrayList<MovieListItem>();
    }

}
