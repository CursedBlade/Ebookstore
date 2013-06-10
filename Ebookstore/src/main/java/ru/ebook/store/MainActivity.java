package ru.ebook.store;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import adapters.GridAdapterPublications;
import async.GetCategories;
import async.GetGenres;
import async.GetGenresForRatings;
import data.Category;
import data.Genre;
import data.Publication;
import library.API;
import library.Cache;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DummySectionFragment.activity=this;
        Cache.setContext(getBaseContext());
        API.setContext(getBaseContext());
        setContentView(R.layout.activity_main);
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setCustomView(R.layout.window_title);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
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
                b.putBoolean("add",true);
                b.putString("title",getString(R.string.action_mypublication));
                intent.putExtras(b);
                startActivity(intent);
                return true;
            }
            case R.id.action_balance:{
                Intent intent=new Intent(this,BalanceActivity.class);
                startActivity(intent);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.

            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply
     * displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        public static Activity activity;
        public class GetPublications extends AsyncTask<Void, Void, JSONArray> {
            public int genre=0;
            public int category=0;
            public String query=null;

            @Override
            protected JSONArray doInBackground(Void... voids) {
                API api=API.getInstance();
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("random", "true"));

                params.add(new BasicNameValuePair("limit", "10"));

                try {
                    return new JSONArray(api.queryGet("publication", params));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONArray array){
                super.onPostExecute(array);
                //TextView textView=(TextView)findViewById(R.id.textView);
                //textView.setText(object.toString());
                GridView gridViewPublications=(GridView)activity.findViewById(R.id.publicationsLayout);



                final Vector<Publication> v=Publication.fromJSONArray(array);
                gridViewPublications.setAdapter(new GridAdapterPublications(activity, v));
                activity.findViewById(R.id.progressBar).setVisibility(View.GONE);
                gridViewPublications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent=new Intent(activity,PublicationDetailsActivity.class);
                        Bundle b=new Bundle();
                        b.putInt("publication",(int)l);
                        b.putString("name",v.get(i).name);
                        b.putString("price", v.get(i).price);
                        intent.putExtras(b);
                        startActivity(intent);
                    }
                });
            }
        }
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            int sectionNumber=getArguments().getInt(ARG_SECTION_NUMBER);
            if(sectionNumber==1){
                rootView = inflater.inflate(R.layout.fragment_main_dummy, container, false);
                new GetPublications().execute();

            }
            else if(sectionNumber==2){
                rootView = inflater.inflate(R.layout.fragment_genres, container, false);
                new GetGenresForRatings().execute(rootView);
                ListView listViewGenres = (ListView)rootView.findViewById(R.id.listViewGenres);
                listViewGenres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i>0){
                            Intent intent = new Intent(view.getContext(), GenresRatingActivity.class);
                            Bundle b = new Bundle();
                            b.putInt("genre", (int)l); //Your id
                            b.putString("title", ((Genre) adapterView.getItemAtPosition(i)).name);
                            intent.putExtras(b); //Put your id to your next Intent
                            startActivity(intent);
                        }
                        else{
                            Intent intent = new Intent(view.getContext(), AuthorsRatingActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
            else if(sectionNumber==3){
                rootView = inflater.inflate(R.layout.fragment_categories, container, false);
                new GetCategories().execute(rootView);

                ListView listViewCategories = (ListView)rootView.findViewById(R.id.listViewCategories);
                listViewCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(view.getContext(), PublicationActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("category", (int)l); //Your id
                        b.putString("title", ((Category) adapterView.getItemAtPosition(i)).name);
                        intent.putExtras(b); //Put your id to your next Intent

                        startActivity(intent);
                    }
                });
            }
            else if(sectionNumber==4){
                rootView = inflater.inflate(R.layout.fragment_genres, container, false);
                new GetGenres().execute(rootView);
                ListView listViewGenres = (ListView)rootView.findViewById(R.id.listViewGenres);
                listViewGenres.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(view.getContext(), PublicationActivity.class);
                        Bundle b = new Bundle();
                        b.putInt("genre", (int)l); //Your id
                        b.putString("title", ((Genre) adapterView.getItemAtPosition(i)).name);
                        intent.putExtras(b); //Put your id to your next Intent
                        startActivity(intent);
                    }
                });
            }
            else{
                dummyTextView.setText(String.valueOf(sectionNumber));
            }
            //rootView.
            return rootView;
        }


    }

}
