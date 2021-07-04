import java.util.ArrayList;

public class Driver {

    public static void main(String[] args) throws Exception {
        ArrayList<Shiur> shiurList = WebScraper.getShiurList();
        shiurList.forEach(WebScraper::downloadShiur);
    }


}
