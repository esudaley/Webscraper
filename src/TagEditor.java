import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * 
 *
 */
public class TagEditor {

    final static String ARTIST = "R' Noach Weinberg";
    final static String AlBUM = "SimpleToRemember.com";

    public static void fixTags(File file, Path directory) throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
        
        Mp3File  mp3file = new Mp3File(file);
        ID3v2 tag = mp3file.getId3v2Tag();
        
        System.out.println("Fixing tags...");
        
        System.out.println("Original title: " + tag.getTitle());
        String title = fixTitle(tag);
        System.out.println("Setting title: " + title);
        tag.setTitle(title);
        tag.setArtist(ARTIST);
        tag.setAlbum(AlBUM);
       
        mp3file.save(directory.toString() + File.separator + file.getName());
        System.out.println("Tags fixed\n");
    }

    private static String fixTitle(ID3v2 tag) {
        String title = tag.getTitle();
        title = title.replaceAll(".*-\\s", "");
        return title.replaceFirst(" ", " - ");
    }
}
