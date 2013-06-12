package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

/**
 * Created by Artyom on 6/1/13.
 * Entity класс для категорий публикаций
 */
public class Category {
    /**
     * Идентификатор категории
     */
    public int id;
    /**
     * Название категории
     */
    public String name;
    /**
     * Родительская категория, может быть null
     */
    public Integer parent;

    /**
     * конструктор из json
     * @param object
     */
    Category(JSONObject object){
        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            this.parent=object.optInt("parent");
        }
    }

    /**
     * Преобразование json массива
     * @param array
     * @return
     */
    public static Vector<Category> fromJSONArray(JSONArray array){
        Vector<Category> categories=new Vector<Category>();
        if(array!=null){
            for(int i=0; i<array.length(); i++){
                categories.add(new Category(array.optJSONObject(i)));
            }
        }
        return categories;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
