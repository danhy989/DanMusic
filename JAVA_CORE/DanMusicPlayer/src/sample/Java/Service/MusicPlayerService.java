package sample.Java.Service;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Track;
import javafx.util.Duration;
import javafx.util.Pair;
import sample.Java.Controller.MainController;
import sample.Java.DTO.CurrentPlayList;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

public class MusicPlayerService  {
    private static final MusicPlayerService instance = new MusicPlayerService();
    private static MediaPlayer mediaPlayer;
    private static final int VOLUME_SLIDER_BAR_MAX_WIDTH = 100;
    private static double MIN_CHANGE = 0.5 ;
    private static Duration statusDuration = Duration.UNKNOWN;

    private static final String PLAY_CIRCLE_ALT = "PLAY_CIRCLE_ALT";
    private static final String PAUSE_CIRCLE_ALT = "PAUSE_CIRCLE_ALT";

    Slider slider,sliderVolume;
    ProgressBar progressBar, progressBarVolume;
    Label totalDurationTime,currentDurationTime;
    FontAwesomeIconView playStopButton,repeatTrackButton,muteTrackButton;

    private ObservableList<File> listObservableFile;
    HashMap<File, Integer > mapFile;
    boolean continuePlaying = true;
    int currentIndex= 0 ;

    private static int numPlayingTrackOnList = 0;

    public static int getNumPlayingTrackOnList() {
        return numPlayingTrackOnList;
    }

    private MusicPlayerService(){ }
    public static MusicPlayerService getInstance(){
        return instance;
    }
    public static void setInstance(FontAwesomeIconView playStopButton,FontAwesomeIconView repeatTrackButton
            ,FontAwesomeIconView muteTrackButton,
                                   Label totalDurationTime,Label currentDurationTime,
                                   ProgressBar progressBar,ProgressBar progressBarVolume,
                                                 Slider slider,Slider sliderVolume){
        instance.playStopButton=playStopButton;
        instance.repeatTrackButton = repeatTrackButton;
        instance.muteTrackButton = muteTrackButton;
        instance.totalDurationTime = totalDurationTime;
        instance.currentDurationTime = currentDurationTime;
        instance.progressBar = progressBar;
        instance.progressBarVolume = progressBarVolume;
        instance.slider = slider;
        instance.sliderVolume = sliderVolume;
    }

    public boolean isPlayingTrack(File f){
        int temp = IntStream.range(0,listObservableFile.size())
                .filter(value -> listObservableFile.get(value).toURI().toString().equals(f.toURI().toString())).findFirst().getAsInt();
        if(numPlayingTrackOnList == temp){
            return true;
        }
        return false;
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

                //end
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
        for(File f:currentPlaylist.getListFile()){
            mapFile.put(f,index++);
        }
        if(mapFile.size()!=currentPlaylist.getListFile().size()){
            throw new Exception("File nhac bi trung");
        }
        setMapFile(mapFile);
        setListObservableFile(currentPlaylist.getListFile());

        int posStart = 0;

        if(fileStart!=null){
            posStart  = IntStream.range(0,listObservableFile.size())
                    .filter(value -> listObservableFile.get(value).toURI().toString().
                            equals(fileStart.toURI().toString())).findFirst().getAsInt();
        }
        playMusic(currentPlaylist.getListFile().get(posStart));
    }
}
