package sample.Java.DTO;

import java.util.ArrayList;
import java.util.List;

public class Albums {
    private String name;
    private String single;
    private String releaseTime;
    private String pathImage;
    private List<Track> tracks = new ArrayList<>();

    public Albums(String name, String single, String releaseTime, String pathImage, List<Track> tracks) {
        this.name = name;
        this.single = single;
        this.releaseTime = releaseTime;
        this.pathImage = pathImage;
        this.tracks = tracks;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public Albums() {
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

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
