package sample.Java.Controller.Library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import sample.Java.Controller.AlbumPane;
import sample.Java.DTO.Album;
import sample.Java.DTO.Single;
import sample.Java.Service.AlbumService;
import sample.Java.Service.SingleService;
import sample.Java.Static.DataStaticLoader;
import sample.Java.entities.AlbumEntity;
import sample.Java.entities.SingleEntity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ArtistPane extends BorderPane implements Initializable {
    private static final int MAX_ITEMS_ON_ONE_ROW = 4;

    @FXML
    private ScrollPane scrollPane;

    public ArtistPane(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("../../../Resources/Fxml/Library/LibListPane.fxml"));

        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<SingleEntity> singleEntityList = null;
        try {
            singleEntityList = DataStaticLoader.getSingleEntityDao().getRandomSingle(12);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        double countRow = 0;
        if(singleEntityList.size()<MAX_ITEMS_ON_ONE_ROW) {
            countRow += 1;
        }else{
            countRow = (double) singleEntityList.size()/MAX_ITEMS_ON_ONE_ROW;
            double check = countRow - (int)countRow;
            if(check!=0){
                countRow = (int)countRow+1;
            }
        }
        List<Single> singleList = SingleService.getInstance().convertFromSingleDao(singleEntityList);
        int numSingle = 0;
        VBox vBox = new VBox();
        for(int row=0;row<countRow;row++){
            ObservableList<sample.Java.Controller.AlbumPane> singlePanes = FXCollections.observableArrayList();
            for(int col=0;col<MAX_ITEMS_ON_ONE_ROW;col++){
                if(numSingle >= singleList.size()){
                    break;
                }
                sample.Java.Controller.AlbumPane singlePane = new AlbumPane();
                File fileImage = new File(singleList.get(numSingle).getPathImage());
                singlePane.getAlbumImageView().setImage( new Image(fileImage.toURI().toString()));
                singlePane.getAlbumSingle().setText(singleList.get(numSingle).getName());
                singlePane.getAlbumName().setText(singleList.get(numSingle).getGenre());
                singlePane.setAlbum(singleList.get(numSingle).getAlbums().get(0));
                singlePanes.add(singlePane);
                numSingle++;
            }
            vBox.getChildren().add(new GridPaneItem(singlePanes,row));
        }
        vBox.setSpacing(20);
        vBox.setPadding(new Insets(10,10,10,10));
        scrollPane.setContent(vBox);
    }
}
