package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Vector;

import library.API;
import ru.ebook.store.R;

/**
 * Created by Artyom on 6/1/13.
 */
public class Genre {
    /**
     * идентификатор жанра
     */
    public int id;
    /**
     * название жанра
     */
    public String name;
    /**
     * Родительский жанр, может быть null
     */
    public Integer parent;
    Genre(){

    }

    /**
     * конструктор из json
     * @param object
     */
    Genre(JSONObject object){
        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            this.parent=object.optInt("parent");
        }
    }

    /**
     * преобразование из json массива
     * @param array
     * @return
     */
    public static Vector<Genre> fromJSONArray(JSONArray array){
        Vector<Genre> genres=new Vector<Genre>();
        if(array!=null){
            for(int i=0; i<array.length(); i++){
                genres.add(new Genre(array.optJSONObject(i)));
            }
        }
        return genres;
    }

    /**
     * преобразование из json массива с модификацией для списка рейтингов
     * @param array
     * @return
     */
    public static Vector<Genre> fromJSONArrayModified(JSONArray array){
        Vector<Genre> genres=new Vector<Genre>();
            if(array!=null){
                for(int i=0; i<array.length(); i++){
                    genres.add(new Genre(array.optJSONObject(i)));
                    genres.get(i).name= API.getContext().getString(R.string.message_rating)+" \""+genres.get(i).name+"\"";
                }
            }
        Genre f=new Genre();

        f.name=API.getContext().getString(R.string.title_authorsrating);
        genres.insertElementAt(f,0);
        return genres;
    }
    @Override
    public String toString(){
        return this.name;
    }
}
