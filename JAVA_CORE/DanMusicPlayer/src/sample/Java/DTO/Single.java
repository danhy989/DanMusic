package sample.Java.DTO;

import java.util.ArrayList;
import java.util.List;

public class Single {
    private String name;
    private String genre;
    private String info;
    private String pathImage;
    private Albums albumsTrackHot;
    private List<Albums> albums;

    public Single(String name, String genre, String info, String pathImage, Albums albumsTrackHot, List<Albums> albums) {
        this.name = name;
        this.genre = genre;
        this.info = info;
        this.pathImage = pathImage;
        this.albumsTrackHot = albumsTrackHot;
        this.albums = albums;
    }

    public Single() {
    }

    public Albums getAlbumsTrackHot() {
        return albumsTrackHot;
    }

    public void setAlbumsTrackHot(Albums albumsTrackHot) {
        this.albumsTrackHot = albumsTrackHot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public List<Albums> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Albums> albums) {
        this.albums = albums;
    }

}
