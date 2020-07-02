package sample.Java.Controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Java.Controller.Home.HomePane;
import sample.Java.Controller.Library.ArtistPane;
import sample.Java.Dao.Dao;
import sample.Java.Dao.Impl.*;
import sample.Java.Service.AlbumService;
import sample.Java.Util.StringUtils;
import sample.Java.entities.*;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.*;

public class AddNewAlbumPane extends VBox implements Initializable {

    @FXML
    private ComboBox id_genre_album_combo_box;

    @FXML
    private ComboBox id_artist_album_combo_box;

    @FXML
    private ComboBox id_name_album_combo_box;

    @FXML
    private Button id_choose_file_add_new_album_action;

    @FXML
    private Button id_add_tracks_action;

    @FXML
    private ListView id_tracks_listView;

    @FXML
    private Button id_files_reset_action;

    @FXML
    private Button id_add_genre_action;

    @FXML
    private Button id_add_single_action;

    @FXML
    private Button id_add_album_action;

    private List<File> listTracks = new ArrayList<>();

    public AddNewAlbumPane() throws SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/add_new_album.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            //this.getStylesheets().add(getClass().getResource("../../Resources/Css/hover.css").toExternalForm());
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        loadDataInit();
        //EffectUtils.setCusorEffect(this);
    }

    GenreDaoImpl genreDao = new GenreDaoImpl();
    SingleDaoImpl singleEntityDao = new SingleDaoImpl();
    AlbumDaoImpl albumDao = new AlbumDaoImpl();
    TrackDaoImpl trackDao = new TrackDaoImpl();
    AlbumTrackDaoImpl albumTrackDao = new AlbumTrackDaoImpl();

    final FileChooser fileChooser = new FileChooser();

    public void loadDataInit() throws SQLException {
        /*
            clear
         */
        id_genre_album_combo_box.getItems().clear();

        /*
        Init combobox for genre
         */
        List<GenreEntity>  genreEntities = genreDao.getList();
        ObservableList genreName = FXCollections.observableArrayList();
        genreEntities.forEach(c->{
            genreName.add(c.getName());
        });

        id_genre_album_combo_box.getItems().addAll(genreName);
        id_genre_album_combo_box.getSelectionModel().select(0);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        id_add_tracks_action.disableProperty().bind(Bindings.size(id_tracks_listView.getItems()).isEqualTo(0));
        id_files_reset_action.disableProperty().bind(id_add_tracks_action.disableProperty());

        id_genre_album_combo_box.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            /*
            Init combobox for artist
             */
            if(newValue!=null){
                List<SingleEntity> singleEntities = null;
                try {
                    singleEntities = singleEntityDao.getListByGenreName(newValue.toString());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                ObservableList singleNames = FXCollections.observableArrayList();
                singleEntities.forEach(c->{
                    singleNames.add(c.getName());
                });

                id_artist_album_combo_box.getItems().clear();
                id_name_album_combo_box.getItems().clear();
                id_artist_album_combo_box.getItems().addAll(singleNames);
                id_artist_album_combo_box.getSelectionModel().select(0);

            }

        });

        id_artist_album_combo_box.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            /*
            Init combobox for artist
             */
            if(newValue != null){
                if(!newValue.toString().trim().equals("")){
                    List<AlbumEntity> entities = null;
                    try {
                        entities = albumDao.getListBySingleName(newValue.toString());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    ObservableList objects = FXCollections.observableArrayList();
                    entities.forEach(c->{
                        objects.add(c.getName());
                    });
                    id_name_album_combo_box.getItems().clear();
                    id_name_album_combo_box.getItems().addAll(objects);
                    id_name_album_combo_box.getSelectionModel().select(0);
                }
            }
        });

        id_choose_file_add_new_album_action.setOnMouseClicked(event -> {
            listTracks = fileChooser.showOpenMultipleDialog(this.getParent().getScene().getWindow());
            if(listTracks!=null){
                for(File file : listTracks){
                    id_tracks_listView.getItems().add(file.getName());
                }
            }

        });

        id_files_reset_action.setOnMouseClicked(event -> {
            id_tracks_listView.getItems().clear();
        });


        id_add_tracks_action.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("These tracks will be added into database?");

            Optional<ButtonType> option = alert.showAndWait();

            if(option.get() != null && option.get() == ButtonType.OK){
                String genreName = "";
                String artistName = "";
                String albumName = "";

                genreName = id_genre_album_combo_box.getValue().toString();
                artistName = id_artist_album_combo_box.getValue().toString();
                albumName = id_name_album_combo_box.getValue().toString();
                if(!genreName.trim().equals("") && !artistName.trim().equals("") && !albumName.trim().equals("")){
                    if(listTracks!=null){
                        for(File file :listTracks){
                            Path to = Paths.get(StringUtils.getResourceString("\\Music\\"+artistName+"-"+albumName+"\\"));
                            Path pathMusicFileDest = null;
                            if (!Files.exists(to)) {
                                try {
                                    Files.createDirectories(to);
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }
                            }
                            pathMusicFileDest = Paths.get(StringUtils.getResourceString("\\Music\\"+artistName+"-"+albumName+"\\"+file.getName()));
                            try {
                                if(Files.exists(pathMusicFileDest)){
                                    Files.deleteIfExists(pathMusicFileDest) ;
                                }
                                Files.copy(file.toPath(),  pathMusicFileDest);
                            /*
                            Save into database
                             */
                                if(!id_name_album_combo_box.isDisabled()){
                                    //Th1: thêm bài hát vào album đã tồn tại
                                    AlbumService.getInstance().addTrackToExistAlbum(albumName,file,pathMusicFileDest.toString());
                                }

                            } catch (FileAlreadyExistsException faee) {
                                faee.printStackTrace();
                                System.out.println("File exists");
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });

        id_add_genre_action.setOnMouseClicked(event -> {
            try {
                Stage thisStage = (Stage)this.getScene().getWindow();
                GenreManagementController genreManagementController = new GenreManagementController(thisStage,this);
                Scene scene = new Scene(genreManagementController);
                Stage stage = new Stage();
                stage.setTitle("Opening");
                stage.setScene(scene);
                stage.show();
                ((Node)event.getSource()).getScene().getWindow().hide();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });

        id_add_single_action.setOnMouseClicked(event -> {
            Stage thisStage = (Stage)this.getScene().getWindow();
            SingleManagementController singleManagementController = null;
            try {
                singleManagementController = new SingleManagementController(thisStage,this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(singleManagementController);
            Stage stage = new Stage();
            stage.setTitle("Opening");
            stage.setScene(scene);
            stage.show();
            ((Node)event.getSource()).getScene().getWindow().hide();
        });

        id_add_album_action.setOnMouseClicked(event -> {
            Stage thisStage = (Stage)this.getScene().getWindow();
            AlbumManagementController albumManagementController = null;
            try {
                albumManagementController = new AlbumManagementController(thisStage,this);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(albumManagementController);
            Stage stage = new Stage();
            stage.setTitle("Opening");
            stage.setScene(scene);
            stage.show();
            ((Node)event.getSource()).getScene().getWindow().hide();
        });
    }
}
