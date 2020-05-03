package rohit.maurya.countrywisenews;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class News
{
    //@SerializedName("author")
    private String autohor;

    //@SerializedName("title")
    private String title;

    //@SerializedName("description")
    private String description;

    //@SerializedName("url")
    private String url;

    //@SerializedName("urlToImage")
    private String imageUrl;

    //@SerializedName("publishedAt")
    private String publishTime;

    //@SerializedName("content")
    private String content;

    @SerializedName("articles")
    private JsonArray jsonArray;

    public JsonArray getJsonArray()
    {
        return jsonArray;
    }

    public void setList(JsonArray list) {
        this.jsonArray = list;
    }

    public String getAutohor() {
        return autohor;
    }

    public void setAutohor(String autohor) {
        this.autohor = autohor;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
