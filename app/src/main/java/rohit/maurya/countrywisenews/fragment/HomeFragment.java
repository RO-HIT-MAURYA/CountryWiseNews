package rohit.maurya.countrywisenews.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rohit.maurya.countrywisenews.ApiInterface;
import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.ResponseFormat;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.adapters.RecyclerViewAdapter;
import rohit.maurya.countrywisenews.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private Context context;
    private FragmentHomeBinding fragmentHomeBinding;
    public static JsonArray jsonArray = new JsonArray();

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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

        Call<ResponseFormat> call = apiInterface.getTopNews();
        call.enqueue(new Callback<ResponseFormat>() {
            @Override
            public void onResponse(Call<ResponseFormat> call, Response<ResponseFormat> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    //ArrayList<JsonObject> arrayList = response.body().getList();
                    jsonArray = response.body().getJsonArray();
                    App.jsonArray = jsonArray;
                    //Log.e("responseIs", list + "");

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    fragmentHomeBinding.recyclerView.setLayoutManager(linearLayoutManager);
                    RecyclerViewAdapter recyclerViewAdapter = new RecyclerViewAdapter(context,jsonArray);
                    fragmentHomeBinding.recyclerView.setAdapter(recyclerViewAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseFormat> call, Throwable t) {
                Log.e("errorIs", t.getMessage());
            }
        });
    }
}
