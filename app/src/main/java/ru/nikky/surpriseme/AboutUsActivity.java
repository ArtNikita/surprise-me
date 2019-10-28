package ru.nikky.surpriseme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);



        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .scrimStartAlpha(0f)
                .build();
        Slidr.attach(this, config);
    }
}
