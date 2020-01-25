package rohit.maurya.countrywisenews;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNews();
    }

    private void getNews()
    {
        ApiInterface apiInterface = App.createService(ApiInterface.class);

        Call<News> call = apiInterface.getNews();
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response)
            {
                if (response.isSuccessful())
                {
                    List<JsonObject> list =  response.body().getList();
                    Log.e("responseIs",list+"");

                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("errorIs",t.getMessage());
            }
        });
    }
}
