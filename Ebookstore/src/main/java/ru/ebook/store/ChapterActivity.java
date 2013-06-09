package ru.ebook.store;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import library.API;

/**
 * Created by Artyom on 6/9/13.
 */
public class ChapterActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        GetChapter task=new GetChapter();
        task.chapter=getIntent().getExtras().getInt("chapter",0);
        task.publication=getIntent().getExtras().getInt("publication",0);
        task.execute();


    }

    public class GetChapter extends AsyncTask<Void, Void, JSONObject> {
        public int chapter=0;
        public int publication=0;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            API api=API.getInstance();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id",String.valueOf(chapter)));
            try {
                return new JSONObject(api.queryGet("chapters", params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object){
            super.onPostExecute(object);
            ((TextView)findViewById(R.id.textViewContent)).setText(object.optString("content"));
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }
}
