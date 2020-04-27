package sample.Java.Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Java.DTO.Albums;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.Single;
import sample.Java.DTO.Track;
import sample.Java.Service.AlbumService;
import sample.Java.Service.MusicPlayerService;
import sample.Java.Service.PlaylistService;
import sample.Java.Service.SingleService;


import java.awt.event.ActionEvent;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

public class MainController implements Initializable {

    private static final String VOLUME_OFF = "VOLUME_OFF";
    private static final String VOLUME_UP = "VOLUME_UP";
    private static final Paint BUTTON_ACTIVE_COLOR = Color.RED;
    private static final Paint BUTTON_INACTIVE_COLOR = Color.WHITE;

    private static Stage mainStage;


    public static void setMainStage(Stage mainStage) {
        MainController.mainStage = mainStage;
    }

    @FXML
    private FontAwesomeIconView stage_close,stage_mimimum,playStopButton,repeatTrackButton,muteTrackButton;

    @FXML
    private Slider slider,sliderVolume;
    @FXML
    private ProgressBar progressBar,progressBarVolume;

    @FXML
    private Label totalDurationTime,currentDurationTime;

    @FXML
    private ScrollPane trackListScrollPane,albumListScrollPane,singleScrollPane;

    String pathMusic = ("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/1.2-Ex 1.mp3");

    public void playAction(MouseEvent mouseEvent) {
        try{
            if(MusicPlayerService.getInstance().getIsMediaPlaying() == null
                    || MusicPlayerService.getInstance().getIsMediaPlaying().equals(MediaPlayer.Status.HALTED)){
                System.out.println("start");
                MusicPlayerService.getInstance().playMusic(new File(pathMusic));
            }else
            if(MusicPlayerService.getInstance().getIsMediaPlaying().equals(MediaPlayer.Status.PLAYING)) {
                System.out.println("pause");
                MusicPlayerService.getInstance().pauseMusic();
            }else
            if(MusicPlayerService.getInstance().getIsMediaPlaying().equals(MediaPlayer.Status.PAUSED)){
                System.out.println("resume");
                MusicPlayerService.getInstance().playMusic();
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }

    public void nextTrackAction(MouseEvent mouseEvent){
        try{
            MusicPlayerService.getInstance().nextTrack();
        }catch (Exception e){
            e.printStackTrace();
            System.out.printf("Error: %s\n",e.getMessage());
        }
    }

    public void previousTrackAction(MouseEvent mouseEvent){
        try{
            MusicPlayerService.getInstance().previousTrack();
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
        MusicPlayerService.setInstance(playStopButton,repeatTrackButton,muteTrackButton
                ,totalDurationTime,currentDurationTime,progressBar
                ,progressBarVolume,slider,sliderVolume);

        PlaylistService.setInstance(trackListScrollPane);
        AlbumService.setInstance(albumListScrollPane);
        SingleService.setInstance(singleScrollPane);

        /*
        test
         */
        /*
        Set list track!
         */
        Albums album1,album2,album3;

        File file1 = new File("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/1.2-Ex 1.mp3");
        File file2 = new File("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/people.mp3");
        File file3 = new File("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/The Heart Wants What It Wants - Selena G.mp3");

        Track track1 = new Track("Track 1","03:33",file1);
        Track track2 = new Track("Track 2","02:35",file2);
        Track track3 = new Track("Track 3","04:22",file3);
        Track track4 = new Track("Track 2","02:35",file2);
        Track track5 = new Track("Track 3","04:22",file3);
        Track track6 = new Track("Track 2","02:35",file2);
        Track track7 = new Track("Track 3","04:22",file3);
        Track track8 = new Track("Track 2","02:35",file2);
        Track track9 = new Track("Track 3","04:22",file3);

        List<Track> tracks = new ArrayList<>(Arrays.asList(track1, track2, track3,track4,track5,track6,track7,track8,track9));

        List<Track> tracks2 = new ArrayList<>(Arrays.asList(track1, track4, track3));

        List<Track> tracks3 = new ArrayList<>(Arrays.asList(track1, track4));

         album1 = new Albums("Album A","SonTungMTP","2020-09-12","sample/Resources/Images/unnamed.jpg",tracks);
         album2 = new Albums("Album B","Justin","2020-03-14","sample/Resources/Images/unnamed.jpg",tracks2);
         album3 = new Albums("Album C","Amira","2020-01-14","sample/Resources/Images/unnamed.jpg",tracks3);

         List<Albums> albums = new ArrayList<>(Arrays.asList(album1,album2,album3));

        AlbumService.getInstance().setContentAlbums(albums);
        PlaylistService.getInstance().setContentPlaylist(album1);

        Single single1 =
                new Single("Son Tung","Hiphop","Ca si nguoi thai binh",
                        "sample/Resources/Images/sontung.png",album2,null);

        Single single2 = new Single("Taylor","Ballad","Ca si nguoi nuoc ngoai",
                "sample/Resources/Images/taylor.jpg",album3,null);

        SingleService.getInstance().setContentSingle(new ArrayList<>(Arrays.asList(single1,single2)));

    }



}
