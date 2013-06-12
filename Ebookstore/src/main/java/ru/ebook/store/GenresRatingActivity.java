package ru.ebook.store;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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
 * Created by Artyom on 6/9/13.
 */
public class GenresRatingActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);

        GetGenresRating task=new GetGenresRating();
        task.genre=getIntent().getExtras().getInt("genre", 0);
        task.execute();
    }

    public class GetGenresRating extends AsyncTask<Void, Void, JSONArray> {
        public int genre=0;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONArray doInBackground(Void... voids) {
            API api=API.getInstance();
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            if(genre!=0){
                params.add(new BasicNameValuePair("genre", String.valueOf(this.genre)));
            }
            try {
                return new JSONArray(api.queryGet("genre_rating", params));
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
            gridViewPublications.setAdapter(new GridAdapterPublications(GenresRatingActivity.this,v ));
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            gridViewPublications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent=new Intent(GenresRatingActivity.this,PublicationDetailsActivity.class);
                    Bundle b=new Bundle();
                    b.putInt("publication",(int)l);
                    b.putString("name",v.get(i).name);
                    b.putDouble("price",v.get(i).price);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            });
        }
    }
}
