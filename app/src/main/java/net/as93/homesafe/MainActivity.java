package net.as93.homesafe;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


/**
 * Created by Alicia on 05/03/2015.
 */
public class MainActivity extends ActionBarActivity {

    private String[] mSideMenuItems = {"View Schedules","Create Schedule","Settings", "About"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setUpGenericGui();

        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new HomeActivity();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: // Open or close the app draw
                final ListView mDrawer = (ListView)findViewById(R.id.left_drawer);
                final DrawerLayout mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(mDrawer);
                }
                else{
                    mDrawerLayout.openDrawer(mDrawer);
                }
                return true;
            case R.id.action_websearch:
                Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
        }
    }

    private void displayView(int position) {

        //Fragment Manager
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();


        switch(position) {
            case 0: // View Schedules
                fragment = new HomeActivity();
            break;

            case 1: // Create Schedule
                fragment = new BlankFragment();
            break;

            case 2: // Settings
                fragment = new BlankFragment();
            break;

            case 3: // About
                fragment = new BlankFragment();
                break;

            default:
                fragment = new BlankFragment();
                break;

        }

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

    }

    public void setUpGenericGui(){

        /* Set XML layout */
        this.setContentView(R.layout.activity_base);

        /* Draw Arrow Toggle */
        CharSequence mTitle = getTitle();
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mSideMenuItems));

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

    }

}
