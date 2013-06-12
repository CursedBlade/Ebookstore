package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by Artyom on 6/9/13.
 * Entity класс для глав
 */
public class Chapter {
    /**
     * Идентификатор главы
     */
    public int id;
    /**
     * Название главы
     */
    public String name;
    /**
     * Цена главы
     */
    public Double price;
    /**
     * Индикатор купленности товара
     */
    public Boolean has;

    /**
     * Конструктор из json
     * @param object
     */
    Chapter(JSONObject object){
        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            String priceStr=object.optString("price");
            if(priceStr.contains(".")){
                priceStr=priceStr.substring(0,Math.min(priceStr.indexOf('.')+3,priceStr.length()-1));
            }
            this.price=Double.parseDouble(priceStr);
            this.has=object.optBoolean("has");
        }
    }

    /**
     * преобразование json массива
     * @param array
     * @return
     */
    public static Vector<Chapter> fromJSONArray(JSONArray array){
        Vector<Chapter> chapters=new Vector<Chapter>();
        if(array!=null){
            for(int i=0; i<array.length(); i++){
                chapters.add(new Chapter(array.optJSONObject(i)));
            }
        }
        return chapters;
    }
}
