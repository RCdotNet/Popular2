package se.rcdotnet.udacity.pop1;

import java.util.ArrayList;

/**
 * Created by Laszlo_HP_Notebook on 2018-04-22.
 */

public class ReviewList {
    int id;
    int page;
    ArrayList<ReviewItem> reviews;
    int total_results;
    int total_pages;
    public ReviewList(){
        reviews = new ArrayList<ReviewItem>();
    }
}
