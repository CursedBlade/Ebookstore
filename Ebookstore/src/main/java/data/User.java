package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by Artyom on 6/9/13.
 */
public class User {
    public int id;
    public String name;
    public int count;
    User(JSONObject object){

        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            this.count=object.optInt("count");
        }
    }
    public static Vector<User> fromJSONArray(JSONArray array){
        Vector<User> users=new Vector<User>();
        for(int i=0; i<array.length(); i++){
            users.add(new User(array.optJSONObject(i)));
        }
        return users;
    }
}
