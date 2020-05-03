package rohit.maurya.countrywisenews.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rohit.maurya.countrywisenews.ApiInterface;
import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.News;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.activity.BaseActivity;
import rohit.maurya.countrywisenews.adapters.RecyclerViewAdapter;
import rohit.maurya.countrywisenews.databinding.FragmentCategoryBinding;

public class CategoryFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    private View view;
    private Context context;
    private FragmentCategoryBinding fragmentCategoryBinding;
    private String categoryName = "";
    private JsonArray businessArray = new JsonArray();
    private JsonArray entertainmentArray = new JsonArray();
    private JsonArray healthArray = new JsonArray();
    private JsonArray scienceArray = new JsonArray();
    private JsonArray sportsArray = new JsonArray();
    private JsonArray technologyArray = new JsonArray();

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (fragmentCategoryBinding == null) {
            fragmentCategoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false);
            context = getContext();

            setTabLayoutData();
        }
        return fragmentCategoryBinding.getRoot();
    }

    private void setTabLayoutData() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Business");
        arrayList.add("Entertainment");
        arrayList.add("Health");
        arrayList.add("Science");
        arrayList.add("Sports");
        arrayList.add("Technology");

        for (String string : arrayList) {
            TabLayout.Tab tab = fragmentCategoryBinding.tabLayout.newTab();
            tab.setTag(string);
            tab.setText(string);
            fragmentCategoryBinding.tabLayout.addTab(tab);
            fragmentCategoryBinding.tabLayout.addOnTabSelectedListener(this);
        }

        onTabSelected(Objects.requireNonNull(fragmentCategoryBinding.tabLayout.getTabAt(0)));

        fragmentCategoryBinding.viewPager.setAdapter(new ViewPagerAdapter());
        fragmentCategoryBinding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(fragmentCategoryBinding.tabLayout));
    }

    private void decideCategory() {
        ApiInterface apiInterface = App.createService(ApiInterface.class);
        Call<News> call;

        if (categoryName.equalsIgnoreCase("business"))
        {
            if (businessArray.size() == 0) {
                call = apiInterface.getBusinessNews();
                loadData(call, jsonArray -> {
                    businessArray = jsonArray;
                    Log.e("businessArrayIs", businessArray + "");

                });
            }
            else
            {

            }
        } else if (categoryName.equalsIgnoreCase("entertainment"))
        {
            if (entertainmentArray.size() == 0) {
                call = apiInterface.getEntertainmentNews();
                loadData(call, jsonArray -> {
                    entertainmentArray = jsonArray;
                    Log.e("entertainmentArrayIs", entertainmentArray + "");
                });
            }
            else
            {

            }
        } else if (categoryName.equalsIgnoreCase("health")) {
            if (healthArray.size() == 0) {
                call = apiInterface.getHealthNews();
                loadData(call, jsonArray -> {
                    healthArray = jsonArray;
                    Log.e("healthArrayIs", healthArray + "");
                });
            }
            else
            {

            }
        } else if (categoryName.equalsIgnoreCase("science")) {
            if (scienceArray.size() == 0) {
                call = apiInterface.getScienceNews();
                loadData(call, jsonArray -> {
                    scienceArray = jsonArray;
                    Log.e("scienceArrayIs", scienceArray + "");
                });
            }
            else
            {

            }
        } else if (categoryName.equalsIgnoreCase("sports")) {
            if (sportsArray.size() == 0) {
                call = apiInterface.getSportsNews();
                loadData(call, jsonArray -> {
                    sportsArray = jsonArray;
                    Log.e("sportsArrayIs", sportsArray + "");
                });
            }
            else
            {

            }
        } else {
            if (technologyArray.size() == 0) {
                call = apiInterface.getTechnologyNews();
                loadData(call, jsonArray -> {
                    technologyArray = jsonArray;
                    Log.e("technologyArrayArrayIs", technologyArray + "");
                });
            }
            else
            {

            }
        }
    }

    private void loadData(Call<News> call, CallBack callBack) {
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    //ArrayList<JsonObject> arrayList = response.body().getList();
                    JsonArray jsonArray = response.body().getJsonArray();
                    callBack.response(jsonArray);
                    //Log.e("tempJsonArray", jsonArray + "");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("errorIs", t.getMessage());
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Object object = tab.getTag();
        if (object != null) {
            categoryName = object.toString();
            decideCategory();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private interface CallBack {
        void response(JsonArray jsonArray);
    }

    private class ViewPagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position)
        {
            //return super.instantiateItem(container, position);

            View view = ((BaseActivity)context).getLayoutInflater().inflate(R.layout.recycler_view,container,false);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);
            RecyclerViewAdapter recyclerViewAdapter;
            if (position == 0)
                recyclerViewAdapter = new RecyclerViewAdapter(context,businessArray);
            else if (position == 1)
                recyclerViewAdapter = new RecyclerViewAdapter(context,entertainmentArray);
            else if (position == 2)
                recyclerViewAdapter = new RecyclerViewAdapter(context,healthArray);
            else if (position == 3)
                recyclerViewAdapter = new RecyclerViewAdapter(context,scienceArray);
            else if (position == 4)
                recyclerViewAdapter = new RecyclerViewAdapter(context,sportsArray);
            else
                recyclerViewAdapter = new RecyclerViewAdapter(context,technologyArray);

            recyclerView.setAdapter(recyclerViewAdapter);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);

            container.removeView((View)object);
        }
    }
}
