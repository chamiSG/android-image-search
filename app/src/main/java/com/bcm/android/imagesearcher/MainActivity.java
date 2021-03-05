package com.bcm.android.imagesearcher;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Web api url
    String DATA_URL;

    //Tag values to read from json
    public static final String TAG_IMAGE_URL = "image";
    public static final String TAG_NAME = "name";

    //GridView Object
    private GridView gridView;

    //ArrayList for Storing image urls and titles
    private ArrayList<String> images;
    private ArrayList<String> names;
    EditText searchView;
    ImageButton searchBtn;
    String key;
    private ContextWrapper mContext;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = (EditText) findViewById(R.id.search_input);
        searchBtn = (ImageButton) findViewById(R.id.btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = searchView.getText().toString();
                DATA_URL = "https://eatbcm.com.au/upload_img/search.php?key=" + key;
                gridView = (GridView) findViewById(R.id.gridView);
                images = new ArrayList<>();
                names = new ArrayList<>();
                //Calling the getData method
                getData(DATA_URL);
            }
        });
    }

    private void getData(String url){

        //Showing a progress dialog while our app fetches the data from url
        final ProgressDialog loading = ProgressDialog.show(this, "Please wait...","Fetching data...",false,false);
        //Creating a json array request to get the json from our api
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Dismissing the progressdialog on response
                        loading.dismiss();
                        //Displaying our grid
                        showGrid(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding our request to the queue
        requestQueue.add(jsonArrayRequest);
    }

    private void showGrid(JSONArray jsonArray) {
        if( jsonArray.length() == 0 ){
            Toast.makeText(this,"No match image.",Toast.LENGTH_LONG).show();
            return;
        }
        //Looping through all the elements of json array
        for (int i = 0; i < jsonArray.length(); i++) {
            //Creating a json object of the current index
            JSONObject obj = null;
            try {
                //getting json object from current index
                obj = jsonArray.getJSONObject(i);
                //getting image url and title from json object
                images.add(obj.getString(TAG_IMAGE_URL));
                names.add(obj.getString(TAG_NAME));
//                Log.e("url", obj.getString(TAG_IMAGE_URL));
                Intent intent = new Intent(MainActivity.this, ImageViewer.class);
                intent.putExtra("image", images.get(i));
                startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        //Creating GridViewAdapter Object
//        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, images, names);
//        //Adding adapter to gridview
//        gridView.setAdapter(gridViewAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//        });
    }
}

