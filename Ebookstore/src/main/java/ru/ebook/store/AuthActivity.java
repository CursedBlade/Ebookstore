package ru.ebook.store;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import library.API;

/**
 * Created by Artyom on 5/29/13.
 */
public class AuthActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            String token=getAccessToken();
            if(token!=null){
                API.accessToken=token;
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else{
                setContentView(R.layout.activity_auth);
            }
        }
        catch(Exception e){

        }
    }
    public void tryAuth(View v){
        Auth auth=new Auth();
        auth.email=((EditText)findViewById(R.id.editTextEmail)).getText().toString();
        auth.password=((EditText)findViewById(R.id.editTextPassword)).getText().toString();
        auth.execute();
    }
    public void auth(String access_token, int id){
        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("access_token", access_token);
        editor.putInt("id", id);
        editor.commit();
    }
    public String getAccessToken(){

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("access_token",null);
    }

    /**
     * Асинхронная авторизация
     */
    public class Auth extends AsyncTask<Void, Void, JSONObject> {
        /**
         * Логин
         */
        public String email;
        /**
         * Пароль
         */
        public String password;
        /**
         * Показ прогрессбара
         */
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
        }
        /**
         * Запрос авторизациии
         * @param voids
         * @return
         */
        @Override
        protected JSONObject doInBackground(Void... voids) {
            API api=API.getInstance();
            final JSONObject params = new JSONObject();
            try {
                params.put("email", this.email);
                params.put("password", this.password);
            } catch (JSONException e) {
                e.printStackTrace();
            };
            try {
                return new JSONObject(api.queryPost("auth",params));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        /**
         * Действия после получения ответа от сервера
         * @param object - ответ сервера
         */
        @Override
        protected void onPostExecute(JSONObject object){
            super.onPostExecute(object);
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            if(!object.optString("access_token").isEmpty()){
                auth(object.optString("access_token"),object.optInt("id"));
                startActivity(
                        new Intent(AuthActivity.this, MainActivity.class)
                );
                finish();
            }
            else{
                Toast.makeText(getBaseContext(), R.string.message_autherror, Toast.LENGTH_SHORT).show();
            }
        }
    }
}