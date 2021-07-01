import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.gargoylesoftware.htmlunit.UnexpectedPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class WebScraper{
    static final String HOME_PAGE 
        = "https://www.simpletoremember.com/articles/a/48ways/";
    static WebClient webClient;
    static ArrayList<Shiur> shiurList = new ArrayList<Shiur>(50);

    public static void main(String[] args) throws Exception {
        webClient = initWebClient();
        shiurList = getShiurList(HOME_PAGE);
        shiurList.forEach(WebScraper::downloadShiur);
    }

    private static WebClient initWebClient() {
        WebClient webClient = new WebClient(); 
        //turn off HtmlUnit's console logging
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        return webClient;
    }

    /**
     * Scrapes the home page for the links to each sub-page of the 48 Ways 
     * 
     * @param The home page for the 48 Ways Shiurim
     * @return A list of wayToWisdom shiurim
     * @throws Exception
     */
    private static ArrayList<Shiur> getShiurList(String homepage) throws Exception {
        System.out.println("Scraping download urls...\n");
        
        ArrayList<Shiur> ways = new ArrayList<Shiur>(50);
        HtmlPage page = webClient.getPage(homepage);
        List<HtmlAnchor> anchorList = page.getAnchors()
                .stream().filter(anchor -> anchor.asText().contains("Way #"))
                .collect(Collectors.toList());

        for (HtmlAnchor link : anchorList) {
            String name = link.asText();
            String shiurPageURL = page.getFullyQualifiedUrl(link.getHrefAttribute())
                    .toString();
            shiurPageURL = shiurPageURL.replace("\n","").replace("\r", "");
            System.out.println("Finding dl url for: " + name);
            String shiurDownloadURL = findDownloadLink(shiurPageURL);
            System.out.println("Download link: " + shiurDownloadURL);
            System.out.println();
            Shiur shiur = new Shiur(name, shiurPageURL, shiurDownloadURL);
            ways.add(shiur);
        }
        return ways;
    }
    
    /**
     * Finds the download link on the individual shiur page
     * @param mainPageURL
     * @return
     */
    private static String findDownloadLink(String mainPageURL) {
        HtmlPage page;
        String downloadURL = "";
        try {
            page = WebScraper.webClient.getPage(mainPageURL);
            page = page.getAnchorByText("CLICK HERE for the MP3 of this").click();
            //use regex to find download link since "CLICK TO DOWNLOAD" anchor 
            //text is hidden inside <img> so HtmlUnit won't find anchor by text
            Pattern linkRegex = Pattern.compile("audio.*?\\.mp3");
            Matcher m = linkRegex.matcher(page.asXml());
            if (m.find())
                downloadURL = m.group();
        } catch(IOException e) {}
        return downloadURL;
    }

    public static void downloadShiur(Shiur shiur) {
        System.out.println("Download...\n");
        System.out.println("Downloading: " + "Way #" + shiur.getNumber() + " " 
                + shiur.getName());
        FileOutputStream outstream;
        try {
            outstream = new FileOutputStream(new File(shiur.getName() +".mp3"));
            UnexpectedPage page = webClient.getPage(shiur.getDownloadURL());
            InputStream instream  = page.getWebResponse().getContentAsStream();
            byte[] buffer = new byte[4096];
            while (instream.available() > 0) {
                instream.read(buffer);
                outstream.write(buffer, 0, buffer.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Download Complete");
    }
}