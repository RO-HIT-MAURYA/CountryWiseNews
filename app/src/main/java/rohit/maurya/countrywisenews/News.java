package rohit.maurya.countrywisenews;

import io.realm.RealmObject;

public class News extends RealmObject
{
    private String author;
    private String title;
    private String description;
    private String url;
    private String imageUrl;
    private String publishTime;
    private String content;
}
