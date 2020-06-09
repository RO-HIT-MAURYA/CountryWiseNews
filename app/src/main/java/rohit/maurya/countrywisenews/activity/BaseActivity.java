package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.databinding.ActivityBaseBinding;
import rohit.maurya.countrywisenews.fragment.BookMarkFragment;
import rohit.maurya.countrywisenews.fragment.CategoryFragment;
import rohit.maurya.countrywisenews.fragment.HomeFragment;
import rohit.maurya.countrywisenews.fragment.SettingFragment;

public class BaseActivity extends AppCompatActivity {
    private ActivityBaseBinding activityBaseBinding;
    private HomeFragment homeFragment;
    private CategoryFragment categoryFragment;
    private SettingFragment settingFragment;
    private FragmentManager fragmentManager;
    private BookMarkFragment bookMarkFragment;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBaseBinding = DataBindingUtil.setContentView(this, R.layout.activity_base);

        context = this;

        fragmentManager = getSupportFragmentManager();

        homeFragment = HomeFragment.newInstance();
        fragmentLoader(homeFragment);
    }

    private long l;

    public void fragmentLoader(Fragment fragment) {

        if (fragment.isVisible())
            return;

        if (System.currentTimeMillis() - l < 321)
            return;
        l = System.currentTimeMillis();

        fragmentManager.beginTransaction().setCustomAnimations(R.anim.anim_enter, R.anim.anim_exit)
                .addToBackStack("")
                .replace(R.id.frameLayout, fragment).commit();

        if (fragment instanceof HomeFragment) {
            App.newsType = 6;
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 1)
                for (int i = 0; i < count; i++)
                    onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {

        fragmentManager.popBackStack();

        new Handler().postDelayed(() ->
        {
            if (fragmentManager.getBackStackEntryCount() == 0)
                finish();

            if (homeFragment.isVisible())
                decideBottomSelection(activityBaseBinding.homeLayout);
            else if (categoryFragment != null && categoryFragment.isVisible())
                decideBottomSelection(activityBaseBinding.categoryLayout);
            else if (bookMarkFragment != null && bookMarkFragment.isVisible())
                decideBottomSelection(activityBaseBinding.bookMarkLayout);
            else if (settingFragment != null && settingFragment.isVisible())
                decideBottomSelection(activityBaseBinding.settingLayout);
        }, 120);
    }

    public void onViewModeClick(View view) {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void onBottomBarClick(View view) {
        if (homeFragment.isVisible() && view == activityBaseBinding.homeLayout)
            return;
        else if (categoryFragment != null && categoryFragment.isVisible() && view == activityBaseBinding.categoryLayout)
            return;
        else if (bookMarkFragment != null && bookMarkFragment.isVisible() && view == activityBaseBinding.bookMarkLayout)
            return;
        else if (settingFragment != null && settingFragment.isAdded() && view == activityBaseBinding.settingLayout)
            return;

        if (view == activityBaseBinding.homeLayout)
            fragmentLoader(homeFragment);
        else if (view == activityBaseBinding.categoryLayout) {
            if (categoryFragment == null)
                categoryFragment = CategoryFragment.newInstance();
            fragmentLoader(categoryFragment);
        } else if (view == activityBaseBinding.bookMarkLayout) {
            if (bookMarkFragment == null)
                bookMarkFragment = BookMarkFragment.newInstance();
            fragmentLoader(bookMarkFragment);
        } else {
            if (settingFragment == null)
                settingFragment = SettingFragment.newInstance();
            fragmentLoader(settingFragment);
        }

        decideBottomSelection(view);
    }

    private void decideBottomSelection(View view) {
        if (view == activityBaseBinding.homeLayout)
            App.newsType = 6;

        LinearLayout linearLayout;
        ImageView imageView;
        for (int i = 0; i < activityBaseBinding.linearLayout.getChildCount(); i++) {
            linearLayout = (LinearLayout) activityBaseBinding.linearLayout.getChildAt(i);
            imageView = (ImageView) linearLayout.getChildAt(0);
            imageView.setAlpha(0.6f);
            imageView.setColorFilter(getResources().getColor(R.color.iconTint));
        }

        linearLayout = (LinearLayout) view;
        imageView = (ImageView) linearLayout.getChildAt(0);
        imageView.setAlpha(1f);
        imageView.setColorFilter(getResources().getColor(R.color.selectedTint));
    }

    public void onSearchIconClick(View view)
    {
        Log.e("searchIcon","isClicked");
        String string = view.getTag()+"";
        Intent intent = new Intent(this,SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("tag",string);
        startActivity(intent);
    }
}
