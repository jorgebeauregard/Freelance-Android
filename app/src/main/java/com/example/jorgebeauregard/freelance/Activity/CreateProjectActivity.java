package com.example.jorgebeauregard.freelance.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
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

public class CreateProjectActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
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
                String urlG = url + "api/createProject?name="+name_to_send+"&description="+description_to_send+"&user_id=1&difficulty="+option_difficulty+"&category="+category_to_send;
                Toast.makeText(CreateProjectActivity.this, urlG,
                        Toast.LENGTH_LONG).show();

                // Formulate the request and handle the response.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, urlG, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(CreateProjectActivity.this, "Project has been successfully saved",
                                Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(CreateProjectActivity.this, error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
                // Add the request to the RequestQueue.
                mRequestQueue.add(stringRequest);
            }
        });

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

                    setTitle("Create a project");

                    for(int i=0;i<JSONdata.length();i++){
                        category_list.add(((JSONObject)JSONdata.get(i)).getString("name"));
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }

                //Adapter for categories spinner
                ArrayAdapter<String> dataAdapterCategory = new ArrayAdapter<>(CreateProjectActivity.this, R.layout.item_spinner, category_list);
                dataAdapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_category.setAdapter(dataAdapterCategory);
                spinner_category.setOnItemSelectedListener(CreateProjectActivity.this);

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
                ArrayAdapter<String> dataAdapterDifficulty = new ArrayAdapter<>(CreateProjectActivity.this, R.layout.item_spinner, difficulty_list);
                dataAdapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_difficulty.setAdapter(dataAdapterDifficulty);
                spinner_difficulty.setOnItemSelectedListener(CreateProjectActivity.this);

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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch(parent.getId()){
            case R.id.category_spinner:
                option_category =  parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateProjectActivity.this, parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_LONG).show();
            break;
            case R.id.difficulty_spinner:
                option_difficulty = parent.getItemAtPosition(position).toString();
                Toast.makeText(CreateProjectActivity.this, parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_LONG).show();
            break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}