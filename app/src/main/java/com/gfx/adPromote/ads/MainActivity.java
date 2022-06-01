package com.gfx.adPromote.ads;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gfx.adPromote.AppPromote;
import com.gfx.adPromote.Interfaces.OnPromoteAppWithYoutubeListener;


@SuppressLint("CustomSplashScreen")
public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.appsPromote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AppsDemoActivity.class));
            }
        });

        findViewById(R.id.youtubePromote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),YoutubeDemoActivity.class));
            }
        });


    }


}
