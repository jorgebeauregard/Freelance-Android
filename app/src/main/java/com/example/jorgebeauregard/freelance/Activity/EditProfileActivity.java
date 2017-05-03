package com.example.jorgebeauregard.freelance.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.example.jorgebeauregard.freelance.R;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {
    private String user_id;
    private EditText name, password, password_confirmation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user_id = getIntent().getStringExtra("user_id");

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        //Back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        //Change color of the status because Android Studio is stupid and the types of the activities don't match
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        setTitle("Edit Profile");

        name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        password_confirmation = (EditText)findViewById(R.id.password_confirmation);

        RequestQueue mRequestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        BasicNetwork network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();

        final String url = "http://10.50.92.115:8000/";

        //Get the project
        String urlH = url + "api/getUser?user_id="+user_id;
        StringRequest stringRequestProject = new StringRequest(Request.Method.GET, urlH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                try {
                    JSONObject jsonData = new JSONObject(response);
                    JSONObject JSONdata = jsonData.getJSONObject("data");

                    name.setText(JSONdata.getString("name"));

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        mRequestQueue.add(stringRequestProject);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(password.getText());
                System.out.println(password_confirmation.getText());

                if(password.getText().toString().equals(password_confirmation.getText().toString())){
                    RequestQueue mRequestQueue;
                    // Instantiate the cache
                    Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
                    // Set up the network to use HttpURLConnection as the HTTP client.
                    BasicNetwork network = new BasicNetwork(new HurlStack());
                    // Instantiate the RequestQueue with the cache and network.
                    mRequestQueue = new RequestQueue(cache, network);
                    // Start the queue
                    mRequestQueue.start();

                    final String url = "http://10.50.92.115:8000/";

                    //Get the project
                    String name_to_send = name.getText().toString().replace(" ", "%20");


                    String urlG = url + "api/updateUser?user_id="+user_id+"&name="+name_to_send+"&password="+password.getText().toString();
                    StringRequest stringRequestUpdate = new StringRequest(Request.Method.GET, urlG, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Do something with the response
                            try {
                                JSONObject jsonData = new JSONObject(response);
                                JSONObject JSONdata = jsonData.getJSONObject("data");

                                switch(jsonData.getString("state")){
                                    case "200":
                                        Toast toastSuccess = Toast.makeText(getApplicationContext(),"Profile edited successfully",Toast.LENGTH_LONG);
                                        toastSuccess.show();
                                        Intent intent = new Intent(EditProfileActivity.this, AllProjectsActivity.class);
                                        startActivity(intent);
                                        break;
                                    case "404":
                                        Toast toastFailure = Toast.makeText(getApplicationContext(),"Profile could not be updated",Toast.LENGTH_LONG);
                                        toastFailure.show();
                                        break;
                                }

                            } catch (JSONException e) {
                                Log.e("JSONException", "Error: " + e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                        }
                    });

                    mRequestQueue.add(stringRequestUpdate);
                }
                else{
                    Toast toastPasswordIncorrect = Toast.makeText(getApplicationContext(),"Passwords did not match",Toast.LENGTH_LONG);
                    toastPasswordIncorrect.show();
                }
            }
        });
    }

}
