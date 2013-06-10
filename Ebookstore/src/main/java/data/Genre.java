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
    public int id;
    public String name;
    public Integer parent;
    Genre(){

    }
    Genre(JSONObject object){
        if(object!=null){
            this.id=object.optInt("id");
            this.name=object.optString("name");
            this.parent=object.optInt("parent");
        }
    }
    public static Vector<Genre> fromJSONArray(JSONArray array){
        Vector<Genre> genres=new Vector<Genre>();
        for(int i=0; i<array.length(); i++){
            genres.add(new Genre(array.optJSONObject(i)));
        }
        return genres;
    }
    public static Vector<Genre> fromJSONArrayModified(JSONArray array){
        Vector<Genre> genres=new Vector<Genre>();
        for(int i=0; i<array.length(); i++){
            genres.add(new Genre(array.optJSONObject(i)));
            genres.get(i).name= API.getContext().getString(R.string.message_rating)+" \""+genres.get(i).name+"\"";
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
