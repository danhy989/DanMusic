package sample.Java.Util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.lang.reflect.Method;
import java.util.Optional;

public class AlertUtils {
    private static AlertUtils instance = null;
    private Alert alert;
    private AlertUtils(){};

    public static AlertUtils getInstance(){
        if(instance==null){
            instance = new AlertUtils();
        }
        return instance;
    }

    public void ShowAlert(AlertType alertType, String title, String header, String content){
        alert = new Alert(alertType);
        alert.setTitle(title);
        if(header.isEmpty()){
            alert.setHeaderText(null);
        }else{
            alert.setHeaderText(header);
        }
        alert.setContentText(content);
        alert.show();
    }

    public boolean ShowAlertConfirm(String title, String header, String content){
        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(title);
        if(header.isEmpty()){
            alert.setHeaderText(null);
        }else{
            alert.setHeaderText(header);
        }
        alert.setContentText(content);
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() != null && option.get() == ButtonType.OK){
            return true;
        }
        return false;
    }
}
