package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

import data.Chapter;
import library.API;
import ru.ebook.store.R;

/**
 * Created by Artyom on 6/9/13.
 */
public class ListAdapterChapters extends BaseAdapter {
    public Vector<Chapter> items;
    private LayoutInflater mInflater;

    protected Context context;


    public ListAdapterChapters(Context ctxt,Vector<Chapter> items){
        this.context=ctxt;
        this.items=items;
        this.mInflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return items.get(arg0).id;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        int id= R.layout.listview_item_chapter;
        View convertView = mInflater.inflate(id, null);
        TextView txtView=(TextView)convertView.findViewById(R.id.chapter);
        if(items.get(arg0).has){
            txtView.setText(items.get(arg0).name+" ("+API.getContext().getString(R.string.purchased)+")");
        }
        else{
            txtView.setText(items.get(arg0).name+" ("+items.get(arg0).price+" "+ API.getContext().getString(R.string.rub)+")");
        }
        return convertView;
    }

}
