package net.as93.homesafe;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.as93.homesafe.fragments.BlankFragment;
import net.as93.homesafe.fragments.HomeFragment;
import net.as93.homesafe.menu.MenuAdapter;
import net.as93.homesafe.menu.MenuModel;

import java.util.ArrayList;


/**
 * Created by Alicia on 05/03/2015.
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        setUpGenericGui();

        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragment = new HomeFragment();
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
        switch (item.getItemId()) {
            case android.R.id.home: // Open or close the app draw
                toggleDrawer();
                return true;
            case R.id.action_websearch:
                Toast.makeText(this, "Hello World", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Opens of closes drawer.
     */
    private void toggleDrawer() {
        final ListView mDrawer = (ListView) findViewById(R.id.left_drawer);
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(mDrawer);
        } else {
            mDrawerLayout.openDrawer(mDrawer);
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayView(position);
            ListView mDrawer = (ListView) findViewById(R.id.left_drawer);
            mDrawer.setItemChecked(position, true);
            toggleDrawer();
        }
    }

    private void displayView(int position) {

        switch (position) {
            case 0: // View Schedules
                inflateFragmentView(new HomeFragment());
                break;

            case 1: // Create Schedule
                Intent i = new Intent(this, CreateSchedule.class);
                startActivity(i);
                toggleDrawer();
                break;

            case 2: // Settings
                inflateFragmentView(new BlankFragment());
                break;

            case 3: // About
                inflateFragmentView(new BlankFragment());
                break;

        }

    }

    private void inflateFragmentView(Fragment fragment) {
        //Fragment Manager
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void setUpGenericGui() {

        /* Set XML layout */
        this.setContentView(R.layout.activity_base);

        /* Draw Arrow Toggle */
        CharSequence mTitle = getTitle();
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        MenuAdapter adapter = new MenuAdapter(this, generateData());
        mDrawerList.setAdapter(adapter);

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

    private ArrayList<MenuModel> generateData(){
        ArrayList<MenuModel> models = new ArrayList<MenuModel>();
        models.add(new MenuModel(R.drawable.ico_view,"View Schedules","1"));
        models.add(new MenuModel(R.drawable.ico_create,"Create Schedule","2"));
        models.add(new MenuModel(R.drawable.ico_settings,"Settings","3"));
        models.add(new MenuModel(R.drawable.ico_about,"About","4"));
        return models;
    }

}