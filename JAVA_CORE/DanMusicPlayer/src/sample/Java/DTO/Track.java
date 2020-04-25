package sample.Java.DTO;

import java.io.File;

public class Track {
    private String name;
    private String duration;
    private File trackFile;
    private boolean isPlaying = false;

    public Track(String name, String duration, File trackFile) {
        this.name = name;
        this.duration = duration;
        this.trackFile = trackFile;
    }

    public Track() {
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
