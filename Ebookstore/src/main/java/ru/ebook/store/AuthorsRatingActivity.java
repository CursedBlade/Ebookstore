package ru.ebook.store;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import adapters.ListAdapterUsers;
import data.User;
import library.API;

/**
 * Created by Artyom on 6/9/13.
 */
public class AuthorsRatingActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorsrating);

        new GetAuthorsRating().execute();
    }

    public class GetAuthorsRating extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
        @Override
        protected JSONArray doInBackground(Void... voids) {
            API api=API.getInstance();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            try {
                return new JSONArray(api.queryGet("writer_rating", params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray array){
            super.onPostExecute(array);
            ListView listViewUsers=(ListView)findViewById(R.id.listViewUsers);
            listViewUsers.setAdapter(new ListAdapterUsers(AuthorsRatingActivity.this, User.fromJSONArray(array)));
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }
}
