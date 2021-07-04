import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Driver {
    static Path tempDir = initDir("Temp");
    static Path finDir = initDir("48 Ways - R' Noach Weinberg");
    
    public static void main(String[] args) throws Exception {
        ArrayList<Shiur> shiurList = WebScraper.getShiurList();
        shiurList.forEach(shiur -> WebScraper.downloadShiur(shiur, tempDir));
        for (File shiur : tempDir.toFile().listFiles()) {
            TagEditor.fixTags(shiur, finDir);
        }
        removeDir(tempDir);
        Desktop.getDesktop().open(finDir.toFile());
    }
    
    private static Path initDir(String dir) {
        Path path = Paths.get(dir);
        if (!Files.exists(path))
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return path;
    }
    
    private static void removeDir(Path path) throws IOException {
        System.out.println("Removing \"Temp\" directory and files...\n");
        File dir = path.toFile();
        for (File f : dir.listFiles()) {
            Files.delete(f.toPath());
        }
        Files.delete(dir.toPath());
        System.out.println("Complete.");
    }
}
