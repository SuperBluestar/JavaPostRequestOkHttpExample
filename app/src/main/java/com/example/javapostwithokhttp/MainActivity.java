package com.example.javapostwithokhttp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.javapostwithokhttp.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpPostRequest.sendHttpPostRequest("http://192.168.113.182:8000/phone_users/api", "phone='%2B815055393758'", new HttpPostRequest.HttpPostCallback() {
                    @Override
                    public void onSuccess(String response) {
                        // Do something with the response
                        System.out.println(response);
                        try {
                            final String responseObj = new JSONObject(response).getString("data");
                            final String allowed = new JSONObject(responseObj).getString("allowed");
                            if (allowed == "true") {
                                Log.v("Success", "Allow");
                            } else {
                                Log.v("Success", "Denied");
                            }
                        } catch (JSONException e) {
                            Log.v("JsonError", e.getMessage() != null ? e.getMessage() : e.toString());
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        // Handle the error
                        e.printStackTrace();
                    }
                });


//                PostExample example = new PostExample();
//                String json = "{'phone': '+815055393757'}";
//                example.post("http://192.168.113.182:8000/phone_users/api", json, new Callback() {
//                    @Override
//                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                        try {
//                            final String responseObj = new JSONObject(response.body().string()).getString("data");
//                            final String allowed = new JSONObject(responseObj).getString("allowed");
//                            Log.v("Success", allowed);
//                        } catch (JSONException e) {
//                            Log.v("JsonError", e.getMessage() == null ? "Unknown error" : e.getMessage());
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
//                        Log.v("Error", e.getMessage() == null ? "Unknown error" : e.getMessage());
//                    }
//                });
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}