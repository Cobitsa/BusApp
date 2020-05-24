package com.cobitsa.busdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        출처: https://proletariat.tistory.com/86 [프롤레타리아, IT에 범접하다.]
        setContentView(R.layout.activity_main);
    }
}
