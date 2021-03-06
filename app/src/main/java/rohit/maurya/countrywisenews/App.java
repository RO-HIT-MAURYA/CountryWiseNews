package rohit.maurya.countrywisenews;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application
{
    private static String baseUrl = "https://newsapi.org/v2/";
    public static int newsType;

    private static Retrofit retrofit;
    private static Gson gson = new GsonBuilder().create();
    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor);
    private static OkHttpClient okHttpClient = okHttpClientBuilder.build();

    public static <T> T createService(Class<T> serviceClass){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(serviceClass);
    }

    public static String getDifference(String string)
    {
        //Log.e("stringIs",string);
        string = string.replace("T"," ");
        string = string.replace("Z","");
        string = string.replace("\"","");

        Log.e("stringIs",string);
        //2020-05-06 12:28:46

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = simpleDateFormat.parse(string);
            long l = System.currentTimeMillis() - date.getTime();

            int i = (int)TimeUnit.MILLISECONDS.toDays(l);
            if (i > 0)
                return i+" day ago ";

            i = (int)TimeUnit.MILLISECONDS.toHours(l);
            if (i > 0)
                return i+" hr ago ";

            i = (int)TimeUnit.MILLISECONDS.toMinutes(l);
            if (i > 0)
                return i+" min ago ";

        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("exceptionIs",e.toString());
        }

        return  "";
    }

    public static String filterString(String string)
    {
        string = string.replace("\"","");
        string = string.replace("\\","");
        string = string.replace("\r", "");
        string = string.replace("\n", " ");

        return string;
    }

    public interface DataStoredCallBack
    {
        void dataStoredSuccessfully();
    }
}
