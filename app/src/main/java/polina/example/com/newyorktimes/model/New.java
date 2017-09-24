package polina.example.com.newyorktimes.model;

/**
 * Created by polina on 9/21/17.
 */

public class New {
    String title;
    String description;
    String imageURL;
    int width;
    int height;

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    String desk;
    String webUrl;

    public New(String title, String description, String imageURL, String webUrl, String desk) {
        this.title = title;
        this.description = description;
        this.imageURL = imageURL;
        this.webUrl = webUrl;
        this.desk = desk;
    }

    public void setSize(int w, int h) {
        width = w;
        height = h;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
