package net.as93.homesafe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Alicia on 10/02/2015.
 */
public class MainActivity extends ActionBarActivity  {


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private String[] mSideMenuItems = {"View Schedules","Create Schedule","Settings", "About"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpGui(savedInstanceState);
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
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



    private void displayView(int position) {
        switch(position) {
            case 0: // View Schedules

            case 1: // Create Schedule

            case 2: // Settings

            case 3: // About

            default:

        }
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void setUpGui(Bundle savedInstanceState){

        /* Set XML layout */
        this.setContentView(R.layout.activity_main);

        /* Get elements */
        ImageButton btnAddNew  = (ImageButton)findViewById(R.id.btnAddNew);


        /* Draw Arrow Toggle */
        mTitle =  getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mSideMenuItems));

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawerLayout, R.drawable.ic_drawer,
                R.string.drawer_open, R.string.drawer_close
        );
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());



        /* Recycler View */
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.schedule_recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        AppData ad = new AppData(getApplicationContext());






/*  =============== Writing Dummy Data to Database ==================*/ /*
        //Dummy Data
        Schedule s1 = new Schedule();
        s1.setId(1);
        s1.setContact("07712044238");
        s1.setLocation("Andover");
        s1.setMessage("Arrived at Andover");
        Schedule s2 = new Schedule();
        s2.setId(2);
        s2.setContact("07717477592");
        s2.setLocation("OX1 1RX");
        s2.setMessage("Yo, am on Oxpens Rd");
        Schedule s3 = new Schedule();
        s3.setId(3);
        s3.setContact("07412194151");
        s3.setLocation("E1 1ES");
        s3.setMessage("Arrived home safely");

        ArrayList<Schedule> dummyData = new ArrayList<>();
        dummyData.add(s1);
        dummyData.add(s2);
        dummyData.add(s3);

        ad.setScheduleList(dummyData);
        ad.writeToDb();
        */

        ad.readFromDb();

        if(ad.getAllSchedules()!=null) {
            ArrayList<Schedule> schedulesData = new ArrayList<>(ad.getAllSchedules());
            RecyclerView.Adapter mAdapter = new MyAdapter(schedulesData);
            mRecyclerView.setAdapter(mAdapter);
        }
        else{
            /* Show the user the adding schedules message */
            showNoSchedulesInfo();

        }
        /* Add New Schedule Button Event */
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapSelect.class);
                startActivity(intent);
            }
        });
    }

    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        Fragment fragment;
//        FragmentManager fragmentManager = getFragmentManager(); // For AppCompat use getSupportFragmentManager
        switch(position) {
            default:
            case 0:
                Toast.makeText(this, "Hello World 0 ", Toast.LENGTH_LONG).show();

                //fragment = new MyFragment1();
                break;
            case 1:
                Toast.makeText(this, "Hello World 1 ", Toast.LENGTH_LONG).show();

                //fragment = new MyFragment2();
                break;
        }
//        fragmentManager.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit();
    }


    public void showNoSchedulesInfo(){

        /* Liniar layout to hold all related components */
        LinearLayout linearLayout= new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(30,40,30,10);

        /* ImageView to show a nice picture of something vaguley relevant*/
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.welcome);
        imageView.setPadding(5,30,5,10);

        Typeface fontRaleway = Typeface.createFromAsset(getAssets(), "fonts/Raleway.ttf");

        /* Heading 1 and properties */
        TextView h1 = new TextView(this);
        h1.setTypeface(fontRaleway);
        h1.setTextSize(28);
        h1.setPadding(5, 30, 5, 5);
        h1.setGravity(Gravity.CENTER);
        h1.setText("Welcome to HomeSafe");

        /* Heading 2 and properties */
        TextView h2 = new TextView(this);
        h2.setTypeface(fontRaleway);
        h2.setTextSize(20);
        h2.setPadding(5,30,5,5);
        h2.setGravity(Gravity.CENTER);
        h2.setText("The location scheduling SMS app");

        /* Heading 3 and properties */
        TextView h3 = new TextView(this);
        h3.setTypeface(fontRaleway);
        h3.setTextSize(20);
        h3.setPadding(5,30,5,5);
        h3.setGravity(Gravity.CENTER);
        h3.setText("To get started create a new schedule by touching the pink button below to add an SMS schedule");

        /* Add everything to the liniar layout*/
        linearLayout.addView(h1);
        linearLayout.addView(h2);
        linearLayout.addView(imageView);
        linearLayout.addView(h3);

        /* put the liniear layout containing everything on the screen */
        RelativeLayout main = (RelativeLayout)findViewById(R.id.main);
        main.addView(linearLayout);

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private ArrayList<Schedule> mDataset;

        public  class ViewHolder extends RecyclerView.ViewHolder {
            public LinearLayout mTextView;
            public ViewHolder(View v) {
                super(v);
                mTextView = (LinearLayout) v;
            }
        }

        public MyAdapter(ArrayList<Schedule> myDataset) {
            mDataset = myDataset;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            ViewHolder holder = new ViewHolder(v);
            return holder;
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            View v = holder.mTextView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            Schedule s = mDataset.get(position);
            if (s != null) {
                TextView tt = (TextView) v.findViewById(R.id.contact_location);
                TextView bt = (TextView) v.findViewById(R.id.message);
                if (tt != null) {
                    tt.setText("To: "+s.getContact()+"  Location: "+s.getLocation());                            }
                if(bt != null){
                    bt.setText("Message: "+ s.getMessage());
                }
            }



        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }


    }



}