package sample.Java.entities;

public class AlbumTrackEntity {
    private Long id_album;
    private Long id_track;
    private String createTime;

    public AlbumTrackEntity(Long id_album, Long id_track, String createTime) {
        this.id_album = id_album;
        this.id_track = id_track;
        this.createTime = createTime;
    }

    public AlbumTrackEntity() {
    }

    public Long getId_album() {
        return id_album;
    }

    public void setId_album(Long id_album) {
        this.id_album = id_album;
    }

    public Long getId_track() {
        return id_track;
    }

    public void setId_track(Long id_track) {
        this.id_track = id_track;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
