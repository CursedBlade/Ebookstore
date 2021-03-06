package ru.ebook.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import adapters.ListAdapterChapters;
import data.Chapter;
import library.API;

/**
 * Created by Artyom on 6/9/13.
 */
public class PublicationDetailsActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicationdetails);
        ((TextView)findViewById(R.id.publicationName)).setText(getIntent().getExtras().getString("name", ""));
        ((TextView)findViewById(R.id.publicationAuthor)).setText(getIntent().getExtras().getString("author", ""));
        ((TextView)findViewById(R.id.publicationPrice)).setText(getIntent().getExtras().getDouble("price", 0) + " " + getString(R.string.rub));
        GetChapters task=new GetChapters();
        task.publication=getIntent().getExtras().getInt("publication",0);
        task.execute();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getIntent().getExtras().getBoolean("edit", false)){
            getMenuInflater().inflate(R.menu.addchapter, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
    public class GetChapters extends AsyncTask<Void, Void, JSONArray> {
        public int publication=0;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONArray doInBackground(Void... voids) {
            API api=API.getInstance();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("publication",String.valueOf(publication)));
            try {
                return new JSONArray(api.queryGet("chapters", params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array){
            super.onPostExecute(array);
            ListView listViewChapters=(ListView)findViewById(R.id.listViewChapters);
            final Vector<Chapter> v=Chapter.fromJSONArray(array);
            listViewChapters.setAdapter(new ListAdapterChapters(PublicationDetailsActivity.this, v));
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            listViewChapters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, final int i,final long l) {
                    if(!v.get(i).has && v.get(i).price!=0){
                    new AlertDialog.Builder(PublicationDetailsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.message_buy)
                            .setMessage(R.string.message_buy)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    MakePurchase mp=new MakePurchase();
                                    mp.chapter=(int)l;
                                    mp.execute();
                                    v.get(i).has=true;
                                    Intent intent = new Intent(getBaseContext(), ChapterActivity.class);
                                    Bundle b = new Bundle();
                                    b.putInt("chapter", (int)l); //Your id
                                    b.putInt("publication", publication); //Your id
                                    intent.putExtras(b); //Put your id to your next Intent
                                    startActivity(intent);
                                }

                            })
                            .setNegativeButton(R.string.no, null)
                            .show();
                    }
                    else{

                        Intent intent = new Intent(getBaseContext(), ChapterActivity.class);
                        Bundle b = new Bundle();
                        if(getIntent().getExtras().getBoolean("edit", false)){
                            b.putBoolean("edit",true);
                        }
                        b.putInt("chapter", (int)l); //Your id
                        b.putInt("publication", publication); //Your id
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
                    }
                }
            });
        }
    }
    public class MakePurchase extends AsyncTask<Void, Void, JSONObject> {
        public int chapter=0;
        @Override
        protected JSONObject doInBackground(Void... voids) {
            API api=API.getInstance();
            JSONObject params = new JSONObject();
            try {
                params.put("chapter",chapter);
                return new JSONObject(api.queryPost("purchases", params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object){
            super.onPostExecute(object);
        }
    }
}
