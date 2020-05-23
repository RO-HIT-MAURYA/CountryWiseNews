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

        ImageModal imageModal = realm.where(ImageModal.class).equalTo("title", titleName).findFirst();
        String string = imageModal.getBase64String();
        return string == null || string.isEmpty();
    }

    public static void storeBase64String(String titleName, String base64String) {
        Realm realm = Realm.getDefaultInstance();


       // Log.e("newIs",news+"");
        realm.executeTransaction(realm1 -> {
            ImageModal imageModal = realm1.where(ImageModal.class).equalTo("title", titleName).findFirst();
            imageModal.setBase64String(base64String);
            //Log.e("newNewsIs", imageModal +"");
        });
    }

    public static String getBase64String(String titleName) {
        Realm realm = Realm.getDefaultInstance();
        titleName = App.filterString(titleName);
        ImageModal imageModal = realm.where(ImageModal.class).equalTo("title", titleName).findFirst();

        return imageModal.getBase64String();
    }
}
