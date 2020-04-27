package sample.Java.Service;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import sample.Java.Controller.SinglePane;
import sample.Java.DTO.Single;

import java.util.List;

public class SingleService {
    private static final SingleService instance = new SingleService();
    private SingleService(){};
    public static SingleService getInstance(){return instance;}

    public static void setInstance(ScrollPane singleScrollPane){
        instance.singleScrollPane = singleScrollPane;
    }

    ScrollPane singleScrollPane;

    public void setContentSingle(List<Single> singles){
        singleScrollPane.setContent(null);
        VBox vBox = new VBox();
        for(int i=0;i<singles.size();i++){
            SinglePane singlePane = new SinglePane();
            singlePane.getName().setText(singles.get(i).getName());
            singlePane.getGenre().setText(singles.get(i).getName());
            singlePane.getImage().setImage(new Image(singles.get(i).getPathImage()));
            singlePane.setSingle(singles.get(i));
            vBox.getChildren().add(singlePane);
        }
        vBox.setPadding(new Insets(0,5,0,5));
        singleScrollPane.setContent(vBox);
        singleScrollPane.setFitToWidth(true);
        singleScrollPane.setPannable(true);
    }
}
