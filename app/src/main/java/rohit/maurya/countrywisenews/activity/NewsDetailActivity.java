package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.databinding.ActivityNewsDetailBinding;

public class NewsDetailActivity extends AppCompatActivity {
    ActivityNewsDetailBinding activityNewsDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        int i = getIntent().getIntExtra("int",0);

        JsonArray jsonArray = App.jsonArray;
        if (jsonArray != null)
            activityNewsDetailBinding.verticalViewPager.setAdapter(new ViewPagerAdapter(jsonArray));

        activityNewsDetailBinding.verticalViewPager.setCurrentItem(i);

    }

    private class ViewPagerAdapter extends PagerAdapter {
        private JsonArray jsonArray = new JsonArray();

        private ViewPagerAdapter(JsonArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @Override
        public int getCount() {
            return jsonArray.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //return super.instantiateItem(container, position);
            View view = getLayoutInflater().inflate(R.layout.view_pager_item, container, false);
            JsonObject jsonObject = (JsonObject) jsonArray.get(position);

            String string = jsonObject.get("urlToImage") + "";
            string = string.replace("\"", "");
            Log.e("imageIs", string);
            if (!string.isEmpty())
                Picasso.get().load(string).into((ImageView) view.findViewById(R.id.imageView));

            string = jsonObject.get("title") + "";
            ((TextView) view.findViewById(R.id.titleTextView)).setText(string);

            string = jsonObject.get("content") + "";
            string = string.replace("\"","");
            string = string.replace("\r","");
            string = string.replace("\n"," ");
            if (string.contains("["))
                string = string.substring(0, string.lastIndexOf("["));
            else
                string = jsonObject.get("description")+"";
            ((TextView) view.findViewById(R.id.bodyTextView)).setText(string);

            jsonObject = (JsonObject) jsonObject.get("source");
            string = jsonObject.get("name") + "";
            if (!string.equalsIgnoreCase("null"))
                ((TextView) view.findViewById(R.id.extraTextView)).setText("swipe left for more at " + string);

            activityNewsDetailBinding.verticalViewPager.addView(view);

            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //super.destroyItem(container, position, object);
            container.removeView((View) object);
        }
    }
}
