package se.rcdotnet.udacity.pop1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Laszlo_HP_Notebook on 2018-04-22.
 */

public class VideoListItem implements Parcelable{
    String id;
    String iso_639_1;
    String iso_3166_1;
    String key;
    String name;
    String site;
    int size;       // Allowed Values: 360, 480, 720, 1080
    String type;    //Allowed Values: Trailer, Teaser, Clip, Featurette

    public VideoListItem(){}

    protected VideoListItem(Parcel in) {
        id = in.readString();
        iso_639_1 = in.readString();
        iso_3166_1 = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    public static final Creator<VideoListItem> CREATOR = new Creator<VideoListItem>() {
        @Override
        public VideoListItem createFromParcel(Parcel in) {
            return new VideoListItem(in);
        }

        @Override
        public VideoListItem[] newArray(int size) {
            return new VideoListItem[size];
        }
    };

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIso_639_1() {
        return iso_639_1;
    }
    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }
    public String getIso_3166_1() {
        return iso_3166_1;
    }
    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSite() {
        return site;
    }
    public void setSite(String site) {
        this.site = site;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso_639_1);
        dest.writeString(iso_3166_1);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }
}
