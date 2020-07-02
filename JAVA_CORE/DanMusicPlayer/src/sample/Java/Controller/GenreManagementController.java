package sample.Java.Controller;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.Java.Dao.Impl.GenreDaoImpl;
import sample.Java.Util.AlertUtils;
import sample.Java.entities.GenreEntity;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Node;

public class GenreManagementController extends VBox implements Initializable {

    @FXML
    private TableView id_table;

    @FXML
    private Button id_exits;

    @FXML
    private TextField id_genreID;

    @FXML
    private TextField id_genreName;

    @FXML
    private Button id_reset;

    @FXML
    private Button id_add_action;

    @FXML
    private Button id_delete_action;

    @FXML
    private Button id_edit_action;


    private Stage parentStage;

    private AddNewAlbumPane addNewAlbumPane;

    public GenreManagementController(Stage parentStage,AddNewAlbumPane addNewAlbumPane) throws SQLException {
        this.parentStage = parentStage;
        this.addNewAlbumPane = addNewAlbumPane;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../../Resources/Fxml/Genre_Management.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try{
            loader.load();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        showData();

    }

    GenreDaoImpl genreDao = new GenreDaoImpl();

    private void showData() throws SQLException {
        id_table.getItems().clear();
        ObservableList<GenreEntity> list = FXCollections.observableList(genreDao.getList());
        TableColumn<GenreEntity,Long> idCol = new TableColumn<GenreEntity, Long>("ID");
        TableColumn<GenreEntity,String> nameCol = new TableColumn<GenreEntity, String>("Name");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        id_table.getColumns().clear();
        id_table.getColumns().addAll(idCol,nameCol);
        id_table.setItems(list);

    }

    private void addData() throws SQLException {
        GenreEntity genreEntity = new GenreEntity();
        genreEntity.setName(id_genreName.getText());

        Boolean rs = genreDao.save(genreEntity);
        if(rs.equals(false)){
            AlertUtils.getInstance().ShowAlert(Alert.AlertType.ERROR,"Error","","The value has existed, please entering a new value. Try again!");
        }else{
            AlertUtils.getInstance().ShowAlert(Alert.AlertType.INFORMATION,"Success","","The value has already been created successfully");
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        id_add_action.disableProperty().bind(Bindings.length(id_genreID.textProperty()).isNotEqualTo(0).or(Bindings.length(id_genreName.textProperty()).isEqualTo(0)));
        id_delete_action.disableProperty().bind(Bindings.length(id_genreID.textProperty()).isEqualTo(0));
        id_edit_action.disableProperty().bind(Bindings.length(id_genreID.textProperty()).isEqualTo(0).or(Bindings.length(id_genreName.textProperty()).isEqualTo(0)));

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
                GenreEntity genreEntity = (GenreEntity)newValue;
                id_genreID.setText(String.valueOf(genreEntity.getId()));
                id_genreName.setText(genreEntity.getName());
            }
        });

        id_reset.setOnMouseClicked(event -> {
            id_genreID.clear();
            id_genreName.clear();
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
            boolean rs = AlertUtils.getInstance().ShowAlertConfirm("Confirm","","Are you sure want to delete these value?");
            if(rs){
                GenreEntity genreEntity = new GenreEntity(Long.parseLong(id_genreID.getText()),id_genreName.getText());
                try {
                    boolean rsDao = genreDao.delete(genreEntity);
                    if(rsDao){
                        AlertUtils.getInstance().ShowAlert(Alert.AlertType.INFORMATION,"Success","","The value has already been deleted successfully");
                        this.showData();
                    }else{
                        AlertUtils.getInstance().ShowAlert(Alert.AlertType.ERROR,"Error","","Can not delete these value. Try again!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        id_edit_action.setOnMouseClicked(event -> {
            boolean rs = AlertUtils.getInstance().ShowAlertConfirm("Confirm","","Are you sure want to update these value?");
            if(rs){
                GenreEntity genreEntity = new GenreEntity(Long.parseLong(id_genreID.getText()),id_genreName.getText());
                try {
                    boolean rsDao = genreDao.update(genreEntity,genreEntity);
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
}
