package ru.ebook.store;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import adapters.ListAdapterGenres;
import data.Category;
import data.Genre;
import library.API;

/**
 * Created by Artyom on 6/10/13.
 */
public class PublicationAddActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicationadd);
        new GetGenres().execute();
        new GetCategories().execute();
    }
    public void addPublication(View v){

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
            Spinner spinner = (Spinner) findViewById(R.id.spinner2);
            List<String> list = new ArrayList(Genre.fromJSONArray(array));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PublicationAddActivity.this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
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
            Spinner spinner = (Spinner) findViewById(R.id.spinner);
            List<String> list = new ArrayList(Category.fromJSONArray(array));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(PublicationAddActivity.this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
        }
    }
}