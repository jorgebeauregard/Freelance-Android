package com.example.jorgebeauregard.freelance;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.example.jorgebeauregard.freelance.Activity.AllMyProjectsActivity;
import com.example.jorgebeauregard.freelance.Activity.AllProjectsActivity;
import com.example.jorgebeauregard.freelance.Activity.LoginActivity;
import com.example.jorgebeauregard.freelance.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterUserActivity extends AppCompatActivity {

    private LoginActivity.UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private AutoCompleteTextView username;
    private AutoCompleteTextView name;
    private EditText password;
    private View mProgressView;
    private View mLoginFormView;
    private SharedPreferences preferences;
    final String url = "http://10.50.92.115:8000/";
    public static final String EXTRA_MESSAGE = "com.example.Freelance.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Button register = (Button) findViewById(R.id.register);
        mEmailView = (AutoCompleteTextView)findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        name=(AutoCompleteTextView)findViewById(R.id.name);
        preferences = getSharedPreferences("user",MODE_PRIVATE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doRegister();
            }
        });
    }
    public void doRegister() {
        String usuario = mEmailView.getText().toString();
        String contrase = mPasswordView.getText().toString();
        String nombre = name.getText().toString();
        RequestQueue mRequestQueue;
        // Instantiate the cache
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        // Set up the network to use HttpURLConnection as the HTTP client.
        BasicNetwork network = new BasicNetwork(new HurlStack());
        // Instantiate the RequestQueue with the cache and network.
        mRequestQueue = new RequestQueue(cache, network);
        // Start the queue
        mRequestQueue.start();
        String register = url + "api/register?name=" + nombre+"&email=" + usuario + "&password=" + contrase;
        final Context c = this;
        // Formulate the request and handle the response.
        //Toast.makeText(getBaseContext(),"prueba", Toast.LENGTH_SHORT).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, register, new Response.Listener<String>() {
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
                    if (Integer.parseInt(state) == 409) {
                        Toast.makeText(getBaseContext(), "User already exists", Toast.LENGTH_SHORT).show();
                    }
                    if (Integer.parseInt(state) == 200) {
                        JSONdata = jsonData.getJSONObject("data");
                        String id = JSONdata.getString("user_id");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("user_id", id);
                        editor.apply();
                        Intent intent = new Intent(RegisterUserActivity.this,AllProjectsActivity.class);
                        intent.putExtra(EXTRA_MESSAGE, id);
                        startActivity(intent);
                        //Toast.makeText(getBaseContext(), id, Toast.LENGTH_SHORT).show();
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
}
