package rohit.maurya.countrywisenews.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonArray;
import com.victor.loading.rotate.RotateLoading;

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
    private JsonArray businessArray;//= new JsonArray();
    private JsonArray entertainmentArray;// = new JsonArray();
    private JsonArray healthArray;// = new JsonArray();
    private JsonArray scienceArray;// = new JsonArray();
    private JsonArray sportsArray;// = new JsonArray();
    private JsonArray technologyArray;// = new JsonArray();
    private RecyclerViewAdapter businessAdapter, entertainmentAdapter, healthAdapter, scienceAdapter, sportsAdapter, technologyAdapter;
    private Dialog dialog;

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

            View view = getLayoutInflater().inflate(R.layout.loader_layout,null,false);
            RotateLoading rotateLoading = view.findViewById(R.id.rotateLoading);
            rotateLoading.start();

            dialog = new Dialog(context);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.setContentView(view);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.show();

            initializeArray();

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

        for (int i=0; i<arrayList.size(); i++) {
            TabLayout.Tab tab = fragmentCategoryBinding.tabLayout.newTab();
            tab.setText(arrayList.get(i));
            tab.setTag(i);
            fragmentCategoryBinding.tabLayout.addTab(tab);
            fragmentCategoryBinding.tabLayout.addOnTabSelectedListener(this);
        }

        //fragmentCategoryBinding.tabLayout.setupWithViewPager(fragmentCategoryBinding.viewPager);
        fragmentCategoryBinding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(fragmentCategoryBinding.tabLayout));
    }

    private void initializeArray() {
        ApiInterface apiInterface = App.createService(ApiInterface.class);
        Call<News> call;

        if (businessArray == null) {
            call = apiInterface.getBusinessNews();
            loadData(call, jsonArray -> {
                businessArray = jsonArray;
                businessAdapter = new RecyclerViewAdapter(context,businessArray);
            });
        } else if (entertainmentArray == null) {
            call = apiInterface.getEntertainmentNews();
            loadData(call, jsonArray -> {
                entertainmentArray = jsonArray;
                entertainmentAdapter = new RecyclerViewAdapter(context,entertainmentArray);
            });
        } else if (healthArray == null) {
            call = apiInterface.getHealthNews();
            loadData(call, jsonArray -> {
                healthArray = jsonArray;
                healthAdapter = new RecyclerViewAdapter(context,healthArray);
            });
        } else if (scienceArray == null) {
            call = apiInterface.getScienceNews();
            loadData(call, jsonArray -> {
                scienceArray = jsonArray;
                scienceAdapter = new RecyclerViewAdapter(context,scienceArray);
            });
        } else if (sportsArray == null) {
            call = apiInterface.getSportsNews();
            loadData(call, jsonArray -> {
                sportsArray = jsonArray;
                sportsAdapter = new RecyclerViewAdapter(context,sportsArray);
            });
        } else if (technologyArray == null) {
            call = apiInterface.getTechnologyNews();
            loadData(call, jsonArray -> {
                technologyArray = jsonArray;
                technologyAdapter = new RecyclerViewAdapter(context,technologyArray);
                ((RotateLoading)dialog.findViewById(R.id.rotateLoading)).stop();
                dialog.hide();
                setTabLayoutData();
                fragmentCategoryBinding.viewPager.setAdapter(new ViewPagerAdapter());
            });
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

                    if (jsonArray == null)
                        callBack.response(new JsonArray());
                    else
                        callBack.response(jsonArray);

                    initializeArray();

                    Log.e("responseIs", jsonArray + "");
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("errorIs", t.getMessage());
                callBack.response(new JsonArray());
                initializeArray();
            }
        });
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tab.getTag() instanceof Integer)
            fragmentCategoryBinding.viewPager.setCurrentItem((int)tab.getTag());
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

    private class ViewPagerAdapter extends PagerAdapter {
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
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //return super.instantiateItem(container, position);

            View view = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.recycler_view, container, false);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);

            if (position == 0)
                recyclerView.setAdapter(businessAdapter);
            else if (position == 1)
                recyclerView.setAdapter(entertainmentAdapter);
            else if (position == 2)
                recyclerView.setAdapter(healthAdapter);
            else if (position == 3)
                recyclerView.setAdapter(scienceAdapter);
            else if (position == 4)
                recyclerView.setAdapter(sportsAdapter);
            else
                recyclerView.setAdapter(technologyAdapter);

            container.addView(view);

            Log.e("pagerIs", "invoked");

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);

            container.removeView((View) object);
        }
    }
}
