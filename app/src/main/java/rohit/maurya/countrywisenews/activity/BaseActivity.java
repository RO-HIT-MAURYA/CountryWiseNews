package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
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

public class BaseActivity extends AppCompatActivity {
    private ActivityBaseBinding activityBaseBinding;
    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private SettingFragment settingFragment;
    private FragmentManager fragmentManager;
    public  Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);

        context = this;

        fragmentManager = getSupportFragmentManager();

        homeFragment = HomeFragment.newInstance();
        fragmentLoader(homeFragment);
    }

    public void fragmentLoader(Fragment fragment) {
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .addToBackStack("")
                .replace(R.id.frameLayout, fragment).commit();
    }

    @Override
    public void onBackPressed() {

        if (homeFragment.isVisible())
            finish();
        else if (categoryFragment != null && categoryFragment.isVisible())
        {
            App.jsonArray = HomeFragment.jsonArray;
            decideBottomSelection(activityBaseBinding.homeLayout);
        }
        else if (settingFragment !=null && settingFragment.isVisible())
            decideBottomSelection(activityBaseBinding.categoryLayout);

        fragmentManager.popBackStack();
    }

    /*private void onClick(View view) {
        if (view == activityBaseBinding.button)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        startActivity(new Intent(this,BaseActivity.class));
        finish();
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.day_night) {
            String string = item.getTitle().toString();
            if (string.equalsIgnoreCase("day mode")) {
                item.setTitle("Night Mode");
                setTheme(android.R.style.Theme_Light);
            } else {
                item.setTitle("day mode");
                setTheme(android.R.style.ThemeOverlay_Material_Dark);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBottomBarClick(View view) {
        if (homeFragment.isVisible() && view == activityBaseBinding.homeLayout)
            return;
        else if (categoryFragment != null && categoryFragment.isVisible() && view == activityBaseBinding.categoryLayout)
            return;
        else if (settingFragment != null && settingFragment.isAdded() && view == activityBaseBinding.settingLayout)
            return;

        if (view == activityBaseBinding.homeLayout)
            fragmentLoader(homeFragment);
        else if (view == activityBaseBinding.categoryLayout) {
            if (categoryFragment == null)
                categoryFragment = CategoryFragment.newInstance();
            fragmentLoader(categoryFragment);
        } else {
            if (settingFragment == null)
                settingFragment = SettingFragment.newInstance();
            fragmentLoader(settingFragment);
        }

        decideBottomSelection(view);
    }

    private void decideBottomSelection(View view) {
        if (view == activityBaseBinding.homeLayout) {
            activityBaseBinding.homeImage.setColorFilter(getResources().getColor(R.color.colorBlack));
            activityBaseBinding.categoryImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.settingImage.setColorFilter(getResources().getColor(R.color.grey5));
        } else if (view == activityBaseBinding.categoryLayout) {
            activityBaseBinding.homeImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.categoryImage.setColorFilter(getResources().getColor(R.color.colorBlack));
            activityBaseBinding.settingImage.setColorFilter(getResources().getColor(R.color.grey5));
        } else {
            activityBaseBinding.homeImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.categoryImage.setColorFilter(getResources().getColor(R.color.grey5));
            activityBaseBinding.settingImage.setColorFilter(getResources().getColor(R.color.colorBlack));
        }

        Log.e("conditionIs","executed");
    }
}
