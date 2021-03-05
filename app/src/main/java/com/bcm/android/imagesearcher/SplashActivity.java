package com.bcm.android.imagesearcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Splash Screen duration
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(com.bcm.android.imagesearcher.SplashActivity.this, MainActivity.class));
                finish();
            }
        }, secondsDelayed * 3000);
    }
}

