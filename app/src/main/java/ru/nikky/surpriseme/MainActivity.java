package ru.nikky.surpriseme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    static TextView userName;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.userName);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        sharedPreferences = this.getSharedPreferences("ru.nikky.surpriseme", Context.MODE_PRIVATE);
        userName.setText(sharedPreferences.getString("userName", "Nikita"));
    }

    public void aboutUsFunc(View view) {
        Intent intent = new Intent(this, AboutUsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.nothing);
    }

    public void settingsFunc(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.nothing);
    }

    public void rateUsFunc(View view) {
        try{
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.handicap.surpriseme")));
        } catch (ActivityNotFoundException e){
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.handicap.surpriseme")));
        }
    }

    public void openChatClientFunc(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String subject = "Question";
        String body = "\n\nSent from the best guide application! :D";
        Uri data = Uri.parse("mailto:info@surprizeme.ru?subject=" + subject + "&body=" + body);
        intent.setData(data);
        startActivity(intent);
    }

    public void helpFunc(View view) {
        Toast.makeText(this, "Help button doesn't work yet :(", Toast.LENGTH_SHORT).show();
    }

    public void logOutFunc(View view) {
        Toast.makeText(this, "See you ;)", Toast.LENGTH_SHORT).show();
        if(android.os.Build.VERSION.SDK_INT >= 21)
        {
            finishAndRemoveTask();
        }
        else
        {
            finish();
        }
    }

    public void legalFunc(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://surprizeme.ru/agreement/"));
        startActivity(browserIntent);
    }

    public void ourPartnersFunc(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://surprizeme.ru/partners/"));
        startActivity(browserIntent);
    }
}

//Links
//https://android-arsenal.com/details/1/3341  Status Bar Settings
//https://github.com/r0adkll/Slidr Slider Activity