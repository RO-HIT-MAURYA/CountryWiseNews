package rohit.maurya.countrywisenews.modals;

import io.realm.RealmObject;

public class ImageModal extends RealmObject
{
    private String title;
    private String base64String;

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
}
