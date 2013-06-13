package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by Artyom on 6/1/13.
 */
public class Publication {
    /**
     * Идентификатор публикации
     */
    public int id;
    /**
     * Название публикации
     */
    public String name;
    /**
     * Цена публикации
     */
    public Double price;
    /**
     * Автор публикации
     */
    public String author;

    /**
     * конструктор из json объекта
     * @param object
     */
    Publication(JSONObject object){
        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            this.author=object.optString("author_name");
            String priceStr=object.optString("price");
            if(priceStr.contains(".")){
                priceStr=priceStr.substring(0,Math.min(priceStr.indexOf('.')+3,priceStr.length()-1));
            }
            this.price=Double.parseDouble(priceStr);
        }
    }

    /**
     * Преобразование из json массива
     * @param array
     * @return
     */
    public static Vector<Publication> fromJSONArray(JSONArray array){
        Vector<Publication> publications=new Vector<Publication>();
        if(array!=null){
            for(int i=0; i<array.length(); i++){
                publications.add(new Publication(array.optJSONObject(i)));
            }
        }
        return publications;
    }
}
