package sample.Java.Util;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import java.io.*;


public class Mp3Utils {
    public static double getDurationWithMp3Spi(File file)  {
        int duration=0;
        try{
            FileInputStream fis     = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            Player player = new Player(bis);
            AudioFile audioFile = AudioFileIO.read(file);
            duration= audioFile.getAudioHeader().getTrackLength();
        }catch (Exception e){
            e.printStackTrace();
        }

        return duration;
    }
}
