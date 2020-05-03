package sample.Java.entities;

public class TrackEntity {
    private Long id;
    private String name;
    private String pathImage;
    private String pathSoundFile;
    public TrackEntity(Long id, String name, String pathImage, String pathSoundFile) {
        this.id = id;
        this.name = name;
        this.pathImage = pathImage;
        this.pathSoundFile = pathSoundFile;
    }

    public TrackEntity() {
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

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getPathSoundFile() {
        return pathSoundFile;
    }

    public void setPathSoundFile(String pathSoundFile) {
        this.pathSoundFile = pathSoundFile;
    }

}
