package rohit.maurya.countrywisenews.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.adapters.RecyclerViewAdapter;
import rohit.maurya.countrywisenews.databinding.FragmentBookmarkBinding;
import rohit.maurya.countrywisenews.modals.NewsModal;

public class BookMarkFragment extends Fragment {
    private FragmentBookmarkBinding fragmentBookmarkBinding;
    private Context context;

    public static BookMarkFragment newInstance() {
        BookMarkFragment fragment = new BookMarkFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentBookmarkBinding == null) {
            fragmentBookmarkBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false);
            context = getContext();
            //setAdapter();
        }
        // Inflate the layout for this fragment
        setAdapter();
        return fragmentBookmarkBinding.getRoot();
    }

    public void setAdapter() {

        Realm realm = Realm.getDefaultInstance();
        RealmResults<NewsModal> realmResults = realm.where(NewsModal.class).equalTo("bookMark", true).findAll();
        realmResults = realmResults.sort("publishedAt", Sort.DESCENDING);
        JsonArray jsonArray = new JsonParser().parse(realmResults.asJSON()).getAsJsonArray();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        fragmentBookmarkBinding.recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context, jsonArray, false);
        fragmentBookmarkBinding.recyclerView.setAdapter(recyclerViewAdapter);
    }
}
