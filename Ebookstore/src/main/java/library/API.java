package library;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artyom on 5/7/13.
 */
public class API {
    /**
     * Тип запроса
     */
    private static String contentType="application/json";
    public static String accessToken;
    private static API instance=null;
    protected String host="http://book-store.azurewebsites.net/";
    protected HttpClient client;
    static Context context = null;
    public static void setContext(Context ctxt) {
        if (context == null)
            context = ctxt;
    }

    /**
     * текущий контекст приложения
     * @return
     */
    public static Context getContext() {

        return  context;
    }

    /**
     * инициализация подключения к интернету
     */
    private API(){
        this.checkHttpClient();
    }

    /**
     * проверка подключения
     */
    private void checkHttpClient(){
        if(this.client==null){
            this.client=new DefaultHttpClient();
        }
    }
    public static synchronized  API getInstance(){
        if(API.instance==null){
            API.instance=new API();
        }
        return API.instance;
    }

    /**
     * GET запрос
     * @param name
     * @param params
     * @return
     */
    public String queryGet(String name,List<NameValuePair> params) {
        HttpGet request;
        StringBuilder paramsString=new StringBuilder();
        if(params==null){
            params=new ArrayList<NameValuePair>();
        }
        params.add(new BasicNameValuePair("access_token",API.accessToken));
        if(params!=null){

            paramsString.append('?');
            for(int i=0; i<params.size(); i++){
                if(i>0){
                    paramsString.append('&');
                }
                paramsString.append(params.get(i).getName());
                paramsString.append('=');
                paramsString.append(params.get(i).getValue());
            }
        }
        String url=this.host+name+paramsString.toString();
        request=new HttpGet(url);

        String cacheString=Cache.getCache(url);
        if(cacheString!=null){
            return cacheString;
        }
        HttpResponse response = null;
        String responseText=null;
        try {
            response = this.client.execute(request);
            HttpEntity entity = response.getEntity();
            responseText = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Cache.putCache(url,responseText);
        return responseText;
    }

    /**
     * POST запрос
     * @param name
     * @param params
     * @return
     */
    public String queryPost(String name,JSONObject params) {

        HttpPost request=new HttpPost(this.host+name+"?access_token="+API.accessToken);
        request.setHeader("Accept", this.contentType);
        request.setHeader("Content-Type", this.contentType);
        if(params!=null){

            try {
                request.setEntity(new StringEntity(params.toString(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        HttpResponse response = null;
        String responseText=null;
        try {
            response = this.client.execute(request);
            HttpEntity entity = response.getEntity();
            responseText = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseText;
    }
}
