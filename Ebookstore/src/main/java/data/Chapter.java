package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by Artyom on 6/9/13.
 */
public class Chapter {
    public int id;
    public String name;
    public String price;
    Chapter(JSONObject object){
        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            this.price=String.format("%.2f", object.optDouble("price"));
        }
    }
    public static Vector<Chapter> fromJSONArray(JSONArray array){
        Vector<Chapter> chapters=new Vector<Chapter>();
        for(int i=0; i<array.length(); i++){
            chapters.add(new Chapter(array.optJSONObject(i)));
        }
        return chapters;
    }
}
