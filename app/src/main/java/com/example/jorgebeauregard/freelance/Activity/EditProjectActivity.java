package com.example.jorgebeauregard.freelance.Activity;

import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.example.jorgebeauregard.freelance.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EditProjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner_category, spinner_difficulty;
    private String option_category;
    private String option_difficulty;
    private TextView name, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        spinner_category = (Spinner)findViewById(R.id.category_spinner);
        spinner_difficulty = (Spinner)findViewById(R.id.difficulty_spinner);
        name = (TextView)findViewById(R.id.project_name);
        description = (TextView)findViewById(R.id.project_description);

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                RequestQueue mRequestQueue;
                // Instantiate the cache
                Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
                // Set up the network to use HttpURLConnection as the HTTP client.
                BasicNetwork network = new BasicNetwork(new HurlStack());
                // Instantiate the RequestQueue with the cache and network.
                mRequestQueue = new RequestQueue(cache, network);
                // Start the queue
                mRequestQueue.start();

                String name_to_send = name.getText().toString().replace(" ", "%20");
                String description_to_send = description.getText().toString().replace(" ", "%20");
                String category_to_send = option_category.replace(" ", "%20");
                final String url = "http://10.50.92.115:8000/";
                String urlG = url + "api/updateProject?name="+name_to_send+"&description="+description_to_send+"&project_id="+getIntent().getIntExtra("project_id",1)+"&user_id=1&difficulty="+option_difficulty+"&category="+category_to_send;

                // Formulate the request and handle the response.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlG, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditProjectActivity.this, "Project has been succesfully edited",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditProjectActivity.this, MyProjectActivity.class);
                        intent.putExtra("project_id",getIntent().getIntExtra("project_id",1));
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(EditProjectActivity.this, error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
                // Add the request to the RequestQueue.
                mRequestQueue.add(stringRequest);
            }
        });
        loadSpinners();
        loadProjectData();
    }

    public void  loadSpinners(){
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
        String urlG = url + "api/getAllCategories";

        //Get the project
        String urlH = url + "api/getProject_id="+getIntent().getIntExtra("project_id",1);

        final Context c = this;
        // Formulate the request and handle the response.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlG, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                List<String> category_list = new ArrayList<>();
                List<String> difficulty_list = new ArrayList<>();

                try {
                    JSONObject jsonData = new JSONObject(response);
                    JSONArray JSONdata = jsonData.getJSONArray("data");

                    setTitle("Edit your project");

                    for(int i=0;i<JSONdata.length();i++){
                        category_list.add(((JSONObject)JSONdata.get(i)).getString("name"));
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }

                //Adapter for categories spinner
                ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<>(EditProjectActivity.this, R.layout.item_spinner, category_list);
                dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_category.setAdapter(dataAdapterCategory);
                spinner_category.setOnItemSelectedListener(EditProjectActivity.this);

                difficulty_list.add("1");
                difficulty_list.add("2");
                difficulty_list.add("3");
                difficulty_list.add("4");
                difficulty_list.add("5");
                difficulty_list.add("6");
                difficulty_list.add("7");
                difficulty_list.add("8");
                difficulty_list.add("9");
                difficulty_list.add("10");

                //Adapter for difficulty spinner
                ArrayAdapter<String> dataAdapterDifficulty = new ArrayAdapter<>(EditProjectActivity.this, R.layout.item_spinner, difficulty_list);
                dataAdapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_difficulty.setAdapter(dataAdapterDifficulty);
                spinner_difficulty.setOnItemSelectedListener(EditProjectActivity.this);

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

    public void loadProjectData(){
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
        String urlH = url + "api/getProject?project_id="+getIntent().getIntExtra("project_id",1);
        StringRequest stringRequestProject = new StringRequest(Request.Method.GET, urlH, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Do something with the response
                try {
                    JSONObject jsonData = new JSONObject(response);
                    JSONObject JSONdata = jsonData.getJSONObject("data");

                    name.setText(JSONdata.getString("name"));
                    description.setText(JSONdata.getString("description"));
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
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.category_spinner:
                option_category =  parent.getItemAtPosition(position).toString();
                break;
            case R.id.difficulty_spinner:
                option_difficulty = parent.getItemAtPosition(position).toString();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
