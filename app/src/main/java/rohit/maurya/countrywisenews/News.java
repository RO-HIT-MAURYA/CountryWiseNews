package rohit.maurya.countrywisenews;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class News extends RealmObject
{
    //@PrimaryKey
    private String title;
    private int newsType;//0 = business,  1 = entertainment, 2 = health, 3 = science, 4 = sports, 5 = technology, 6 = home
    private String publishedAt;
    private String author;
    private String description;
    private String name;
    private String url;
    private String urlToImage;

    /*public News(String title, int newsType, String publishedAt, String author, String description, String name, String url, String urlToImage) {
        this.title = title;
        this.newsType = newsType;
        this.publishedAt = publishedAt;
        this.author = author;
        this.description = description;
        this.name = name;
        this.url = url;
        this.urlToImage = urlToImage;
    }*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }
}
