package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by Artyom on 6/9/13.
 */
public class User {
    /**
     * Идентификатор пользователя
     */
    public int id;
    /**
     * ФИО пользователя
     */
    public String name;
    /**
     * Количество публикаций
     */
    public int count;
    /**
     * Баланс пользователя
     */
    public Double balance;
    User(JSONObject object){

        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            this.count=object.optInt("count");
            String balanceStr=object.optString("balance");
            if(balance!=null){
                if(balanceStr.contains(".")){
                    balanceStr=balanceStr.substring(0,Math.min(balanceStr.indexOf('.')+3,balanceStr.length()-1));
                }
                this.balance=Double.parseDouble(balanceStr);
            }
        }
    }
    public static Vector<User> fromJSONArray(JSONArray array){
        Vector<User> users=new Vector<User>();
        if(array!=null){
            for(int i=0; i<array.length(); i++){
                users.add(new User(array.optJSONObject(i)));
            }
        }
        return users;
    }
}
