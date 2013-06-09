package ru.ebook.store;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

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
        ((TextView)findViewById(R.id.publicationPrice)).setText(getIntent().getExtras().getString("price", "") + " " + getString(R.string.rub));
        GetChapters task=new GetChapters();
        task.publication=getIntent().getExtras().getInt("publication",0);
        task.execute();


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

            listViewChapters.setAdapter(new ListAdapterChapters(PublicationDetailsActivity.this, Chapter.fromJSONArray(array)));
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            listViewChapters.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i,final long l) {
                    new AlertDialog.Builder(PublicationDetailsActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle(R.string.message_buy)
                            .setMessage(R.string.message_buy)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

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
            });
        }
    }
}
