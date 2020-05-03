package rohit.maurya.countrywisenews.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rohit.maurya.countrywisenews.ApiInterface;
import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.News;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.adapters.ListAdapter;
import rohit.maurya.countrywisenews.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private View view;
    private Context context;
    private FragmentHomeBinding fragmentHomeBinding;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (fragmentHomeBinding == null) {
            fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            context = getContext();

            getHomeNews();
        }

        return fragmentHomeBinding.getRoot();
    }

    private void getHomeNews() {
        ApiInterface apiInterface = App.createService(ApiInterface.class);

        Call<News> call = apiInterface.getNews();
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    //ArrayList<JsonObject> arrayList = response.body().getList();
                    JsonArray jsonArray = response.body().getJsonArray();
                    //Log.e("responseIs", list + "");

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    fragmentHomeBinding.recyclerView.setLayoutManager(linearLayoutManager);
                    ListAdapter listAdapter = new ListAdapter(context,jsonArray);
                    fragmentHomeBinding.recyclerView.setAdapter(listAdapter);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("errorIs", t.getMessage());
            }
        });
    }
}
