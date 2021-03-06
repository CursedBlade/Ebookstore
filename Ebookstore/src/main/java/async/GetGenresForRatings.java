package async;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import adapters.ListAdapterGenres;
import data.Genre;
import library.API;
import ru.ebook.store.R;

/**
 * Created by Artyom on 6/9/13.
 */
public class GetGenresForRatings extends AsyncTask<View, Void, JSONArray> {
    View view;


    @Override
    protected JSONArray doInBackground(View... views) {
        this.view = views[0];
        API api=API.getInstance();
        try {
            return new JSONArray(api.queryGet("genres",null));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONArray array){
        ListView listViewGenres = (ListView)view.findViewById(R.id.listViewGenres);
        ListAdapterGenres adapter=new ListAdapterGenres(view.getContext(), Genre.fromJSONArrayModified(array));
        listViewGenres.setAdapter(adapter);
        /*TextView textView=new TextView(view.getContext());
        textView.setText((Genre.fromJSONArray(array)).get(0).name);
        listViewGenres.addView(textView);*/

    }
}