package sample.Java.Service;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import sample.Java.Controller.TrackPane;
import sample.Java.DTO.CurrentPlayList;
import sample.Java.DTO.MusicplayerTracksInfo;

import java.io.File;
import java.util.*;
import java.util.stream.IntStream;

public class MusicPlayerService  {
    private static final MusicPlayerService instance = new MusicPlayerService();
    private static MediaPlayer mediaPlayer;
    private static final int VOLUME_SLIDER_BAR_MAX_WIDTH = 100;
    private static double MIN_CHANGE = 0.5 ;
    private static Duration statusDuration = Duration.UNKNOWN;
    private static int numPlayingTrackOnList = 0;

    private static final String PLAY_CIRCLE_ALT = "PLAY_CIRCLE_ALT";
    private static final String PAUSE_CIRCLE_ALT = "PAUSE_CIRCLE_ALT";

    Slider slider,sliderVolume;
    ProgressBar progressBar, progressBarVolume;
    Label totalDurationTime,currentDurationTime,nameTrackPlaying,singleTrackPlaying;
    FontAwesomeIconView playStopButton,repeatTrackButton,muteTrackButton;
    ImageView imageTrackPLaying;

    private ObservableList<File> listObservableFile;
    private MusicplayerTracksInfo musicplayerTracksInfo;
    HashMap<File, Integer > mapFile;
    int currentIndex= 0 ;

    private MusicPlayerService(){ }
    public static MusicPlayerService getInstance(){
        return instance;
    }
    public static void setInstance(FontAwesomeIconView playStopButton, FontAwesomeIconView repeatTrackButton
            , FontAwesomeIconView muteTrackButton,
                                   Label totalDurationTime, Label currentDurationTime,
                                   ProgressBar progressBar, ProgressBar progressBarVolume,
                                   Slider slider, Slider sliderVolume, ImageView imageTrackPLaying,
                                   Label nameTrackPlaying, Label singleTrackPlaying){
        instance.playStopButton=playStopButton;
        instance.repeatTrackButton = repeatTrackButton;
        instance.muteTrackButton = muteTrackButton;
        instance.totalDurationTime = totalDurationTime;
        instance.currentDurationTime = currentDurationTime;
        instance.progressBar = progressBar;
        instance.progressBarVolume = progressBarVolume;
        instance.slider = slider;
        instance.sliderVolume = sliderVolume;
        instance.imageTrackPLaying = imageTrackPLaying;
        instance.nameTrackPlaying=nameTrackPlaying;
        instance.singleTrackPlaying=singleTrackPlaying;
    }

    public void playMusic(File f) throws Exception {
        if(mediaPlayer != null){
            mediaPlayer.stop();
        }



        String URI = f.toURI().toString();
        if(null!=listObservableFile && listObservableFile.size()>1){
            numPlayingTrackOnList = IntStream.range(0,listObservableFile.size())
                    .filter(value -> listObservableFile.get(value).toURI().toString().equals(URI)).findFirst().getAsInt();
        }
        chooseColorTrackPane();
            Media media = new Media(URI);
            try
            {
                playStopButton.setGlyphName(PAUSE_CIRCLE_ALT);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        if(mapFile!=null && mapFile.size()>1){
                            int index = mapFile.get(f);
                            int indexNextTrack = index+1;
                            if(indexNextTrack < listObservableFile.size()){
                                try {
                                    if(statusDuration.equals(Duration.ZERO)){
                                        System.out.println(statusDuration);
                                        mediaPlayer.seek(Duration.ZERO);
                                        return;
                                    }
                                    playMusic(listObservableFile.get(indexNextTrack));
                                    currentIndex = indexNextTrack;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                currentIndex =0;
                            }
                        }else{
                            mediaPlayer.dispose();
                            playStopButton.setGlyphName(PLAY_CIRCLE_ALT);
                        }
                    }
                });

                mediaPlayer.setAutoPlay(true);
                setupMusicPLayerBottomBar();
            }catch (Exception e)
            {
                System.out.println( e.getMessage() );
                try {
                    throw new Exception("Can't play this track");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
        }
    }

    private void chooseColorTrackPane() {
        VBox vboxPlaylist = (VBox) PlaylistService.getInstance().getTrackListScrollPane().getContent();
        for(int i=0;i<listObservableFile.size();i++){
            TrackPane trackPane = (TrackPane) vboxPlaylist.getChildren().get(i);
            if(listObservableFile.get(numPlayingTrackOnList).getAbsolutePath().equals(listObservableFile.get(i).getAbsolutePath())){
                trackPane.getTrackNumOrder().setTextFill(Color.RED);
                trackPane.getTrackTotalDuration().setTextFill(Color.RED);
                trackPane.getTrackName().setTextFill(Color.RED);
            }else{
                trackPane.getTrackNumOrder().setTextFill(Color.BLACK);
                trackPane.getTrackTotalDuration().setTextFill(Color.BLACK);
                trackPane.getTrackName().setTextFill(Color.BLACK);
            }
        }
    }

    public void playMusic() throws Exception {
        try
        {
            mediaPlayer.play();
            playStopButton.setGlyphName(PAUSE_CIRCLE_ALT);
        }
        catch (Exception e)
        {
            System.out.println( e.getMessage() );
            throw new Exception("Can't play this track");
        }
    }

    public void stopMusic() throws Exception {
        try
        {
            if(mediaPlayer != null){
                mediaPlayer.stop();
                playStopButton.setGlyphName(PLAY_CIRCLE_ALT);
            }
        }
        catch (Exception e)
        {
            System.out.println( e.getMessage() );
            throw new Exception("Can't stop this track");
        }
    }

    public void pauseMusic() throws Exception {
        try
        {
            if(mediaPlayer != null){
                mediaPlayer.pause();
                playStopButton.setGlyphName(PLAY_CIRCLE_ALT);
            }
        }
        catch (Exception e)
        {
            System.out.println( e.getMessage() );
            throw new Exception("Can't pause this track");
        }
    }

    public void nextTrack(){
        if(mediaPlayer != null){
            if(listObservableFile.size()>1 && statusDuration!=Duration.ZERO){
                try {
                    if(numPlayingTrackOnList<listObservableFile.size()-1){
                        stopMusic();
                        this.playMusic(listObservableFile.get(++numPlayingTrackOnList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                this.resetTrack();
            }
        }
    }

    public void previousTrack(){
        if(mediaPlayer != null){
            if(listObservableFile.size()>1 && statusDuration!=Duration.ZERO){
                try {
                    if(numPlayingTrackOnList>0){
                        stopMusic();
                        this.playMusic(listObservableFile.get(--numPlayingTrackOnList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                this.resetTrack();
            }
        }
    }

    public double getDuration(){
        return mediaPlayer.getMedia().getDuration().toMillis();
    }

    public double getCurrentTime(){
        return mediaPlayer.getCurrentTime().toMillis();
    }

    public double getTotalDuration(){
        return mediaPlayer.getTotalDuration().toSeconds();
    }

    public ReadOnlyObjectProperty<Duration> getTotalDurationProperty(){
        return mediaPlayer.totalDurationProperty();
    }

    public MediaPlayer.Status getIsMediaPlaying(){
        if(mediaPlayer != null){
            return mediaPlayer.getStatus();
        }
        return null;

    }
    public void setupMusicPLayerBottomBar(){
        if(mediaPlayer != null){
            mediaPlayer.setOnReady(()->{

                /*
        Binding volume
         */
                progressBarVolume.setMaxWidth(VOLUME_SLIDER_BAR_MAX_WIDTH);
                progressBarVolume.prefWidthProperty().bind(sliderVolume.widthProperty());
                progressBarVolume.progressProperty().bind(sliderVolume.valueProperty().divide(VOLUME_SLIDER_BAR_MAX_WIDTH));

                sliderVolume.setValue(VOLUME_SLIDER_BAR_MAX_WIDTH/2);

                double totalDuration = mediaPlayer.getTotalDuration().toSeconds();
                /*
                Setup volume default
                 */
                mediaPlayer.setVolume(sliderVolume.getValue()/progressBarVolume.getMaxWidth());
            /*
            Set label time
             */
                currentDurationTime.textProperty().bind(Bindings.createStringBinding(()->{
                    Duration time = mediaPlayer.getCurrentTime();
                    return String.format("%d:%02d",(int)time.toSeconds()/60,(int)time.toSeconds()%60);
                },mediaPlayer.currentTimeProperty()));
                totalDurationTime.setText(new String(String.format("%d:%02d",(int)totalDuration/60,(int)totalDuration%60)));

            /*
            Binding the media player's total duration with slider's total duration
             */
                slider.maxProperty().bind(
                        Bindings.createDoubleBinding(
                                () -> totalDuration,
                                mediaPlayer.totalDurationProperty()));

            /*
            Handle playing music event!
             */
                if(mediaPlayer!=null ){
                    mediaPlayer.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
                        if (!slider.isValueChanging()) {
                            slider.setValue(newValue.toSeconds());
                        }
                    });
                }
                /*
                Binding the slider with the progress bar!
                 */
                progressBar.prefWidthProperty().bind(slider.widthProperty());
                progressBar.progressProperty().bind(slider.valueProperty().divide(mediaPlayer.getTotalDuration().toSeconds()));
            /*
            Handle slider's changing event!
                Dragged event.
                Press event.
             */
                slider.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
                    if (! isChanging) {
                        mediaPlayer.seek(Duration.seconds(slider.getValue()));
                    }
                });
                slider.valueProperty().addListener((obs, oldValue, newValue) -> {
                    if (! slider.isValueChanging()) {
                        double currentTime = mediaPlayer.getCurrentTime().toSeconds();
                        if (Math.abs(currentTime - newValue.doubleValue()) > MIN_CHANGE) {
                            mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
                        }
                    }
                });

                mediaPlayer.volumeProperty().bind(sliderVolume.valueProperty().divide(progressBarVolume.getMaxWidth()));

                //Track playing info
                imageTrackPLaying.imageProperty().bind(musicplayerTracksInfo.getImageTracksObservableList().get(numPlayingTrackOnList));
               nameTrackPlaying.textProperty().bind(musicplayerTracksInfo.getNameTracksObservableList().get(numPlayingTrackOnList));
                singleTrackPlaying.textProperty().bind(musicplayerTracksInfo.getSingleTracksObservableList().get(numPlayingTrackOnList));



            });
        }
    }

    public void resetTrack(){
        if(mediaPlayer!=null && !this.getIsMediaPlaying().equals(MediaPlayer.Status.UNKNOWN)){
            mediaPlayer.seek(Duration.millis(0));
            mediaPlayer.play();
            playStopButton.setGlyphName(PAUSE_CIRCLE_ALT);
        }
    }

    public static void setStatusDuration(Duration statusDuration) {
        MusicPlayerService.statusDuration = statusDuration;
    }

    public static void muteTrack(boolean b){
        if(mediaPlayer != null) mediaPlayer.setMute(b);
    }


    public void setMapFile(HashMap<File, Integer> mapFile) {
        this.mapFile = mapFile;
    }

    public void setListObservableFile(ObservableList<File> listObservableFile) {
        this.listObservableFile = listObservableFile;
    }

    public void startPlaylist(CurrentPlayList currentPlaylist,File fileStart) throws Exception {
        HashMap<File,Integer> mapFile = new HashMap<>();
        int index = 0;
        musicplayerTracksInfo = (MusicplayerTracksInfo) currentPlaylist.getCurrentPlaylist().get("musicplayerTracksInfo");
        ObservableList<File> listfiles = (ObservableList<File>)currentPlaylist.getCurrentPlaylist().get("listFiles");
        for(File f:listfiles){
            mapFile.put(f,index++);
        }
        if(mapFile.size()!=listfiles.size()){
            throw new Exception("File nhac bi trung");
        }
        setMapFile(mapFile);
        setListObservableFile(listfiles);

        int posStart = 0;

        if(fileStart!=null){
            posStart  = IntStream.range(0,listObservableFile.size())
                    .filter(value -> listObservableFile.get(value).toURI().toString().
                            equals(fileStart.toURI().toString())).findFirst().getAsInt();
        }
        playMusic(listfiles.get(posStart));
    }
}
