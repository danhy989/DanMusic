package sample.Java.DTO;

import javafx.collections.ObservableList;

import java.io.File;

public class CurrentPlayList {
    private ObservableList<File> listFile;

    public CurrentPlayList(ObservableList<File> listFile){

        this.listFile = listFile;
    }

    public ObservableList<File> getListFile() {
        return listFile;
    }

    public void setListFile(ObservableList<File> listFile) {
        this.listFile = listFile;
    }
}
