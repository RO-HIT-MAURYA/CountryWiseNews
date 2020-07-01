package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.adapters.RecyclerViewAdapter;
import rohit.maurya.countrywisenews.databinding.ActivitySearchBinding;
import rohit.maurya.countrywisenews.modals.NewsModal;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding activitySearchBinding;
    public JsonArray masterJsonArray = new JsonArray();
    public static JsonArray subJsonArray = new JsonArray();
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        setUpToolBar();
        getJsonArray();
    }

    private void getJsonArray() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NewsModal> realmResults = realm.where(NewsModal.class).findAll();
        realmResults = realmResults.sort("publishedAt", Sort.DESCENDING);
        masterJsonArray = new JsonParser().parse(realmResults.asJSON()).getAsJsonArray();
        subJsonArray = masterJsonArray;

        setAdapter(masterJsonArray);
    }

    private void setAdapter(JsonArray jsonArray) {

        if (jsonArray.size() == 0)
            activitySearchBinding.textView.setVisibility(View.VISIBLE);
        else
            activitySearchBinding.textView.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activitySearchBinding.recyclerView.setLayoutManager(linearLayoutManager);
        activitySearchBinding.recyclerView.setAdapter(new RecyclerViewAdapter(this, jsonArray, true));
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        Menu menu = toolbar.getMenu();
        searchView = (SearchView) menu.getItem(0).getActionView();
        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setQueryHint("Search here");

        LinearLayout linearLayout = (LinearLayout) searchView.getChildAt(0);
        linearLayout = (LinearLayout) linearLayout.getChildAt(2);
        linearLayout = (LinearLayout) linearLayout.getChildAt(1);

        SearchView.SearchAutoComplete autoComplete = (androidx.appcompat.widget.SearchView.SearchAutoComplete) linearLayout.getChildAt(0);
        autoComplete.setHintTextColor(getResources().getColor(R.color.vColor));
        autoComplete.setTextColor(getResources().getColor(R.color.lineColor));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //filterData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });

        /*searchView.setOnKeyListener((v, keyCode, event) -> {
            Log.e("imeIs","clicked");
            filterData();
            return false;
        });*/
    }

    private void filterData(String string) {
        //String string = searchView.getQuery()+"";
        JsonArray jsonArray = new JsonArray();
        //JsonObject jsonObject;
        for (JsonElement jsonElement : masterJsonArray) {
            if ((jsonElement + "").contains(string))
                jsonArray.add(jsonElement);
        }

        subJsonArray = jsonArray;
        setAdapter(jsonArray);
    }

    public void onMicClick(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to search");
        startActivityForResult(intent, 31);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("intercepted", "false");
        if (resultCode == RESULT_OK) {
            Log.e("intercepted", "true");
            if (data != null) {
                ArrayList<String> arrayList = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (arrayList.size() > 0)
                    searchView.setQuery(arrayList.get(0), false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_OK);
        super.onBackPressed();
    }
}