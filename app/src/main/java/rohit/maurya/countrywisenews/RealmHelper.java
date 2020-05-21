package rohit.maurya.countrywisenews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmHelper {
    public static void storeDataInDb(int i, JsonArray jsonArray, App.DataStoredCallBack dataStoredCallBack) {
        Realm realm = Realm.getDefaultInstance();

        JsonObject jsonObject;

        for (JsonElement jsonElement : jsonArray) {
            jsonObject = (JsonObject) jsonElement;

            long l = realm.where(News.class).equalTo("title", App.filterString(jsonObject.get("title") + "")).count();
            //Log.e("result1Is",realmResults.asJSON()+"");
            if (l > 0)
                continue;

            Log.e("continueIs", "executed");

            realm.beginTransaction();

            //News news = realm.createObject(News.class, jsonObject.get("title")+"");
            News news = realm.createObject(News.class);
            //Log.e("StoredTitleIs",App.filterString(jsonObject.get("title")+""));
            news.setTitle(App.filterString(jsonObject.get("title") + ""));
            news.setNewsType(i);
            news.setPublishedAt(App.filterString(jsonObject.get("publishedAt") + ""));
            news.setAuthor(App.filterString(jsonObject.get("author") + ""));
            news.setDescription(App.filterString(jsonObject.get("description") + ""));
            news.setUrl(App.filterString(jsonObject.get("url") + ""));
            news.setUrlToImage(App.filterString(jsonObject.get("urlToImage") + ""));

            jsonObject = (JsonObject) jsonObject.get("source");
            news.setName(App.filterString(jsonObject.get("name") + ""));

            realm.commitTransaction();
        }

        /*RealmResults<News> realmResults = realm.where(News.class).equalTo("newsType",6).findAll();
        Log.e("realmResultIs",realmResults.size()+"");
        Log.e("asJsonIs",realmResults.asJSON()+"");*/

        dataStoredCallBack.dataStoredSuccessfully();
    }

    public static boolean isImageByteExist(String titleName) {
        Realm realm = Realm.getDefaultInstance();
        titleName = titleName.replace("\"", "");


        News news = realm.where(News.class).equalTo("title", titleName).findFirst();
        Log.e("newsIs", news + "");
        String string = news.getBase64String();
        return string == null;
    }

    public static void updateDbWithImage(String titleName, String base64String) {
        Realm realm = Realm.getDefaultInstance();


       // Log.e("newIs",news+"");
        realm.executeTransaction(realm1 -> {
            News news = realm1.where(News.class).equalTo("title", titleName).findFirst();
            news.setBase64String(base64String);
            Log.e("newNewsIs",news+"");
        });
    }

    public static String getBase64String(String titleName) {
        Realm realm = Realm.getDefaultInstance();
        News news = realm.where(News.class).equalTo("title", titleName).findFirst();

        return news.getBase64String();
    }
}
