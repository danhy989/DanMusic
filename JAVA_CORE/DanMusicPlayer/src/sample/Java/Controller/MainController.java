package sample.Java.Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Java.Service.MusicPlayerService;


import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MainController implements Initializable {

    private static final String PLAY_CIRCLE_ALT = "PLAY_CIRCLE_ALT";
    private static final String PAUSE_CIRCLE_ALT = "PAUSE_CIRCLE_ALT";
    private static final String VOLUME_DOWN = "VOLUME_DOWN";
    private static final String VOLUME_OFF = "VOLUME_OFF";
    private static final String VOLUME_UP = "VOLUME_UP";
    private static final Paint BUTTON_ACTIVE_COLOR = Color.RED;
    private static final Paint BUTTON_INACTIVE_COLOR = Color.WHITE;

    private static Stage mainStage;

    private static double duration = 0;

    private static double currentTime = 0;


    public static void setMainStage(Stage mainStage) {
        MainController.mainStage = mainStage;
    }

    private boolean atEndOfMedia = false;

    private ObservableList<File> listObservableFile;

    @FXML
    private FontAwesomeIconView stage_close,stage_mimimum,playStopButton,repeatTrackButton,muteTrackButton;

    @FXML
    private Slider slider,sliderVolume;
    @FXML
    private ProgressBar progressBar,progressBarVolume;

    @FXML
    private Label totalDurationTime,currentDurationTime;

    private class CurrentPlayList{
        private ObservableList<File> listFile;

        public CurrentPlayList(ObservableList<File> listFile){
            this.listFile = listFile;
        }
    }

    String pathMusic = ("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/1.2-Ex 1.mp3");

    public void playAction(MouseEvent mouseEvent) {
        try{
            if(MusicPlayerService.getInstance().getIsMediaPlaying() == null
                    || MusicPlayerService.getInstance().getIsMediaPlaying().equals(MediaPlayer.Status.HALTED)
                    || MusicPlayerService.getInstance().getIsMediaPlaying().equals(MediaPlayer.Status.UNKNOWN)){
                System.out.println("start");
                MusicPlayerService.getInstance().playMusic(new File(pathMusic),Optional.of(new Pair<>(playStopButton,PLAY_CIRCLE_ALT)));
                playStopButton.setGlyphName(PAUSE_CIRCLE_ALT);
            }else
            if(MusicPlayerService.getInstance().getIsMediaPlaying().equals(MediaPlayer.Status.PLAYING)) {
                System.out.println("pause");
                MusicPlayerService.getInstance().pauseMusic();
                playStopButton.setGlyphName(PLAY_CIRCLE_ALT);
            }else
            if(MusicPlayerService.getInstance().getIsMediaPlaying().equals(MediaPlayer.Status.PAUSED)){
                System.out.println("resume");
                MusicPlayerService.getInstance().playMusic();
                playStopButton.setGlyphName(PAUSE_CIRCLE_ALT);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }

    public void nextTrackAction(MouseEvent mouseEvent){
        try{
            MusicPlayerService.getInstance().resetTrack();
            playStopButton.setGlyphName(PAUSE_CIRCLE_ALT);
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }

    public void previousTrackAction(MouseEvent mouseEvent){
        try{
            MusicPlayerService.getInstance().resetTrack();
            playStopButton.setGlyphName(PAUSE_CIRCLE_ALT);
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }

    public void repeatTrackAction(MouseEvent mouseEvent){
        try{
            if(repeatTrackButton.getFill()== BUTTON_INACTIVE_COLOR){
                MusicPlayerService.setStatusDuration(Duration.ZERO);
                repeatTrackButton.setFill(BUTTON_ACTIVE_COLOR);
            }else{
                MusicPlayerService.setStatusDuration(Duration.ONE);
                repeatTrackButton.setFill(BUTTON_INACTIVE_COLOR);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }

    public void muteUnmuteAction(MouseEvent mouseEvent){
        try{
            String muteTrackButtonGlyphName = muteTrackButton.getGlyphName();
            if(muteTrackButtonGlyphName.equals(VOLUME_UP)){
                MusicPlayerService.muteTrack(true);
                sliderVolume.setValue(0);
                muteTrackButton.setGlyphName(VOLUME_OFF);
                System.out.printf("Volume: %s\n",VOLUME_OFF);
            }else if(muteTrackButtonGlyphName.equals(VOLUME_OFF)){
                MusicPlayerService.muteTrack(false);
                sliderVolume.setValue(sliderVolume.getMax());
                muteTrackButton.setGlyphName(VOLUME_UP);
                System.out.printf("Volume: %s\n",VOLUME_UP);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }

    public void stageAction(MouseEvent mouseEvent){
        Object source = mouseEvent.getSource();
        try{
            if (stage_close.equals(source)) {
                System.out.println("Close the window");
                System.exit(1);
            }
            else if (stage_mimimum.equals(source)) {
                System.out.println("Minimum the window");
                mainStage.setIconified(true);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
        Setup music player's items
         */
        MusicPlayerService.setInstance(totalDurationTime,currentDurationTime,progressBar
                ,progressBarVolume,slider,sliderVolume);


        File file1 = new File("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/1.2-Ex 1.mp3");
        File file2 = new File("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/people.mp3");
        File file3 = new File("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/The Heart Wants What It Wants - Selena G.mp3");
        listObservableFile = FXCollections.observableArrayList(file1,file2,file3);
        HashMap<File,Integer> mapFile = new HashMap<>();
        int index = 0;
        for(File f:listObservableFile){
            mapFile.put(f,index++);
        }

        MusicPlayerService.getInstance().setMapFile(mapFile);
        MusicPlayerService.getInstance().setListObservableFile(listObservableFile);



    }



}
