package rohit.maurya.countrywisenews.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.activity.BaseActivity;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.InnerClass> {
    private JsonArray jsonArray = new JsonArray();
    private Context context;

    public ListAdapter(Context context, JsonArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public InnerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.list_item, parent, false);
        return new ListAdapter.InnerClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerClass holder, int position) {
        JsonObject jsonObject = (JsonObject) jsonArray.get(position);
        holder.dayTextView.setText(jsonObject.get("publishedAt") + "");
        holder.titleTextView.setText(jsonObject.get("title") + "");

        String string = jsonObject.get("author") + "";
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

        public InnerClass(@NonNull View itemView) {
            super(itemView);

            dayTextView = itemView.findViewById(R.id.dayTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
        }
    }

    class GetDifference {
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


        public String getTimeAgo(long time) {
            if (time < 1000000000000L) {
                // if timestamp given in seconds, convert to millis
                time *= 1000;
            }

            //long now = getCurrentTime(ctx);
            long now = System.currentTimeMillis();
            if (time > now || time <= 0) {
                return null;
            }

            // TODO: localize
            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " minutes ago";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " hours ago";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " days ago";
            }
        }
    }
}
