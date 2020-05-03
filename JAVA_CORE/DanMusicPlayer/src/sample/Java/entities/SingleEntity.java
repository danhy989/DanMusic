package sample.Java.entities;

public class SingleEntity {
    private Long id;
    private String name;
    private String info;
    private String pathImage;
    private Long id_genre;

    public SingleEntity(Long id, String name, String info, String pathImage, Long id_genre) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.pathImage = pathImage;
        this.id_genre = id_genre;
    }

    public SingleEntity() {

    }

    public Long getId_genre() {
        return id_genre;
    }

    public void setId_genre(Long id_genre) {
        this.id_genre = id_genre;
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
}
