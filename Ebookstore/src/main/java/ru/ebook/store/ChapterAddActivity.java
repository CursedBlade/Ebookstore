package ru.ebook.store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import library.API;

/**
 * Created by Artyom on 6/10/13.
 */
public class ChapterAddActivity extends ExtendActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapteradd);
    }

    public void addChapter(View v){
        EditText name=(EditText)findViewById(R.id.editTextName);
        EditText price=(EditText)findViewById(R.id.editTextPrice);
        if(name.getText().toString().isEmpty() || price.getText().toString().isEmpty()){
            Toast.makeText(getBaseContext(), R.string.message_fillerror, Toast.LENGTH_SHORT);
        }
        else{
            MakeChapter mc=new MakeChapter();
            mc.publication=getIntent().getExtras().getInt("publication");
            mc.price=price.getText().toString();
            mc.name=name.getText().toString();
            mc.execute();
        }
    }
    public class MakeChapter extends AsyncTask<Void, Void, JSONObject> {
        public String name="";
        public int publication=0;
        public String price="";
        ProgressDialog mProgressDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            mProgressDialog=new ProgressDialog(ChapterAddActivity.this);
            mProgressDialog.show();
            mProgressDialog.setMessage(getString(R.string.message_wait));
        }
        @Override
        protected JSONObject doInBackground(Void... voids) {
            API api=API.getInstance();
            JSONObject params = new JSONObject();
            try {
                params.put("name",name);
                params.put("price",price);
                params.put("publication_id",publication);
                params.put("content","");
                return new JSONObject(api.queryPost("chapters", params));
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

                Toast.makeText(getBaseContext(),R.string.message_chapteradd,Toast.LENGTH_LONG);
                finish();
            }
            else{
                Toast.makeText(getBaseContext(),object.optString("error"),Toast.LENGTH_LONG);
            }
        }
    }
}