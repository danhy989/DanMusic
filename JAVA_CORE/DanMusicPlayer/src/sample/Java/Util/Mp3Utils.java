package sample.Java.Util;

import com.sun.org.slf4j.internal.LoggerFactory;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Mp3Utils {

    static {
        Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
        Logger.getLogger("org.jaudiotagger.tag").setLevel(Level.OFF);
        Logger.getLogger("org.jaudiotagger.audio.mp3.MP3File").setLevel(Level.OFF);
        Logger.getLogger("org.jaudiotagger.tag.id3.ID3v23Tag").setLevel(Level.OFF);
    }

    public static double getDurationWithMp3Spi(File file)  {
        int duration=0;
        try{
            AudioFile audioFile = AudioFileIO.read(file);
            duration= audioFile.getAudioHeader().getTrackLength();
        }catch (Exception e){
            e.printStackTrace();
        }

        return duration;
    }
}
