package se.rcdotnet.udacity.pop1;

// this object represent one movie.
// This made to be parcellable so we can pass around between activities

import android.os.Parcel;
import android.os.Parcelable;

public class MovieListItem implements Parcelable{
    String posterPath;
    boolean adult;
    String owerview;
    String releaseDate;
    int [] genre_ids;
    int id;
    String originalTitle;
    String originalLanguage;
    String title;
    String backdropPath;
    int popularity;
    int voteCount;
    boolean video;
    int voteAvarage;

    protected MovieListItem(Parcel in) {
        posterPath = in.readString();
        adult = in.readByte() != 0;
        owerview = in.readString();
        releaseDate = in.readString();
        genre_ids = in.createIntArray();
        id = in.readInt();
        originalTitle = in.readString();
        originalLanguage = in.readString();
        title = in.readString();
        backdropPath = in.readString();
        popularity = in.readInt();
        voteCount = in.readInt();
        video = in.readByte() != 0;
        voteAvarage = in.readInt();
    }
    public MovieListItem(){}

    public static final Creator<MovieListItem> CREATOR = new Creator<MovieListItem>() {
        @Override
        public MovieListItem createFromParcel(Parcel in) {
            return new MovieListItem(in);
        }

        @Override
        public MovieListItem[] newArray(int size) {
            return new MovieListItem[size];
        }
    };

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOwerview() {
        return owerview;
    }

    public void setOwerview(String owerview) {
        this.owerview = owerview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(int[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public int getVoteAvarage() {
        return voteAvarage;
    }

    public void setVoteAvarage(int voteAvarage) {
        this.voteAvarage = voteAvarage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeByte((byte) (adult ? 1 : 0));
        dest.writeString(owerview);
        dest.writeString(releaseDate);
        dest.writeIntArray(genre_ids);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeInt(popularity);
        dest.writeInt(voteCount);
        dest.writeByte((byte) (video ? 1 : 0));
        dest.writeInt(voteAvarage);
    }
}
