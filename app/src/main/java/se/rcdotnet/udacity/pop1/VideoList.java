package se.rcdotnet.udacity.pop1;

import java.util.ArrayList;

/**
 * Created by Laszlo_HP_Notebook on 2018-04-22.
 */

public class VideoList {
    int id;
    ArrayList <VideoListItem> videos;

    public  VideoList(){
        videos = new ArrayList<VideoListItem>();
    }
}
