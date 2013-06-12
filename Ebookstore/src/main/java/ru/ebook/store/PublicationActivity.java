package ru.ebook.store;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import adapters.GridAdapterPublications;
import data.Publication;
import library.API;

/**
 * Created by Artyom on 6/2/13.
 */
public class PublicationActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);

        GetPublications task=new GetPublications();
        task.genre=getIntent().getExtras().getInt("genre",0);
        task.category=getIntent().getExtras().getInt("category",0);
        task.query=getIntent().getExtras().getString("query", null);
        task.user=getIntent().getExtras().getInt("author", 0);
        task.execute();
    }
    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        if(getIntent().getExtras().getBoolean("edit", false)){
            getMenuInflater().inflate(R.menu.add, menu);
        }
        boolean res=super.onCreateOptionsMenu(menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Configure the search info and add any event listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                GetPublications task=new GetPublications();
                task.genre=getIntent().getExtras().getInt("genre",0);
                task.category=getIntent().getExtras().getInt("category",0);
                task.query=s;
                task.execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return res;
    }

    public class GetPublications extends AsyncTask<Void, Void, JSONArray> {
        public int genre=0;
        public int category=0;
        public int user=0;
        public String query=null;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //TextView textView=(TextView)findViewById(R.id.textView);
            //textView.setText("");
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONArray doInBackground(Void... voids) {
            API api=API.getInstance();
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            if(genre!=0){
                params.add(new BasicNameValuePair("genre", String.valueOf(this.genre)));
            }
            if(category!=0){
                params.add(new BasicNameValuePair("category", String.valueOf(this.category)));
            }
            if(user!=0){
                params.add(new BasicNameValuePair("user", String.valueOf(this.user)));
            }
            if(query!=null){
                params.add(new BasicNameValuePair("query", this.query));

            }
            try {
                return new JSONArray(api.queryGet("publication", params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array){
            super.onPostExecute(array);
            //TextView textView=(TextView)findViewById(R.id.textView);
            //textView.setText(object.toString());
            GridView gridViewPublications=(GridView)findViewById(R.id.publicationsLayout);


            final Vector<Publication> v=Publication.fromJSONArray(array);
            gridViewPublications.setAdapter(new GridAdapterPublications(PublicationActivity.this, v));
            findViewById(R.id.progressBar).setVisibility(View.GONE);

            gridViewPublications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(PublicationActivity.this,PublicationDetailsActivity.class);
                    Bundle b=new Bundle();
                    b.putInt("publication",(int)l);
                    b.putString("name",v.get(i).name);
                    b.putString("author",v.get(i).author);
                    if(getIntent().getExtras().getBoolean("edit", false)){
                        b.putBoolean("edit",true);
                    }
                    b.putDouble("price",v.get(i).price);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
    }
}