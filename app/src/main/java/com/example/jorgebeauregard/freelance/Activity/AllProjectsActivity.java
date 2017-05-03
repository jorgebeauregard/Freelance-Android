package com.example.jorgebeauregard.freelance.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

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

public class AllProjectsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ListView listProjects;
    private List<Project> ListProjects;
    private AdapterHome a1;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setTitle("Home");

        preferences = getSharedPreferences("user", MODE_PRIVATE);
        if(preferences.getString("user_id","").equals("")){
            Intent intent = new Intent(AllProjectsActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListProjects = new ArrayList<>();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllProjectsActivity.this,CreateProjectActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

        final String url = "http://10.50.92.115:8000/";
        String urlG = url + "api/getAllProjects";
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
                        JSONArray array_categories = ((JSONObject)jsonData.get(i)).getJSONArray("categories");
                        List<String> categories_list = new ArrayList<>();
                        JSONArray array_collaborators = ((JSONObject)jsonData.get(i)).getJSONArray("collaborators");
                        List<String> collaborators_list = new ArrayList<>();
                        JSONArray array_images = ((JSONObject)jsonData.get(i)).getJSONArray("photos");
                        List<String> images_list = new ArrayList<>();

                        for (int j = 0; j < array_categories.length(); j++) {
                            categories_list.add(((JSONObject) array_categories.get(j)).getString("name"));
                        }

                        for(int j = 0; j < array_collaborators.length(); j++){
                            collaborators_list.add(((JSONObject) array_collaborators.get(j)).getString("name"));
                        }

                        for(int j  = 0; j < array_images.length(); j++){
                            images_list.add(((JSONObject) array_images.get(j)).getString("path"));
                        }

                        Project project = new Project(((JSONObject)jsonData.get(i)).getInt("id"),((JSONObject)jsonData.get(i)).getString("name"), ((JSONObject)jsonData.get(i)).getString("owner"), ((JSONObject)jsonData.get(i)).getString("description"), ((JSONObject)jsonData.get(i)).getInt("difficulty"), ((JSONObject)jsonData.get(i)).getString("document"),images_list,categories_list,collaborators_list);
                        ListProjects.add(project);
                    }
                    listProjects = (ListView)findViewById(R.id.listProjects);

                    a1=new AdapterHome(AllProjectsActivity.this, ListProjects, AllProjectsActivity.this);

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the AllProjectsActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_projects) {
            Intent intent = new Intent(AllProjectsActivity.this, AllProjectsActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_submitted_projects) {
            Intent intent = new Intent(AllProjectsActivity.this, AllMyProjectsActivity.class);
            startActivity(intent);
        } else if (id == R.id.my_work_projects) {

        } else if (id == R.id.profile) {
            Intent intent = new Intent(AllProjectsActivity.this, ProfileActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            SharedPreferences settings = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.remove("user_id");
            editor.commit();

            Intent intent = new Intent(AllProjectsActivity.this,LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
