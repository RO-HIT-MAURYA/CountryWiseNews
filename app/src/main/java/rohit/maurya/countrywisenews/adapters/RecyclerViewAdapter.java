package rohit.maurya.countrywisenews.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.RealmHelper;
import rohit.maurya.countrywisenews.activity.NewsDetailActivity;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.activity.BaseActivity;
import rohit.maurya.countrywisenews.activity.SearchActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.InnerClass> {
    private JsonArray jsonArray;
    private Context context;
    private static boolean fromSearch;

    public RecyclerViewAdapter(Context context, JsonArray jsonArray, boolean fromSearch) {
        this.context = context;
        this.jsonArray = jsonArray;
        RecyclerViewAdapter.fromSearch = fromSearch;
    }

    @NonNull
    @Override
    public InnerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (context instanceof BaseActivity)
            view = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        else
            view = ((SearchActivity) context).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        return new RecyclerViewAdapter.InnerClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerClass holder, int position) {

        if (jsonArray == null)
            return;

        JsonObject jsonObject = (JsonObject) jsonArray.get(position);

        if (jsonObject == null)
            return;

        holder.view.setTag(position);

        String string = jsonObject.get("publishedAt") + "";
        //string = App.filterString(string);
        holder.dayTextView.setText(App.getDifference(string));
        //holder.dayTextView.setText(jsonObject.get("publishedAt")+"");

        string = jsonObject.get("title") + "";
        string = App.filterString(string);
        String str = string;
        holder.titleTextView.setText(string);

        boolean bool = jsonObject.getAsJsonPrimitive("bookMark").getAsBoolean();
        if (bool)
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_black_24dp));
        else
            holder.imageView.setImageDrawable(context.getDrawable(R.drawable.ic_bookmark_border_black_24dp));

        holder.imageView.setOnClickListener(view -> {
            if (bool) {
                jsonObject.addProperty("bookMark", false);
                RealmHelper.updateBookmark(str, false);
            } else {
                jsonObject.addProperty("bookMark", true);
                RealmHelper.updateBookmark(str, true);
            }
            jsonArray.set(position, jsonObject);
            notifyDataSetChanged();
        });

        string = jsonObject.get("author") + "";
        string = App.filterString(string);
        if (string.equalsIgnoreCase("null"))
            holder.authorTextView.setText("\" Unknown \"");
        else
            holder.authorTextView.setText(string);
    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }

    public class InnerClass extends RecyclerView.ViewHolder {
        private TextView dayTextView, titleTextView, authorTextView;
        private View view;
        private ImageView imageView;

        private InnerClass(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            dayTextView = itemView.findViewById(R.id.dayTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(view -> {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("int", (int) view.getTag());
                //Log.e("activityIs", (int) view.getTag() + "");
                if (!fromSearch)
                    intent.putExtra("jsonArray", jsonArray + "");
                Log.e("started", jsonArray + "");
                context.startActivity(intent);
            });
        }
    }
}
