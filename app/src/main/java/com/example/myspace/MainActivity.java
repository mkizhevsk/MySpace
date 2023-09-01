package com.example.myspace;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.myspace.data.WeatherProvider;
import com.example.myspace.data.service.BaseService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BaseService baseService;

    public static Handler weatherHandler;

    TextView weatherTextView;

    private static final String TAG = "myLogs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherTextView = findViewById(R.id.weather_text);

        if(checkPermissions()) startApp();
    }

    protected void startApp() {
        Log.d(TAG, "start app");

        Intent intent = new Intent(this, BaseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        weatherHandler = getWeatherHandler();

        WeatherProvider.getInstance().getTemperature();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BaseService.LocalBinder binder = (BaseService.LocalBinder) service;
            baseService = binder.getService();

            Log.d(TAG, "MainActivity onServiceConnected");

//            notes = baseService.getNotes();
//            Log.d(TAG, "" + notes.size());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            Log.d(TAG, "MainActivity onServiceDisconnected");
        }
    };

    private Handler getWeatherHandler() {
        return new Handler(message -> {
            Bundle bundle = message.getData();
            double temperature = bundle.getDouble("temperature");

            weatherTextView.setText(new DecimalFormat("##.#").format(temperature));

            Log.d(TAG, "weatherHandler " + temperature);
            return true;
        });
    }

    protected void onResume(Bundle savedInstanceState) {
        Log.d(TAG, "onResume");
    }

    // top right menu
    public  boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "export");
        menu.add(0, 2, 0, "import my_space.db");
//        menu.add(0, 3, 0, "погода");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                baseService.exportDatabase();
                Toast.makeText(getApplicationContext(), "Данные экспортированы во внутреннюю память", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Импорт данных");
                alert.setMessage("Импортировать данные?");
                alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        baseService.importDatabase();
                        Toast.makeText(getApplicationContext(), "Данные импортированы успешно", Toast.LENGTH_SHORT).show();
                    }
                });
                alert.show();
                break;
            /*case 3:
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openweathermap.org/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                RetrofitService api = retrofit.create(RetrofitService.class);

                api.loadPojoCityWeather(openWeatherAppId, openWeatherUnits, "izhevsk").enqueue(new Callback<Weather>() {
                    @Override
                    public void onResponse(Call<Weather> call, Response<Weather> response) {
                        Weather weather = response.body();
                        Log.d(TAG, " " + weather.getVisibility() + " " + weather.getMain().getTemp());
                    }

                    @Override
                    public void onFailure(Call<Weather> call, Throwable t) {

                    }
                });

                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void goToDiary(View view) {
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    public void goToPhones(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);
    }

    public void goToCards(View view) {
        Intent intent = new Intent(this, CardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();

        Log.d(TAG, "onResume");
    }

    public boolean checkPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
        };

        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        Log.d(TAG, " разрешений: " + grantResults.length);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 1) startApp();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BaseService.class));

        super.onDestroy();
    }

}
