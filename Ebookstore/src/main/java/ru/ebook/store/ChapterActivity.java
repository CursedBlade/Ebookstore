package ru.ebook.store;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    static boolean isEdit=false;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        GetChapter task=new GetChapter();
        task.chapter=getIntent().getExtras().getInt("chapter",0);
        task.publication=getIntent().getExtras().getInt("publication",0);
        task.execute();


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(getIntent().getExtras().getBoolean("edit", false)){
            getMenuInflater().inflate(R.menu.editchapter, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.action_editchapter:{
                if(isEdit){
                    item.setIcon(R.drawable.ic_action_edit);
                    findViewById(R.id.editTextContent).setVisibility(View.GONE);
                    findViewById(R.id.textViewContent).setVisibility(View.VISIBLE);
                    String newText=((EditText)findViewById(R.id.editTextContent)).getText().toString();
                    ((TextView)findViewById(R.id.textViewContent)).setText(newText);
                    SaveChapter sc=new SaveChapter();
                    sc.chapter=getIntent().getExtras().getInt("chapter",0);
                    sc.content=newText;
                    sc.execute();
                    isEdit=false;

                }
                else{
                    item.setIcon(R.drawable.ic_action_done);
                    findViewById(R.id.editTextContent).setVisibility(View.VISIBLE);
                    findViewById(R.id.textViewContent).setVisibility(View.GONE);
                    findViewById(R.id.editTextContent).setFocusable(true);
                    isEdit=true;
                }
                return true;
            }
            default:{
                return super.onOptionsItemSelected(item);
            }
        }
    }
    public class SaveChapter extends AsyncTask<Void, Void, JSONObject> {
        public String content="";
        public int chapter=0;
        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressDialog=new ProgressDialog(ChapterActivity.this);
            mProgressDialog.show();
            mProgressDialog.setMessage(getString(R.string.message_wait));
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            API api=API.getInstance();
            JSONObject params = new JSONObject();
            try {
                params.put("id",chapter);
                params.put("content",content);
                return new JSONObject(api.queryPost("chapters/edit", params));
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

                Toast.makeText(ChapterActivity.this, R.string.message_chaptersave, Toast.LENGTH_LONG);
                finish();
            }
            else{
                Toast.makeText(ChapterActivity.this,object.optString("error"),Toast.LENGTH_LONG);
            }
        }
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
            params.add(new BasicNameValuePair("publication",String.valueOf(publication)));
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
            ((EditText)findViewById(R.id.editTextContent)).setText(object.optString("content"));
            findViewById(R.id.progressBar).setVisibility(View.GONE);
        }
    }
}
