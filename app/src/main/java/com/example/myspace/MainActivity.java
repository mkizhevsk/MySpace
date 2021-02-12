package com.example.myspace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myspace.data.BaseService;
import com.example.myspace.data.RetrofitService;
import com.example.myspace.data.Weather;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class MainActivity extends AppCompatActivity {

    BaseService baseService;

//    public static List<Note> notes;
//    public static List<Contact> contacts;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkPermissions()) startApp();
    }

    protected void startApp() {
        Log.d(TAG, "start app");

        Intent intent = new Intent(this, BaseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

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

    protected void onResume(Bundle savedInstanceState) {
        Log.d(TAG, "onResume");
    }

    // top right menu
    public  boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "export");
        menu.add(0, 2, 0, "import");
        menu.add(0, 3, 0, "погода");
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
            case 3:
                /*Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openweathermap.org/data/2.5/")
                        .build();

                RetrofitService service = retrofit.create(RetrofitService.class);

                //Call<Weather> repos = service.loadCityWeather("izhevsk");
//                Log.d(TAG, repos.toString());
                service.loadCityWeather("izhevsk").enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Log.d(TAG, response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });*/

                /*Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.ipify.org/")
                        .build();

                Api api = retrofit.create(Api.class);

                api.getIp().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Log.d(TAG, response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });*/

//                Retrofit retrofit = new Retrofit.Builder()
//                        .baseUrl("https://jsonplaceholder.typicode.com/")
//                        .build();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.openweathermap.org/")
                        .build();

                RetrofitService api = retrofit.create(RetrofitService.class);

                api.loadCityWeather().enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Log.d(TAG, response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });



                break;
        }
        return super.onOptionsItemSelected(item);
    }

//    interface Api{
//
//        @GET("/")
//        Call<ResponseBody> getIp();
//
//    }

    public void goToDiary(View view) {
        Intent intent = new Intent(this, DiaryActivity.class);
        startActivity(intent);
    }

    public void goToPhones(View view) {
        Intent intent = new Intent(this, ContactActivity.class);
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
        if(grantResults.length > 1) startApp();
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BaseService.class));

        super.onDestroy();
    }

}
