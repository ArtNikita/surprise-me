package ru.nikky.surpriseme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrConfig;
import com.r0adkll.slidr.model.SlidrPosition;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    Button tourLangEngButton;
    Button tourLangRusButton;
    SharedPreferences sharedPreferences;
    Switch settingsSwitch;
    EditText settingsEditText;
    String username;
    private String token = "b08ec419bc777ba24264ec5cd426b874c89e4c34";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        settingsEditText = findViewById(R.id.settingsUserName);
        username = "";

        SlidrConfig config = new SlidrConfig.Builder()
                .position(SlidrPosition.TOP)
                .scrimStartAlpha(0f)
                .build();
        Slidr.attach(this, config);

        sharedPreferences = this.getSharedPreferences("ru.nikky.surpriseme", Context.MODE_PRIVATE);
        settingsEditText.setText(sharedPreferences.getString("userName", "Nikita"));
        tourLangEngButton = findViewById(R.id.tourLangEngButton);
        tourLangRusButton = findViewById(R.id.tourLangRusButton);
        settingsSwitch = findViewById(R.id.settingsSwitch);
        settingsSwitch.setChecked(sharedPreferences.getBoolean("permission", false));

        if(sharedPreferences.getString("tourLang", "en").equals("en")) {
            tourLangFunc(tourLangEngButton);
        } else {
            tourLangFunc(tourLangRusButton);
        }

        settingsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SettingsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                    sharedPreferences.edit().putBoolean("permission", true).apply();
                } else {
                    sharedPreferences.edit().putBoolean("permission", false).apply();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
            settingsSwitch.setChecked(false);
        }
    }

    public void tourLangFunc(View view) {
        if (view.getId() == R.id.tourLangEngButton){
            tourLangEngButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.oval_button));
            tourLangRusButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.oval_button_white));
            sharedPreferences.edit().putString("tourLang", "en").apply();
        } else {
            tourLangEngButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.oval_button_white));
            tourLangRusButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.oval_button));
            sharedPreferences.edit().putString("tourLang", "ru").apply();
        }
    }

    public void changeName(View view) {
        username = settingsEditText.getText().toString();
        if (username != null && !username.equals("")){
            if (!username.contains(" ")){
                FirstName firstName = new FirstName(username);
                sendNetworkRequest(firstName);
            } else {
                Toast.makeText(this, "No spaces, only your name", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNetworkRequest(FirstName firstName) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://app.surprizeme.ru")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Token " + token);

        UserClient client = retrofit.create(UserClient.class);
        Call<JsonObject> call = client.changeName(headerMap, firstName);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    JsonObject resultObj = response.body();
                    if (resultObj.get("data").getAsJsonObject().get("first_name").getAsString().equals(username)){
                        Toast.makeText(SettingsActivity.this, "Name updated", Toast.LENGTH_SHORT).show();
                        MainActivity.userName.setText(username);
                        sharedPreferences.edit().putString("userName", username).apply();

                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(SettingsActivity.this, "Something went wrong :/", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(SettingsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
