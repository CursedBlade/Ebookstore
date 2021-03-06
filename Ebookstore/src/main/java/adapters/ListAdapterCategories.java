package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

import data.Category;
import ru.ebook.store.R;

/**
 * Created by Artyom on 6/1/13.
 */
public class ListAdapterCategories extends BaseAdapter {
    public Vector<Category> items;
    private LayoutInflater mInflater;

    protected Context context;


    public ListAdapterCategories(Context ctxt,Vector<Category> items){
        this.context=ctxt;
        this.items=items;
        this.mInflater = (LayoutInflater)ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return items.get(arg0).id;
    }

    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {
        int id= R.layout.listview_item_category;
        View convertView = mInflater.inflate(id, null);
        TextView txtView=(TextView)convertView.findViewById(R.id.genre);
        txtView.setText(items.get(arg0).name);
        return convertView;
    }

}
