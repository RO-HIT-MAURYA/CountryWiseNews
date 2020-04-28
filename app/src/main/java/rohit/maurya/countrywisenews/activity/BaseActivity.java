package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rohit.maurya.countrywisenews.ApiInterface;
import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.News;
import rohit.maurya.countrywisenews.R;

public class BaseActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int i = item.getItemId();
        if (i == R.id.day_night)
        {
            String string = item.getTitle().toString();
            if (string.equalsIgnoreCase("day mode"))
            {
                item.setTitle("Night Mode");
                setTheme(android.R.style.Theme_Light);
            }
            else
            {
                item.setTitle("day mode");
                setTheme(android.R.style.ThemeOverlay_Material_Dark);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
