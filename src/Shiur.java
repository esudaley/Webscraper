
public class Shiur{
    private static final String AUTHOR = "R' Noach Weinberg";
    private String name;
    private int number;
    private String mainURL;
    private String downloadURL;
    
    public Shiur(String name, String mainURL, String downloadURL) {
        setName(name);
        setNumber(name.replaceAll("\\D", ""));
        setMainURL(mainURL);
        setDownloadUrl(downloadURL);
    }

    private void setName(String input) {
        name = input.replaceAll("Way\\s*#\\d*\\s*", "");
    }

    public String getName() {
        return name;
    }

    private void setNumber(String num) {
        number = Integer.parseInt(num);
    }
    
    public int getNumber() {
        return number;
    }
    
    private void setMainURL(String url) {
        mainURL = url;
    }
    
    public String getMainURL() {
        return mainURL;
    }

    private void setDownloadUrl(String url) {
        downloadURL = "https://" + url;
    }
    
    public String getDownloadURL() {
        return downloadURL;
    }
    
    public static String getAuthor() {
        return AUTHOR;
    }

    @Override
    public String toString() {
        return "Name: " + name 
                + "\nNumber: " + number 
                + "\nMainURL: " + mainURL 
                + "\nDownloadURL: "
                + downloadURL
                + "\n";
    }
}
