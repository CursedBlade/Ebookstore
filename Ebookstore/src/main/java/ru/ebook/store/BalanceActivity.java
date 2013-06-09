package ru.ebook.store;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
public class BalanceActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        new GetUser().execute();

    }
    public class GetUser extends AsyncTask<Void, Void, JSONObject> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            API api=API.getInstance();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            Context context = getApplicationContext();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

            params.add(new BasicNameValuePair("id",String.valueOf(prefs.getInt("id",0))));
            try {
                return new JSONObject(api.queryGet("user", params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object){
            super.onPostExecute(object);
            ((TextView)findViewById(R.id.balance)).setText(object.optString("balance"));
            ((TextView)findViewById(R.id.name)).setText(object.optString("name"));
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }
}