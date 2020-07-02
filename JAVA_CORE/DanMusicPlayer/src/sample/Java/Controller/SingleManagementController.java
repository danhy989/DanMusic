package sample.Java.Controller;

import javafx.beans.binding.Bindings;
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
import sample.Java.Dao.Impl.GenreDaoImpl;
import sample.Java.Dao.Impl.SingleDaoImpl;
import sample.Java.Service.AlbumService;
import sample.Java.Util.AlertUtils;
import sample.Java.Util.IOUtils;
import sample.Java.Util.PostgresSQLConnUtils;
import sample.Java.Util.StringUtils;
import sample.Java.entities.GenreEntity;
import sample.Java.entities.SingleEntity;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class SingleManagementController extends VBox implements Initializable {

    @FXML
    private TextField id_singleID;

    @FXML
    private TextField id_singleName;

    @FXML
    private Button id_singlePathImageButton;

    @FXML
    private ComboBox id_singleGenreCombobox;

    @FXML
    private TextArea id_singleInfoTextArea;

    @FXML
    private TableView id_table;

    @FXML
    private Button id_add_action;

    @FXML
    private Button id_delete_action;

    @FXML
    private Button id_edit_action;

    @FXML
    private Button id_exits;

    @FXML
    private Button id_reset;

    @FXML
    private Label id_choose_resul_label;

    private Stage parentStage;

    private AddNewAlbumPane addNewAlbumPane;

    private File imageSingleFile;

    public SingleManagementController(Stage parentStage, AddNewAlbumPane addNewAlbumPane) throws SQLException {
        this.parentStage = parentStage;
        this.addNewAlbumPane = addNewAlbumPane;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/Single_Management.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        showData();
    }

    final SingleDaoImpl singleDao = new SingleDaoImpl();
    final GenreDaoImpl genreDao = new GenreDaoImpl();
    final FileChooser fileChooser = new FileChooser();

    private void showData() throws SQLException {
        id_table.getItems().clear();
        ObservableList<SingleEntity> list = FXCollections.observableList(singleDao.getList());
        TableColumn<SingleEntity,Long> idCol = new TableColumn<SingleEntity, Long>("ID");
        TableColumn<SingleEntity,String> nameCol = new TableColumn<SingleEntity, String>("Name");
        TableColumn<SingleEntity,String> pathCol = new TableColumn<SingleEntity, String>("Path Image");
        TableColumn<SingleEntity,String> infoCol = new TableColumn<SingleEntity, String>("Info");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        pathCol.setCellValueFactory(new PropertyValueFactory<>("pathImage"));
        infoCol.setCellValueFactory(new PropertyValueFactory<>("info"));

        id_table.getColumns().clear();
        id_table.getColumns().addAll(idCol,nameCol,pathCol,infoCol);
        id_table.setItems(list);

        id_singleGenreCombobox.getItems().clear();
        setGenreCombobox();
        id_singleGenreCombobox.getSelectionModel().select(0);
    }

    private void addData() throws SQLException {
        SingleEntity singleEntity = new SingleEntity();
        singleEntity.setName(id_singleName.getText());
        singleEntity.setInfo(id_singleInfoTextArea.getText());

        String pathImage = CopyImageFile(singleEntity.getName());
        singleEntity.setPathImage(pathImage);

        Long idGenre = genreDao.getByName(id_singleGenreCombobox.getValue().toString()).get().getId();
        singleEntity.setId_genre(idGenre);

        Boolean rs = singleDao.save(singleEntity);
        if(rs.equals(false)){
            AlertUtils.getInstance().ShowAlert(Alert.AlertType.ERROR,"Error","","The value has existed, please entering a new value. Try again!");
        }else{
            AlertUtils.getInstance().ShowAlert(Alert.AlertType.INFORMATION,"Success","","The value has already been created successfully");
        }
    }

    private ObservableList  setGenreCombobox() {
        List<GenreEntity> genreEntities = null;
        try {
            genreEntities = genreDao.getList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList genreName = FXCollections.observableArrayList();
        genreEntities.forEach(c->{
            genreName.add(c.getName());
        });
        id_singleGenreCombobox.getItems().addAll(genreName);
        return genreName;
    }

    private void reset(){
        id_singleID.clear();
        id_singleName.clear();
        id_singleInfoTextArea.clear();
        this.id_choose_resul_label.setText("...");
        this.id_choose_resul_label.setStyle("-fx-background-color:WHITE");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id_add_action.disableProperty().bind(Bindings.length(id_singleID.textProperty()).isNotEqualTo(0)
                .or(Bindings.length(id_singleName.textProperty()).isEqualTo(0)).or(Bindings.length(id_choose_resul_label.textProperty()).isNotEqualTo(2)));
        id_delete_action.disableProperty().bind(Bindings.length(id_singleID.textProperty()).isEqualTo(0));
        id_edit_action.disableProperty().bind(Bindings.length(id_singleID.textProperty()).isEqualTo(0)
                .or(Bindings.length(id_singleName.textProperty()).isEqualTo(0)));

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

        id_table.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                id_singleGenreCombobox.getItems().clear();
                SingleEntity singleEntity = (SingleEntity)newValue;
                id_singleID.setText(String.valueOf(singleEntity.getId()));
                id_singleName.setText(singleEntity.getName());
                id_singleInfoTextArea.setText(singleEntity.getInfo());
                ObservableList genreName = this.setGenreCombobox();
                int pos = 0;
                try {
                     pos= genreName.indexOf(genreDao.get(singleEntity.getId_genre()).get().getName());
                    id_singleGenreCombobox.getSelectionModel().select(pos);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                this.id_choose_resul_label.setText("OK");
                this.id_choose_resul_label.setStyle("-fx-background-color:GREEN");
            }
        });


        id_reset.setOnMouseClicked(event -> {
           this.reset();
        });

        id_singlePathImageButton.setOnMouseClicked(event -> {
            File fileImagePath = this.fileChooser.showOpenDialog(this.getScene().getWindow());
            this.imageSingleFile = fileImagePath;
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

        id_delete_action.setOnMouseClicked(event -> {
//            final String singleName = id_AlbumSingleCombobox.getValue().toString();
//            final String albumName = id_AlbumName.getText();
            final Long idSingle = Long.valueOf(id_singleID.getText());
            final String singleName = id_singleName.getText();
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

                    String sql = "delete from track t using album_track a1 , album a2, single a3 where t.id = a1.id_track and a1.id_album = a2.id and a2.id_single = a3.id and a3.id = "+idSingle;
                    stmt.executeUpdate(sql);

                    sql = "delete from single where id = "+idSingle;
                    stmt.executeUpdate(sql);

                    File directory = new File(StringUtils.getResourceString("\\Music\\"));

                    for(File f : directory.listFiles()){
                        if(f.getName().startsWith(singleName)){
                            IOUtils.deleteDirectoryStream(f.toPath());
                        }
                    }

                    //Delete folder single
                    IOUtils.deleteDirectoryStream(Paths.get(StringUtils.getResourceString("\\Images\\single\\"+singleName)));

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

        id_edit_action.setOnMouseClicked(event -> {
            boolean rs = AlertUtils.getInstance().ShowAlertConfirm("Confirm","","Are you sure want to update these value?");
            if(rs){
                SingleEntity singleEntityOld = new SingleEntity(Long.parseLong(id_singleID.getText()));
                SingleEntity singleEntityNew = new SingleEntity();
                singleEntityNew.setName(id_singleName.getText());
                singleEntityNew.setInfo(id_singleInfoTextArea.getText());

                String pathImage = CopyImageFile(singleEntityNew.getName());
                singleEntityNew.setPathImage(pathImage);
                Long idGenre = null;
                try {
                    idGenre = genreDao.getByName(id_singleGenreCombobox.getValue().toString()).get().getId();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                singleEntityNew.setId_genre(idGenre);
                try {
                    boolean rsDao = singleDao.update(singleEntityOld,singleEntityNew);
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

    }

    private String CopyImageFile(String singleName) {
        Path to = Paths.get(StringUtils.getResourceString("\\Images\\single\\"+singleName+"\\"));
        Path pathMusicFileDest = null;
        if (!Files.exists(to)) {
            try {
                Files.createDirectories(to);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        pathMusicFileDest = Paths.get(StringUtils.getResourceString("\\Images\\single\\"+singleName+"\\"+this.imageSingleFile.getName()));
        try {
            if(Files.exists(pathMusicFileDest)){
                Files.deleteIfExists(pathMusicFileDest) ;
            }
            Files.copy(imageSingleFile.toPath(),  pathMusicFileDest);

            return StringUtils.getInnerProjectResourceString(pathMusicFileDest.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
