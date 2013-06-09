package ru.ebook.store;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

/**
 * Created by Artyom on 6/9/13.
 */
abstract public class ExtendActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        if(getIntent().getExtras()!=null && getIntent().getExtras().getString("title")!=null){
            setTitle(getIntent().getExtras().getString("title"));
        }
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        super.onCreate(savedInstanceState);


    }
    @Override
     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        // Configure the search info and add any event listeners
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Intent intent = new Intent(getBaseContext(), PublicationActivity.class);
                Bundle b = new Bundle();
                b.putString("query", s); //Your id
                intent.putExtras(b); //Put your id to your next Intent
                startActivity(intent);
                finish();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                finish();
                return true;
            case R.id.action_mypublications:{
                Intent intent=new Intent(this,PublicationActivity.class);
                Bundle b=new Bundle();
                Context context = getApplicationContext();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                b.putInt("author",prefs.getInt("id",0));
                b.putString("title",getString(R.string.action_mypublication));
                intent.putExtras(b);
                startActivity(intent);
            }
            case R.id.action_balance:{
                Intent intent=new Intent(this,BalanceActivity.class);
                startActivity(intent);
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}