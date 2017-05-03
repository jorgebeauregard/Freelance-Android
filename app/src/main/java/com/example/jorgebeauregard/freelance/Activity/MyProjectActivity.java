package com.example.jorgebeauregard.freelance.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
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
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.example.jorgebeauregard.freelance.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyProjectActivity extends AppCompatActivity {
    boolean flag=false;
    final String url = "http://10.50.92.115:8000/";
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        preferences = getSharedPreferences("user",MODE_PRIVATE);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        final SliderLayout mDemoSlider;

        //Back button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        //Change color of the status bar because Android Studio is stupid and the types of the activities don't match
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag)
                {
                    Intent intent = new Intent(MyProjectActivity.this,EditProjectActivity.class);
                    intent.putExtra("project_id",getIntent().getIntExtra("project_id",1));
                    startActivity(intent);
                }
                else
                {
                    joinProject();
                }
            }
        });

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

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


        String urlG = url + "api/getProject?project_id="+getIntent().getIntExtra("project_id",1);
        final Context c = this;
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                try {
                    JSONObject jsonData = new JSONObject(response);
                    JSONObject JSONdata = jsonData.getJSONObject("data");

                    setTitle(JSONdata.getString("name"));
                    ((TextView) findViewById(R.id.project_difficulty)).setText(JSONdata.getString("difficulty"));
                    ((TextView) findViewById(R.id.project_description)).setText(JSONdata.getString("description"));
                    ((TextView) findViewById(R.id.project_owner)).setText(JSONdata.getString("owner_email"));

                    JSONArray array_photos = JSONdata.getJSONArray("photos");

                    List<String> images_list = new ArrayList<>();
                    for (int i = 0; i < array_photos.length(); i++) {
                        images_list.add(url + ((JSONObject) array_photos.get(i)).getString("path"));
                    }

                    for (int i = 0; i < images_list.size(); i++) {
                        TextSliderView textSliderView = new TextSliderView(MyProjectActivity.this);
                        // initialize a SliderLayout
                        textSliderView.image(images_list.get(i));

                        mDemoSlider.addSlider(textSliderView);
                    }
                    mDemoSlider.setDuration(4000);

                    //Set Text for Categories
                    JSONArray array_categories = JSONdata.getJSONArray("categories");

                    List<String> categories_list = new ArrayList<>();
                    for (int i = 0; i < array_categories.length(); i++) {
                        categories_list.add(((JSONObject) array_categories.get(i)).getString("name"));
                    }

                    String categories = "";
                    for (int i = 0; i < categories_list.size(); i++) {
                        if (i != categories_list.size()-1){
                            categories += categories_list.get(i) + ", ";
                        }
                        else
                            categories += categories_list.get(i);
                    }

                    ((TextView) findViewById(R.id.project_categories)).setText(categories);

                    //End Set Text for Categories

                    //Set Text for Collaborators
                    JSONArray array_collaborators = JSONdata.getJSONArray("collaborators");

                    List<String> collaborators_list = new ArrayList<>();
                    for (int i = 0; i < array_collaborators.length(); i++) {
                        collaborators_list.add(((JSONObject) array_collaborators.get(i)).getString("name"));
                    }

                    String collaborators = "";
                    for (int i = 0; i < collaborators_list.size(); i++) {
                        if (i != collaborators_list.size()-1){
                            if (collaborators_list.get(i).equals(getIntent().getStringExtra("name"))){
                                flag = true;
                            }
                            collaborators += collaborators_list.get(i) + ", ";
                        }
                        else
                            collaborators += collaborators_list.get(i);
                    }

                    ((TextView) findViewById(R.id.project_collaborators)).setText(collaborators);

                    //End Set Text for Collaborators

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    public void joinProject()
    {
        RequestQueue mRequestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        BasicNetwork network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
        String join = url + "api/joinProject?project_id=" + getIntent().getIntExtra("project_id",1) + "&user_id=" + preferences.getString("user_id","");
        //Toast.makeText(getBaseContext(),join, Toast.LENGTH_SHORT).show();
        System.out.print(join);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, join, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                //Toast.makeText(getBaseContext(),"pruebaConexion", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonData = new JSONObject(response);
                    JSONObject JSONdata;
                    //para mostrar errores
                    String state = jsonData.getString("state");
                    //Toast.makeText(getBaseContext(),state, Toast.LENGTH_SHORT).show();
                    if (Integer.parseInt(state) == 200) {

                        Toast.makeText(getBaseContext(), "You have joined the project", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MyProjectActivity.this,AllProjectsActivity.class);
                        startActivity(intent);

                    }
                    //fin de muestra errores

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(stringRequest);
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

        Intent myIntent = new Intent(getApplicationContext(), AllProjectsActivity.class);
        startActivityForResult(myIntent, 0);

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")

    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.all_projects) {
            // Handle the camera action
        } else if (id == R.id.my_submitted_projects) {

        } else if (id == R.id.my_work_projects) {

        } else if (id == R.id.profile) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
