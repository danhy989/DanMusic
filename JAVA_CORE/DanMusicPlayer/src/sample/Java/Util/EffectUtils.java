package sample.Java.Util;

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class EffectUtils{

    public static void setCusorEffect(Node t){
        t.setOnMouseEntered(event -> {
            t.getScene().setCursor(Cursor.HAND);
            if(t.getClass() == Label.class){
                t.setStyle("-fx-font-size: 1.5em ");
            }
        });

        t.setOnMouseExited(event -> {
            t.getScene().setCursor(Cursor.DEFAULT);
            if(t.getClass() == Label.class){
                t.setStyle("-fx-background-color: null ");
            }
        });
    }
}
