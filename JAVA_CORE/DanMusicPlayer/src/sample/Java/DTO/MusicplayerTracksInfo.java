package sample.Java.DTO;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.image.Image;

public class MusicplayerTracksInfo {
    ObservableList<ObservableValue<Image>> imageTracksObservableList = FXCollections.observableArrayList();
    ObservableList<ObservableValue<String>> nameTracksObservableList = FXCollections.observableArrayList();
    ObservableList<ObservableValue<String>> singleTracksObservableList = FXCollections.observableArrayList();

    public MusicplayerTracksInfo(ObservableList<ObservableValue<Image>> imageTracksObservableList,
                                 ObservableList<ObservableValue<String>> nameTracksObservableList,
                                 ObservableList<ObservableValue<String>> singleTracksObservableList) {
        this.imageTracksObservableList = imageTracksObservableList;
        this.nameTracksObservableList = nameTracksObservableList;
        this.singleTracksObservableList = singleTracksObservableList;
    }

    public MusicplayerTracksInfo() {
    }

    public ObservableList<ObservableValue<Image>> getImageTracksObservableList() {
        return imageTracksObservableList;
    }

    public void setImageTracksObservableList(ObservableList<ObservableValue<Image>> imageTracksObservableList) {
        this.imageTracksObservableList = imageTracksObservableList;
    }

    public ObservableList<ObservableValue<String>> getNameTracksObservableList() {
        return nameTracksObservableList;
    }

    public void setNameTracksObservableList(ObservableList<ObservableValue<String>> nameTracksObservableList) {
        this.nameTracksObservableList = nameTracksObservableList;
    }

    public ObservableList<ObservableValue<String>> getSingleTracksObservableList() {
        return singleTracksObservableList;
    }

    public void setSingleTracksObservableList(ObservableList<ObservableValue<String>> singleTracksObservableList) {
        this.singleTracksObservableList = singleTracksObservableList;
    }
}
