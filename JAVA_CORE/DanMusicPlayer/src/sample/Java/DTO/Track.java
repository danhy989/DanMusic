package sample.Java.DTO;

import java.io.File;

public class Track {
    private String name;
    private String duration;
    private File trackFile;
    private boolean isPlaying = false;
    private String imageTrack = "";
    private String single;

    public Track(String name, String duration, File trackFile, String imageTrack, String single) {
        this.name = name;
        this.duration = duration;
        this.trackFile = trackFile;
        this.imageTrack = imageTrack;
        this.single = single;
    }

    public Track() {
    }

    public String getSingle() {
        return single;
    }

    public void setSingle(String single) {
        this.single = single;
    }

    public String getImageTrack() {
        return imageTrack;
    }

    public void setImageTrack(String imageTrack) {
        this.imageTrack = imageTrack;
    }

    public File getTrackFile() {
        return trackFile;
    }

    public void setTrackFile(File trackFile) {
        this.trackFile = trackFile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
