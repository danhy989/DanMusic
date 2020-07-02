package sample.Java.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Java.Dao.Impl.AlbumDaoImpl;
import sample.Java.Dao.Impl.AlbumTrackDaoImpl;
import sample.Java.Dao.Impl.SingleDaoImpl;
import sample.Java.Dao.Impl.TrackDaoImpl;
import sample.Java.Util.*;
import sample.Java.entities.AlbumEntity;
import sample.Java.entities.SingleEntity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AlbumManagementController extends VBox implements Initializable {

    @FXML
    private Label id_choose_resul_label;

    @FXML
    private TextField id_AlbumID,id_AlbumName;

    @FXML
    private Button  id_AlbumPathImageButton,id_resetButton,id_add_action,id_delete_action,id_edit_action,id_exits;

    @FXML
    private ComboBox id_AlbumSingleCombobox;

    @FXML
    private CheckBox idAlbumHotTrackCheckBox;

    @FXML
    private DatePicker id_releaseDatePicker;

    @FXML
    private TableView id_table;

    private Stage parentStage;

    private AddNewAlbumPane addNewAlbumPane;

    private File imageFile;

    public AlbumManagementController(Stage parentStage, AddNewAlbumPane addNewAlbumPane) throws SQLException {
        this.parentStage = parentStage;
        this.addNewAlbumPane = addNewAlbumPane;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/Album_Management.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        showData();
    }

    final FileChooser fileChooser = new FileChooser();
    final AlbumDaoImpl albumDao = new AlbumDaoImpl();
    final SingleDaoImpl singleDao = new SingleDaoImpl();
    final TrackDaoImpl trackDao = new TrackDaoImpl();
    final AlbumTrackDaoImpl albumTrackDao = new AlbumTrackDaoImpl();

    private void showData() throws SQLException {
        id_table.getItems().clear();
        ObservableList<AlbumEntity> list = FXCollections.observableList(albumDao.getList());

        TableColumn<AlbumEntity,Long> idCol = new TableColumn<AlbumEntity, Long>("ID");
        TableColumn<AlbumEntity,String> nameCol = new TableColumn<AlbumEntity, String>("Name");
        TableColumn<AlbumEntity,String> pathCol = new TableColumn<AlbumEntity, String>("Path Image");
        TableColumn<AlbumEntity,String> releaseTimeCol = new TableColumn<AlbumEntity, String>("Release Time");
        TableColumn<AlbumEntity,Boolean> isHotTrackCol = new TableColumn<AlbumEntity, Boolean>("Is hot track?");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        pathCol.setCellValueFactory(new PropertyValueFactory<>("pathImage"));
        releaseTimeCol.setCellValueFactory(new PropertyValueFactory<>("releaseTime"));
        isHotTrackCol.setCellValueFactory(new PropertyValueFactory<>("album_hot_track"));

        id_table.getColumns().clear();
        id_table.getColumns().addAll(idCol,nameCol,pathCol,releaseTimeCol,isHotTrackCol);
        id_table.setItems(list);

        id_AlbumSingleCombobox.getItems().clear();
        setSingleCombobox();
        id_AlbumSingleCombobox.getSelectionModel().select(0);
    }

    private void reset(){
        id_AlbumID.clear();
        id_AlbumName.clear();
        idAlbumHotTrackCheckBox.setSelected(false);
        id_releaseDatePicker.setValue(LocalDate.now());
        this.id_choose_resul_label.setText("...");
        this.id_choose_resul_label.setStyle("-fx-background-color:WHITE");
    }

    private ObservableList  setSingleCombobox() {
        List<SingleEntity> singleEntities = null;
        try {
            singleEntities = singleDao.getList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList singleNames = FXCollections.observableArrayList();
        singleEntities.forEach(c->{
            singleNames.add(c.getName());
        });
        id_AlbumSingleCombobox.getItems().addAll(singleNames);
        return singleNames;
    }

    private void addData() throws SQLException {
        AlbumEntity albumEntity = new AlbumEntity();
        albumEntity.setName(id_AlbumName.getText());

        String pathImage = CopyImageFile(id_AlbumSingleCombobox.getValue().toString(),albumEntity.getName());
        albumEntity.setPathImage(pathImage);

        Long idGenre = singleDao.getByName(id_AlbumSingleCombobox.getValue().toString()).get().getId();
        albumEntity.setId_single(idGenre);

        albumEntity.setAlbum_hot_track(idAlbumHotTrackCheckBox.isSelected());

        albumEntity.setReleaseTime(TimeUtils.convertDateTimePickerToString(id_releaseDatePicker));

        Boolean rs = albumDao.save(albumEntity);
        if(rs.equals(false)){
            AlertUtils.getInstance().ShowAlert(Alert.AlertType.ERROR,"Error","","The value has existed, please entering a new value. Try again!");
        }else{
            AlertUtils.getInstance().ShowAlert(Alert.AlertType.INFORMATION,"Success","","The value has already been created successfully");
        }
    }

    private String CopyImageFile(String singleName,String albumName) {
        Path to = Paths.get(StringUtils.getResourceString("\\Images\\single\\"+singleName+"\\albums\\"+albumName+"\\"));
        Path pathMusicFileDest = null;
        if (!Files.exists(to)) {
            try {
                Files.createDirectories(to);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        pathMusicFileDest = Paths.get(StringUtils.getResourceString("\\Images\\single\\"+singleName+"\\albums\\"+albumName+"\\"+this.imageFile.getName()));
        try {
            if(Files.exists(pathMusicFileDest)){
                Files.deleteIfExists(pathMusicFileDest) ;
            }
            Files.copy(imageFile.toPath(),  pathMusicFileDest);

            return StringUtils.getInnerProjectResourceString(pathMusicFileDest.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id_releaseDatePicker.setValue(LocalDate.now());
        id_exits.setOnMouseClicked(event -> {
            Stage thisStage = (Stage)this.getScene().getWindow();
            thisStage.close();
            parentStage.show();
            //reload data
            try {
                this.addNewAlbumPane.loadDataInit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        id_resetButton.setOnMouseClicked(event -> {
            this.reset();
        });

        id_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                id_AlbumSingleCombobox.getItems().clear();
                AlbumEntity albumEntity = (AlbumEntity) newValue;
                id_AlbumID.setText(String.valueOf(albumEntity.getId()));
                id_AlbumName.setText(albumEntity.getName());
                idAlbumHotTrackCheckBox.setSelected(albumEntity.isAlbum_hot_track());
                try {
                    Date date = TimeUtils.convertStringToDate(albumEntity.getReleaseTime());
                    id_releaseDatePicker.setValue(TimeUtils.convertToLocalDateViaInstant(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ObservableList genreName = this.setSingleCombobox();
                int pos = 0;
                try {
                    pos= genreName.indexOf(singleDao.get(albumEntity.getId_single()).get().getName());
                    id_AlbumSingleCombobox.getSelectionModel().select(pos);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                this.id_choose_resul_label.setText("OK");
                this.id_choose_resul_label.setStyle("-fx-background-color:GREEN");
            }
        });

        id_AlbumPathImageButton.setOnMouseClicked(event -> {
            File fileImagePath = this.fileChooser.showOpenDialog(this.getScene().getWindow());
            this.imageFile = fileImagePath;
            this.id_choose_resul_label.setText("OK");
            this.id_choose_resul_label.setStyle("-fx-background-color:GREEN");
        });

        id_add_action.setOnMouseClicked(event -> {
            try {
                addData();
                this.showData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        id_edit_action.setOnMouseClicked(event -> {
            boolean rs = AlertUtils.getInstance().ShowAlertConfirm("Confirm","","Are you sure want to update these value?");
            if(rs){
                AlbumEntity albumEntityOld = new AlbumEntity(Long.parseLong(id_AlbumID.getText()));
                AlbumEntity albumEntityNew = new AlbumEntity();
                albumEntityNew.setName(id_AlbumName.getText());
                albumEntityNew.setAlbum_hot_track(idAlbumHotTrackCheckBox.isSelected());
                albumEntityNew.setReleaseTime(TimeUtils.convertDateTimePickerToString(id_releaseDatePicker));

                String pathImage = CopyImageFile(id_AlbumSingleCombobox.getValue().toString(),albumEntityNew.getName());
                albumEntityNew.setPathImage(pathImage);
                Long idSingle = null;
                try {
                    idSingle = singleDao.getByName(id_AlbumSingleCombobox.getValue().toString()).get().getId();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                albumEntityNew.setId_single(idSingle);
                try {
                    boolean rsDao = albumDao.update(albumEntityOld,albumEntityNew);
                    if(rsDao){
                        AlertUtils.getInstance().ShowAlert(Alert.AlertType.INFORMATION,"Success","","The value has already been updated successfully");
                        this.showData();
                    }else{
                        AlertUtils.getInstance().ShowAlert(Alert.AlertType.ERROR,"Error","","Can not update these value. Try again!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        id_delete_action.setOnMouseClicked(event -> {
            final String singleName = id_AlbumSingleCombobox.getValue().toString();
            final String albumName = id_AlbumName.getText();

            boolean rsDao = false;
            boolean rs = AlertUtils.getInstance().ShowAlertConfirm("Confirm","","Are you sure want to delete these value?");
            if(rs){
                Connection conn = null;
                Statement stmt = null;
                try {
                        conn = PostgresSQLConnUtils.getConnection();
                        conn.setAutoCommit(false);
                        stmt = conn.createStatement(
                                ResultSet.TYPE_SCROLL_INSENSITIVE,
                                ResultSet.CONCUR_UPDATABLE);


                        //Delete Album
                    String sql = "delete from track t using album_track a , album a2 where t.id = a.id_track and a.id_album = a2.id and a2.id = "+Long.parseLong(id_AlbumID.getText());
                        stmt.executeUpdate(sql);
                        //Delete Track
                     sql = "delete from album where id = "+Long.parseLong(id_AlbumID.getText());

                        stmt.executeUpdate(sql);

                        //Delete folder album
                        IOUtils.deleteDirectoryStream(Paths.get(StringUtils.getResourceString("\\Images\\single\\"+singleName+"\\albums\\"+albumName+"\\")));
                        //Delete folder track
                        IOUtils.deleteDirectoryStream(Paths.get(StringUtils.getResourceString("\\Music\\"+singleName+"-"+albumName+"\\")));

                    conn.commit();
                    rsDao = true;

                } catch (IOException | SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Rolling back data here....");
                    try{
                        if(conn!=null)
                            conn.rollback();
                    }catch(SQLException se2){
                        se2.printStackTrace();
                    }
                }
                finally {
                    try{
                        if(stmt!=null)
                            stmt.close();
                    }catch(SQLException se2){
                    }// nothing we can do
                    try{
                        if(conn!=null)
                            conn.close();
                    }catch(SQLException se){
                        se.printStackTrace();
                    }
                }
                if(rsDao){
                    AlertUtils.getInstance().ShowAlert(Alert.AlertType.INFORMATION,"Success","","The value has already been deleted successfully");
                    try {
                        this.showData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    this.reset();
                }else{
                    AlertUtils.getInstance().ShowAlert(Alert.AlertType.ERROR,"Error","","Can not delete these value. Try again!");
                }
            }
        });
    }
}
