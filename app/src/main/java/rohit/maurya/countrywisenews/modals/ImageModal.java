package rohit.maurya.countrywisenews.modals;

import io.realm.RealmObject;

public class ImageModal extends RealmObject
{
    private String title;
    private String base64String;
    private int vibrantColor;
    private int muteColor;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBase64String() {
        return base64String;
    }

    public void setBase64String(String base64String) {
        this.base64String = base64String;
    }

    public int getVibrantColor() {
        return vibrantColor;
    }

    public void setVibrantColor(int vibrantColor) {
        this.vibrantColor = vibrantColor;
    }

    public int getMuteColor() {
        return muteColor;
    }

    public void setMuteColor(int muteColor) {
        this.muteColor = muteColor;
    }
}
