package rohit.maurya.countrywisenews.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.activity.NewsDetailActivity;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.activity.BaseActivity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.InnerClass> {
    private JsonArray jsonArray = new JsonArray();
    private Context context;

    public RecyclerViewAdapter(Context context, JsonArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @NonNull
    @Override
    public InnerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = ((BaseActivity) context).getLayoutInflater().inflate(R.layout.list_item, parent, false);
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

        holder.dayTextView.setText(App.getDifference(jsonObject.get("publishedAt")+""));
        //holder.dayTextView.setText(jsonObject.get("publishedAt")+"");
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
        private View view;

        private InnerClass(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            dayTextView = itemView.findViewById(R.id.dayTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);

            itemView.setOnClickListener(view -> {
                    Intent intent = new Intent(context,NewsDetailActivity.class);
                    intent.putExtra("int",(int)view.getTag());
                    context.startActivity(intent);
            });
        }
    }
}
