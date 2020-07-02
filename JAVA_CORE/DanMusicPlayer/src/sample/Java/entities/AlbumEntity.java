package sample.Java.entities;

public class AlbumEntity {
    private Long id;
    private String name;
    private String releaseTime;
    private String pathImage;
    private boolean album_hot_track;
    private Long id_single;

    public AlbumEntity(Long id, String name, String releaseTime, String pathImage, boolean album_hot_track, Long id_single) {
        this.id = id;
        this.name = name;
        this.releaseTime = releaseTime;
        this.pathImage = pathImage;
        this.album_hot_track = album_hot_track;
        this.id_single = id_single;
    }

    public AlbumEntity() {

    }

    public AlbumEntity(long id) {
        this.id = id;
    }


    public Long getId_single() {
        return id_single;
    }

    public void setId_single(Long id_single) {
        this.id_single = id_single;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public boolean isAlbum_hot_track() {
        return album_hot_track;
    }

    public void setAlbum_hot_track(boolean album_hot_track) {
        this.album_hot_track = album_hot_track;
    }
}
