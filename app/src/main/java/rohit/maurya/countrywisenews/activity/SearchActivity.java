package rohit.maurya.countrywisenews.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.adapters.RecyclerViewAdapter;
import rohit.maurya.countrywisenews.databinding.ActivitySearchBinding;
import rohit.maurya.countrywisenews.modals.NewsModal;

public class SearchActivity extends AppCompatActivity {

    private ActivitySearchBinding activitySearchBinding;
    private JsonArray masterJsonArray = new JsonArray();
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

        setAdapter(masterJsonArray);
    }

    private void setAdapter(JsonArray jsonArray) {

        if (jsonArray.size() == 0)
            activitySearchBinding.recyclerView.setVisibility(View.GONE);
        else
            activitySearchBinding.recyclerView.setVisibility(View.VISIBLE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        activitySearchBinding.recyclerView.setLayoutManager(linearLayoutManager);
        activitySearchBinding.recyclerView.setAdapter(new RecyclerViewAdapter(this, jsonArray));
    }

    private void setUpToolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        Menu menu = toolbar.getMenu();
        searchView = (SearchView) menu.getItem(0).getActionView();
        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setFocusable(true);
        searchView.setIconified(false);

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

        setAdapter(jsonArray);
    }
}