package ru.ebook.store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import adapters.ListAdapterGenres;
import data.Category;
import data.Genre;
import library.API;

/**
 * Created by Artyom on 6/10/13.
 */
public class PublicationAddActivity extends ExtendActivity {
    Vector<Genre> genres;
    Spinner spinnerGenre;
    Spinner spinnerCategory;
    Vector<Category> categories;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicationadd);
        spinnerGenre = (Spinner) findViewById(R.id.spinnerGenre);
        spinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);
        new GetGenres().execute();
        new GetCategories().execute();
    }
    public void addPublication(View v){
        EditText name=(EditText)findViewById(R.id.editTextName);
        if(name.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(),R.string.message_fillerror,Toast.LENGTH_SHORT);
        }
        else{
            MakePublication mp=new MakePublication();
            mp.category=categories.get(spinnerCategory.getSelectedItemPosition()).id;
            mp.genre=genres.get(spinnerGenre.getSelectedItemPosition()).id;
            mp.name=name.getText().toString();
            mp.execute();
        }
    }
    public class GetGenres extends AsyncTask<Void, Void, JSONArray> {


        @Override
        protected JSONArray doInBackground(Void... views) {
            API api=API.getInstance();
            try {
                return new JSONArray(api.queryGet("genres",null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array){
            genres=Genre.fromJSONArray(array);
            List<String> list = new ArrayList(genres);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PublicationAddActivity.this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerGenre.setAdapter(dataAdapter);
        }
    }
    public class GetCategories extends AsyncTask<Void, Void, JSONArray> {


        @Override
        protected JSONArray doInBackground(Void... views) {
            API api=API.getInstance();
            try {
                return new JSONArray(api.queryGet("categories",null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array){
            categories=Category.fromJSONArray(array);
            List<String> list = new ArrayList(categories);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PublicationAddActivity.this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategory.setAdapter(dataAdapter);
        }
    }
    public class MakePublication extends AsyncTask<Void, Void, JSONObject> {
        public int genre=0;
        public int category=0;
        public String name="";
        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressDialog=new ProgressDialog(PublicationAddActivity.this);
            mProgressDialog.show();
            mProgressDialog.setMessage(getString(R.string.message_wait));
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            API api=API.getInstance();
            JSONObject params = new JSONObject();
            try {
                params.put("name",name);
                JSONArray arr=new JSONArray();
                arr.put(genre);
                params.put("genre",arr);
                params.put("category",category);
                return new JSONObject(api.queryPost("publication", params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object){
            super.onPostExecute(object);
            mProgressDialog.dismiss();
            if(object.optBoolean("success")){
                Toast.makeText(getBaseContext(),R.string.message_publicationadd,Toast.LENGTH_LONG);
                finish();
            }
            else{
                Toast.makeText(getBaseContext(),object.optString("error"),Toast.LENGTH_LONG);
            }
        }
    }
}