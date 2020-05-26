package rohit.maurya.countrywisenews.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
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
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import rohit.maurya.countrywisenews.App;
import rohit.maurya.countrywisenews.R;
import rohit.maurya.countrywisenews.RealmHelper;
import rohit.maurya.countrywisenews.databinding.ActivityNewsDetailBinding;

public class NewsDetailActivity extends AppCompatActivity {

    private Target target;
    private ActivityNewsDetailBinding activityNewsDetailBinding;
    private JsonArray jsonArray;
    private ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            JsonObject jsonObject = (JsonObject) jsonArray.get(position);
            String string = jsonObject.get("title") + "";
            changeStatusBarColor(string);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewsDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_news_detail);

        int i = getIntent().getIntExtra("int", 0);

        jsonArray = new JsonParser().parse(getIntent().getStringExtra("jsonArray")).getAsJsonArray();

        if (jsonArray != null) {
            activityNewsDetailBinding.viewPager2.setAdapter(new ViewPagerAdapter2());
            activityNewsDetailBinding.viewPager2.setCurrentItem(i);
            activityNewsDetailBinding.viewPager2.registerOnPageChangeCallback(onPageChangeCallback);
        }
    }

    private class ViewPagerAdapter2 extends RecyclerView.Adapter<ViewPagerAdapter2.InnerClass> {

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
            string = App.filterString(string);
            Log.e("imageIs", string);
            if (!string.isEmpty()) {
                target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.imageView.setImageBitmap(bitmap);
                        String string = jsonObject.get("title") + "";
                        storeImageDataInDb(string, bitmap); // it also changes color of status bar
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.e("failedTo", "loadBitmap");
                        String string = jsonObject.get("title") + "";

                        string = RealmHelper.getBase64String(string);
                        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        holder.imageView.setImageBitmap(bitmap);

                        Log.e("base64Is", string + "empty ");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Picasso.get().load(string).into(target);
            }
            //Picasso.get().load(string).into(holder.imageView);

            string = jsonObject.get("title") + "";
            string = App.filterString(string);
            holder.textView.setText(string);

            string = jsonObject.get("content") + "";
            string = App.filterString(string);
            string = App.filterString(string);

            if (string.contains("["))
                string = string.substring(0, string.lastIndexOf("["));
            else
                string = jsonObject.get("description") + "";
            string = App.filterString(string);
            holder.tV.setText(string);

            string = jsonObject.get("url") + "";
            string = App.filterString(string);
            holder.t.setTag(string);

            //jsonObject = (JsonObject) jsonObject.get("source");
            string = jsonObject.get("name") + "";
            string = App.filterString(string);
            if (!string.equalsIgnoreCase("null"))
                makeSpannableString(holder.t, "Click for more at " + string);

            holder.t.setOnClickListener(view -> {
                String str = view.getTag() + "";
                str = App.filterString(str);
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

    private void changeStatusBarColor(String string) {
        int[] ints = RealmHelper.getColors(string);
        getWindow().setStatusBarColor(ints[0]);
    }

    private void storeImageDataInDb(final String titleName, Bitmap bitmap) {
        if (RealmHelper.isImageByteExist(titleName)) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            String string = Base64.encodeToString(bytes, Base64.DEFAULT);

            Palette.from(bitmap).generate(palette -> {
                Log.e("onGenerated", "isExecuted");
                assert palette != null;
                //Log.e("colorIs",palette.getDarkMutedColor(getResources().getColor(R.color.colorBlack))+"");
                //getWindow().setStatusBarColor(palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark)));

                RealmHelper.storeImageData(titleName, string, palette.getDominantColor(getResources().getColor(R.color.colorPrimaryDark)),
                        palette.getDarkMutedColor(getResources().getColor(R.color.colorPrimaryDark)),
                        () -> onPageChangeCallback.onPageSelected(activityNewsDetailBinding.viewPager2.getCurrentItem()));
            });
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

        spannableString.setSpan(clickableSpan, string.indexOf("t") + 2, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
