package rohit.maurya.countrywisenews.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import rohit.maurya.countrywisenews.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_splash_screen);

       /* Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {*//* ... *//*}
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {*//* ... *//*}
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();*/

        String country = getApplicationContext().getResources().getConfiguration().locale.getDisplayCountry();
        Log.e("countryIs",country);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, BaseActivity.class));
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        },1230);
    }
}
