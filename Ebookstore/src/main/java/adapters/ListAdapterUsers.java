package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Vector;

import data.User;
import ru.ebook.store.R;

/**
 * Created by Artyom on 6/9/13.
 */
public class ListAdapterUsers extends BaseAdapter {
    public Vector<User> items;
    private LayoutInflater mInflater;

    protected Context context;


    public ListAdapterUsers(Context ctxt,Vector<User> items){
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
        int id= R.layout.listview_item_user;
        View convertView = mInflater.inflate(id, null);
        TextView txtView=(TextView)convertView.findViewById(R.id.user);
        txtView.setText((arg0+1)+". "+items.get(arg0).name);
        return convertView;
    }
}
