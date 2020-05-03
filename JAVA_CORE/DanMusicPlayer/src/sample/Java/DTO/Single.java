package sample.Java.DTO;

import java.util.List;

public class Single {
    private String name;
    private String genre;
    private String info;
    private String pathImage;
    private Album albumTrackHot;
    private List<Album> albums;

    public Single(String name, String genre, String info, String pathImage, Album albumTrackHot, List<Album> albums) {
        this.name = name;
        this.genre = genre;
        this.info = info;
        this.pathImage = pathImage;
        this.albumTrackHot = albumTrackHot;
        this.albums = albums;
    }

    public Single() {
    }

    public Album getAlbumTrackHot() {
        return albumTrackHot;
    }

    public void setAlbumTrackHot(Album albumTrackHot) {
        this.albumTrackHot = albumTrackHot;
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

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

}
