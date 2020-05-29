package rohit.maurya.countrywisenews;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.realm.Realm;
import rohit.maurya.countrywisenews.modals.ImageModal;
import rohit.maurya.countrywisenews.modals.NewsModal;

public class RealmHelper {
    public static void storeDataInDb(int i, JsonArray jsonArray, App.DataStoredCallBack dataStoredCallBack) {
        Realm realm = Realm.getDefaultInstance();

        JsonObject jsonObject;

        for (JsonElement jsonElement : jsonArray) {
            jsonObject = (JsonObject) jsonElement;

            //Log.e("storeDataInDb",App.filterString(jsonObject.get("title")+""));
            long l = realm.where(NewsModal.class).equalTo("title", App.filterString(jsonObject.get("title") + "")).count();
            //Log.e("result1Is",realmResults.asJSON()+"");
            if (l > 0)
                continue;

            Log.e("continueIs", "executed");

            realm.beginTransaction();

            NewsModal newsModal = realm.createObject(NewsModal.class);
            ImageModal imageModal = realm.createObject(ImageModal.class);

            imageModal.setTitle(App.filterString(jsonObject.get("title") + ""));
            imageModal.setBase64String("");

            newsModal.setTitle(App.filterString(jsonObject.get("title") + ""));
            newsModal.setNewsType(i);
            newsModal.setPublishedAt(App.filterString(jsonObject.get("publishedAt") + ""));
            newsModal.setAuthor(App.filterString(jsonObject.get("author") + ""));
            newsModal.setDescription(App.filterString(jsonObject.get("description") + ""));
            newsModal.setUrl(App.filterString(jsonObject.get("url") + ""));
            newsModal.setUrlToImage(App.filterString(jsonObject.get("urlToImage") + ""));

            jsonObject = (JsonObject) jsonObject.get("source");
            newsModal.setName(App.filterString(jsonObject.get("name") + ""));

            newsModal.setBookMark(false);

            realm.commitTransaction();
        }

        /*RealmResults<News> realmResults = realm.where(News.class).equalTo("newsType",6).findAll();
        Log.e("realmResultIs",realmResults.size()+"");
        Log.e("asJsonIs",realmResults.asJSON()+"");*/

        dataStoredCallBack.dataStoredSuccessfully();
    }

    public static boolean isImageByteExist(String titleName) {
        Realm realm = Realm.getDefaultInstance();
        titleName = App.filterString(titleName);

        //Log.e("isImageByteExist",titleName);
        ImageModal imageModal = realm.where(ImageModal.class).equalTo("title", titleName).findFirst();
        String string = imageModal.getBase64String();
        return string == null || string.isEmpty();
    }

    public static void storeImageData(String titleName, String base64String, int vibrantColor, int muteColor, App.DataStoredCallBack dataStoredCallBack) {
        titleName = App.filterString(titleName);
        Realm realm = Realm.getDefaultInstance();
        String tempString = titleName;
        // Log.e("newIs",news+"");
        realm.executeTransaction(realm1 -> {
            ImageModal imageModal = realm1.where(ImageModal.class).equalTo("title", tempString).findFirst();
            imageModal.setBase64String(base64String);

            imageModal.setVibrantColor(vibrantColor);
            imageModal.setMuteColor(muteColor);
            dataStoredCallBack.dataStoredSuccessfully();
            //Log.e("newNewsIs", imageModal +"");
        });
    }

    public static String getBase64String(String titleName) {
        Realm realm = Realm.getDefaultInstance();
        titleName = App.filterString(titleName);
        ImageModal imageModal = realm.where(ImageModal.class).equalTo("title", titleName).findFirst();
        if (imageModal == null)
            return "";

        titleName = imageModal.getBase64String();
        if (titleName == null || titleName.isEmpty())
            return "";
        else
            return imageModal.getBase64String();
    }

    public static int[] getColors(String titleName) {
        int[] ints = new int[2];
        Realm realm = Realm.getDefaultInstance();
        titleName = App.filterString(titleName);

        ImageModal imageModal = realm.where(ImageModal.class).equalTo("title", titleName).findFirst();
        if (imageModal != null) {
            ints[0] = imageModal.getVibrantColor();
            ints[1] = imageModal.getMuteColor();

        }
        return ints;
    }

    public static void updateBookmark(String titleName, boolean bool) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> {
            NewsModal newsModal = realm1.where(NewsModal.class).equalTo("title", titleName).findFirst();
            if (newsModal != null)
                newsModal.setBookMark(bool);
        });
    }
}
