package sample.Java.Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.Java.Controller.Home.HomePane;
import sample.Java.Controller.Library.ArtistPane;
import sample.Java.Controller.Library.PlaylistPane;
import sample.Java.DTO.Album;
import sample.Java.DTO.Single;
import sample.Java.Service.AlbumService;
import sample.Java.Service.MusicPlayerService;
import sample.Java.Service.PlaylistService;
import sample.Java.Service.SingleService;
import sample.Java.Static.DataStaticLoader;
import sample.Java.Util.EffectUtils;


import java.io.File;
import java.net.URL;
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
    private FontAwesomeIconView stage_close,stage_mimimum,playStopButton,repeatTrackButton,muteTrackButton,
            previousTrackbutton,nextTrackbutton;

    @FXML
    private Slider slider,sliderVolume;
    @FXML
    private ProgressBar progressBar,progressBarVolume;

    @FXML
    private Label totalDurationTime,currentDurationTime,nameTrackPlaying,singleTrackPlaying,homeButton,
            libMadeForYouButton,libArtistsButton;

    @FXML
    private ImageView imageTrackPlaying;

    @FXML
    public BorderPane mainPane;



    String pathMusic = ("D:/Source/me/Java-UIT/Music-player/JAVA_CORE/DanMusicPlayer/src/sample/Resources/Music/1.2-Ex 1.mp3");

    public void homeAction(MouseEvent mouseEvent){
        if(!mainPane.getCenter().getClass().equals(HomePane.class)){
            HomePane homePane = new HomePane();
            mainPane.setCenter(homePane);
            List<Album> albumList = AlbumService.getInstance().convertFromAlbumDao(DataStaticLoader.getInstance().getAlbumEntities());
            AlbumService.getInstance().setContentAlbums(albumList);
            PlaylistService.getInstance().setContentPlaylist(albumList.get(0));

            List<Single> singles = SingleService.getInstance().convertAlbumHotTrackFromSingleDao();
            SingleService.getInstance().setContentSingle(singles);
        }
    }

    public void musicPlayingShowButton(MouseEvent mouseEvent){
        SceneMusicPlayingController musicPlayingController = new SceneMusicPlayingController();
        mainPane.setCenter(musicPlayingController);
    }

    public void libraryShowPlaylistAction(MouseEvent mouseEvent){
        if(!mainPane.getCenter().getClass().equals(PlaylistPane.class)) {
            PlaylistPane playlistPane = new PlaylistPane();
            mainPane.setCenter(playlistPane);
        }
    }

    public void libraryShowArtistsAction(MouseEvent mouseEvent){
        if(!mainPane.getCenter().getClass().equals(ArtistPane.class)) {
            ArtistPane artistPane = new ArtistPane();
            mainPane.setCenter(artistPane);
        }
    }

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

    private void setEffectForMainPage(){
        EffectUtils.setCusorEffect(stage_close);
        EffectUtils.setCusorEffect(stage_mimimum);
        EffectUtils.setCusorEffect(playStopButton);
        EffectUtils.setCusorEffect(repeatTrackButton);
        EffectUtils.setCusorEffect(muteTrackButton);
        EffectUtils.setCusorEffect(previousTrackbutton);
        EffectUtils.setCusorEffect(nextTrackbutton);
        EffectUtils.setCusorEffect(homeButton);
        EffectUtils.setCusorEffect(libMadeForYouButton);
        EffectUtils.setCusorEffect(libArtistsButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        HomePane homePane = new HomePane();
        mainPane.setCenter(homePane);

        this.setEffectForMainPage();
        /*
        Setup music player's items
         */
        MusicPlayerService.setInstance(playStopButton,repeatTrackButton,muteTrackButton
                ,totalDurationTime,currentDurationTime,progressBar
                ,progressBarVolume,slider,sliderVolume,imageTrackPlaying,nameTrackPlaying,singleTrackPlaying);

        DataStaticLoader.getInstance();
        List<Album> albumList = AlbumService.getInstance().convertFromAlbumDao(DataStaticLoader.getInstance().getAlbumEntities());
        AlbumService.getInstance().setContentAlbums(albumList);
        PlaylistService.getInstance().setContentPlaylist(albumList.get(0));

        List<Single> singles = SingleService.getInstance().convertAlbumHotTrackFromSingleDao();
        SingleService.getInstance().setContentSingle(singles);


    }



}
