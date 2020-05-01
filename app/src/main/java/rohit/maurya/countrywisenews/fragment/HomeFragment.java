package rohit.maurya.countrywisenews.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import rohit.maurya.countrywisenews.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment
{
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
        if (fragmentHomeBinding == null)
        {
            fragmentHomeBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
            context = getContext();

            getHomeNews();

        }

        return fragmentHomeBinding.getRoot();
        // Inflate the layout for this fragment
        /*if (view == null)
        {
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }
        return view;*/
    }

    private void setViewPagerData(List<JsonObject> list) {
        fragmentHomeBinding.viewPager.setAdapter(new ViewPagerAdapter(list));
    }

    private void getHomeNews()
    {
        ApiInterface apiInterface = App.createService(ApiInterface.class);

        Call<News> call = apiInterface.getNews();
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    ArrayList<JsonObject> arrayList = response.body().getList();
                    //Log.e("responseIs", list + "");

                    List<JsonObject> list = arrayList.subList(0,arrayList.size()/3);
                    setViewPagerData(list);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e("errorIs", t.getMessage());
            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {
        private List<JsonObject> list;
        private ViewPagerAdapter(List<JsonObject> list)
        {
            this.list = list;
            Log.e("listIs",list+"");
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            View view = getLayoutInflater().inflate(R.layout.image_view, container, false);
            ImageView imageView = view.findViewById(R.id.imageView);
            JsonObject jsonObject = new JsonObject();
            jsonObject = list.get(position);
            String string = jsonObject.get("urlToImage")+"";
            Log.e("imageUrlIs",string+"");
            Picasso.get().load(string).into(imageView);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
