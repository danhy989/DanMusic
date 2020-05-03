package sample.Java.DTO;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class CurrentPlayList {
    private HashMap<String,Object> currentPlaylist = new HashMap<>();

    public CurrentPlayList(ObservableList<File> listFiles, List<Track> tracks) throws Exception {
        if(listFiles.size()!=tracks.size()){
            throw new Exception("listFiles va tracks kich co danh sach khong bang nhau");
        }
        MusicplayerTracksInfo musicplayerTracksInfo = new MusicplayerTracksInfo();
        for(int i=0;i<tracks.size();i++){
            File fileImage = new File(tracks.get(i).getImageTrack());
            musicplayerTracksInfo.getImageTracksObservableList().add(new SimpleObjectProperty<Image>(new Image(fileImage.toURI().toString())));
            musicplayerTracksInfo.getNameTracksObservableList().add(new SimpleObjectProperty<String>(new String(tracks.get(i).getName())));
            musicplayerTracksInfo.getSingleTracksObservableList().add(new SimpleObjectProperty<String>(new String(tracks.get(i).getSingle())));
        }
        this.currentPlaylist.put("musicplayerTracksInfo",musicplayerTracksInfo);
        this.currentPlaylist.put("listFiles",listFiles);
    }

    public HashMap<String, Object> getCurrentPlaylist() {
        return currentPlaylist;
    }

    public void setCurrentPlaylist(HashMap<String, Object> currentPlaylist) {
        this.currentPlaylist = currentPlaylist;
    }
}
