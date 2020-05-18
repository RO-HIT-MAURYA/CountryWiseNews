package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Objects;

import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.databinding.ActivityNewsDetailBinding;

public class NewsDetailActivity extends AppCompatActivity {

    private Target target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityNewsDetailBinding activityNewsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        int i = getIntent().getIntExtra("int", 0);

        JsonArray jsonArray = App.jsonArray;
        if (jsonArray != null)
            activityNewsDetailBinding.viewPager2.setAdapter(new ViewPagerAdapter2(jsonArray));

        activityNewsDetailBinding.viewPager2.setCurrentItem(i);

    }

    private class ViewPagerAdapter2 extends RecyclerView.Adapter<ViewPagerAdapter2.InnerClass> {
        private JsonArray jsonArray = new JsonArray();

        private ViewPagerAdapter2(JsonArray jsonArray) {
            this.jsonArray = jsonArray;
        }

        @NonNull
        @Override
        public ViewPagerAdapter2.InnerClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.view_pager_item, parent, false);

            return new InnerClass(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewPagerAdapter2.InnerClass holder, int position) {
            JsonObject jsonObject = (JsonObject) jsonArray.get(position);

            String string = jsonObject.get("urlToImage") + "";
            string = string.replace("\"", "");
            Log.e("imageIs", string);
            if (!string.isEmpty()) {
                target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.imageView.setImageBitmap(bitmap);
                        Palette.from(bitmap).generate(palette -> {
                            Log.e("onGenerated", "isExecuted");
                            assert palette != null;
                            //Log.e("colorIs",palette.getDarkMutedColor(getResources().getColor(R.color.colorBlack))+"");
                            //getWindow().setStatusBarColor(palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark)));
                            getWindow().setStatusBarColor(palette.getDominantColor(getResources().getColor(R.color.colorPrimaryDark)));
                        });
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.get().load(string).into(target);
            }
            //Picasso.get().load(string).into(holder.imageView);

            string = jsonObject.get("title") + "";
            holder.textView.setText(string);

            string = jsonObject.get("content") + "";
            string = string.replace("\"", "");
            string = string.replace("\r", "");
            string = string.replace("\n", " ");
            if (string.contains("["))
                string = string.substring(0, string.lastIndexOf("["));
            else
                string = jsonObject.get("description") + "";
            holder.tV.setText(string);

            string = jsonObject.get("url") + "";
            holder.t.setTag(string);

            jsonObject = (JsonObject) jsonObject.get("source");
            string = jsonObject.get("name") + "";
            if (!string.equalsIgnoreCase("null"))
                makeSpannableString(holder.t, "Click for more at " + string);

            holder.t.setOnClickListener(view -> {
                String str = view.getTag() + "";
                str = str.replace("\"", "");
                Log.e("urlIs", str + "");
                startWebView(str);
            });
        }

        @Override
        public int getItemCount() {
            return jsonArray.size();
        }

        private class InnerClass extends RecyclerView.ViewHolder {
            private TextView textView, tV, t;
            private ImageView imageView;

            private InnerClass(@NonNull View itemView) {
                super(itemView);

                textView = itemView.findViewById(R.id.titleTextView);
                tV = itemView.findViewById(R.id.bodyTextView);
                t = itemView.findViewById(R.id.extraTextView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }

    private void makeSpannableString(TextView textView, String string) {
        SpannableString spannableString = new SpannableString(string);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                /*Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);*/
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorBlack));
            }
        };

        spannableString.setSpan(clickableSpan, string.indexOf("t") + 2, string.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void startWebView(String string) {

        View view = LayoutInflater.from(this).inflate(R.layout.web_view_container, null, false);
        WebView webView = view.findViewById(R.id.webView);// new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.clearHistory();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView v, String url, Bitmap favicon) {
                Log.e("onPageStarted", url);
                view.findViewById(R.id.linearLayout).setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView v, String url) {
                Log.e("onPageFinished", url);
            }
        });

        webView.loadUrl(string);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(view);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.show();
    }
}
