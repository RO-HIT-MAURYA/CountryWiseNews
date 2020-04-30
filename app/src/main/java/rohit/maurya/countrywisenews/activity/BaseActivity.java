package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rohit.maurya.countrywisenews.ApiInterface;
import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.News;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.databinding.ActivityBaseBinding;
import rohit.maurya.countrywisenews.fragment.CategoryFragment;
import rohit.maurya.countrywisenews.fragment.HomeFragment;
import rohit.maurya.countrywisenews.fragment.SettingFragment;

public class BaseActivity extends AppCompatActivity
{
    private ActivityBaseBinding activityBaseBinding;
    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseBinding = DataBindingUtil.setContentView(this,R.layout.activity_base);

        getNews();
    }

    public void fragmentLoader(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .replace(R.id.frameLayout, fragment).commit();
    }

    /*private void onClick(View view) {
        if (view == activityBaseBinding.button)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        startActivity(new Intent(this,BaseActivity.class));
        finish();
    }*/

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

    public void onBottomBarClick(View view)
    {

        if (view == activityBaseBinding.homeLayout)
        {
            activityBaseBinding.homeImage.setColorFilter(getResources().getColor(R.color.colorBlack));
            activityBaseBinding.categoryImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.settingImage.setColorFilter(getResources().getColor(R.color.grey5));

            fragmentLoader(homeFragment);
        }
        else if (view == activityBaseBinding.categoryLayout)
        {
            activityBaseBinding.homeImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.categoryImage.setColorFilter(getResources().getColor(R.color.colorBlack));
            activityBaseBinding.settingImage.setColorFilter(getResources().getColor(R.color.grey5));

            if (categoryFragment == null)
                categoryFragment = CategoryFragment.newInstance();
            fragmentLoader(categoryFragment);
        }
        else
        {
            activityBaseBinding.homeImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.categoryImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.settingImage.setColorFilter(getResources().getColor(R.color.colorBlack));

            if (settingFragment == null)
                settingFragment = SettingFragment.newInstance();
            fragmentLoader(settingFragment);
        }
    }
}
