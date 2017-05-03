package com.example.jorgebeauregard.freelance.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.example.jorgebeauregard.freelance.Adapters.AdapterHome;
import com.example.jorgebeauregard.freelance.Classes.Project;
import com.example.jorgebeauregard.freelance.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AllMyProjectsActivity extends AppCompatActivity {
    private ListView listProjects;
    private List<Project> ListProjects;
    private AdapterHome a1;
    private SharedPreferences preferences;
    final String url = "http://10.50.92.115:8000/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_my_projects);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        setTitle("My Submitted Projects");

        preferences = getSharedPreferences("user", MODE_PRIVATE);
        if (preferences.getString("user_id", "").equals("")) {
            Intent intent = new Intent(AllMyProjectsActivity.this, LoginActivity.class);
            startActivity(intent);
        }

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


        ListProjects = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllMyProjectsActivity.this, CreateProjectActivity.class);
                startActivity(intent);
            }
        });

        //Request Data from the server

        RequestQueue mRequestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        BasicNetwork network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();


        String urlG = url + "api/getMyProjects?user_id=" + getSharedPreferences("user", MODE_PRIVATE).getString("user_id", "");
        final Context c = this;
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                try {

                    JSONObject JSONData = new JSONObject(response);
                    JSONArray jsonData = JSONData.getJSONArray("data");

                    for (int i = 0; i < jsonData.length(); i++) {
                        JSONArray array_categories = ((JSONObject) jsonData.get(i)).getJSONArray("categories");
                        List<String> categories_list = new ArrayList<>();
                        JSONArray array_collaborators = ((JSONObject) jsonData.get(i)).getJSONArray("collaborators");
                        List<String> collaborators_list = new ArrayList<>();
                        JSONArray array_images = ((JSONObject) jsonData.get(i)).getJSONArray("photos");
                        List<String> images_list = new ArrayList<>();

                        for (int j = 0; j < array_categories.length(); j++) {
                            categories_list.add(((JSONObject) array_categories.get(j)).getString("name"));
                        }

                        for (int j = 0; j < array_collaborators.length(); j++) {
                            collaborators_list.add(((JSONObject) array_collaborators.get(j)).getString("name"));
                        }

                        for (int j = 0; j < array_images.length(); j++) {
                            images_list.add(((JSONObject) array_images.get(j)).getString("path"));
                        }

                        Project project = new Project(((JSONObject) jsonData.get(i)).getInt("id"), ((JSONObject) jsonData.get(i)).getString("name"), ((JSONObject) jsonData.get(i)).getString("owner"), ((JSONObject) jsonData.get(i)).getString("description"), ((JSONObject) jsonData.get(i)).getInt("difficulty"), ((JSONObject) jsonData.get(i)).getString("document"), images_list, categories_list, collaborators_list);
                        ListProjects.add(project);
                    }
                    listProjects = (ListView) findViewById(R.id.listProjects);

                    a1 = new AdapterHome(AllMyProjectsActivity.this, ListProjects, AllMyProjectsActivity.this);

                    listProjects.setAdapter(a1);

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
        // Add the request to the RequestQueue.
        mRequestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
}
