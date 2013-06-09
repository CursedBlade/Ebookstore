package async;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import adapters.ListAdapterCategories;
import data.Category;
import library.API;
import ru.ebook.store.R;

/**
 * Created by Artyom on 5/27/13.
 */

public class GetCategories extends AsyncTask<View, Void, JSONArray> {
    View view;


    @Override
    protected JSONArray doInBackground(View... views) {
        this.view = views[0];
        API api=API.getInstance();
        try {
            return new JSONArray(api.queryGet("categories",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray array){
        ListView listViewGenres = (ListView)view.findViewById(R.id.listViewCategories);
        ListAdapterCategories adapter=new ListAdapterCategories(view.getContext(), Category.fromJSONArray(array));
        listViewGenres.setAdapter(adapter);
        /*TextView textView=new TextView(view.getContext());
        textView.setText((Genre.fromJSONArray(array)).get(0).name);
        listViewGenres.addView(textView);*/

    }
}
