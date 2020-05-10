package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.media.Image;
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
            activityNewsDetailBinding.viewPager2.setAdapter(new ViewPagerAdapter2(jsonArray));

        activityNewsDetailBinding.viewPager2.setCurrentItem(i);

    }

    private class ViewPagerAdapter2 extends RecyclerView.Adapter<ViewPagerAdapter2.InnerClass>
    {
        private JsonArray jsonArray = new JsonArray();

        private ViewPagerAdapter2(JsonArray jsonArray)
        {
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public ViewPagerAdapter2.InnerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = getLayoutInflater().inflate(R.layout.view_pager_item,parent,false);

            return new InnerClass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerAdapter2.InnerClass holder, int position)
        {
            JsonObject jsonObject = (JsonObject) jsonArray.get(position);

            String string = jsonObject.get("urlToImage") + "";
            string = string.replace("\"", "");
            Log.e("imageIs", string);
            if (!string.isEmpty())
                Picasso.get().load(string).into(holder.imageView);

            string = jsonObject.get("title") + "";
            holder.textView.setText(string);

            string = jsonObject.get("content") + "";
            string = string.replace("\"","");
            string = string.replace("\r","");
            string = string.replace("\n"," ");
            if (string.contains("["))
                string = string.substring(0, string.lastIndexOf("["));
            else
                string = jsonObject.get("description")+"";
            holder.tV.setText(string);

            jsonObject = (JsonObject) jsonObject.get("source");
            string = jsonObject.get("name") + "";
            if (!string.equalsIgnoreCase("null"))
                holder.t.setText("swipe left for more at " + string);

        }

        @Override
        public int getItemCount() {
            return jsonArray.size();
        }

        private class InnerClass extends RecyclerView.ViewHolder
        {
            private TextView textView,tV,t;
            private ImageView imageView;

            private InnerClass(@NonNull View itemView)
            {
                super(itemView);

                textView = itemView.findViewById(R.id.titleTextView);
                tV = itemView.findViewById(R.id.bodyTextView);
                t = itemView.findViewById(R.id.extraTextView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}
