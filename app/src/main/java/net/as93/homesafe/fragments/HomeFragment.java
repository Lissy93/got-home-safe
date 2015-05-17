package net.as93.homesafe.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.as93.homesafe.CreateSchedule;
import net.as93.homesafe.R;
import net.as93.homesafe.data.Schedule;
import net.as93.homesafe.data.AppData;

import java.util.ArrayList;

/**
 * Created by Alicia on 10/02/2015.
 */
public class HomeFragment extends Fragment {

    private boolean noSchedules;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_fragment, container, false);
        setUpViewSchedulesGui(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(noSchedules){
            showNoSchedulesInfo();
        }

        /* Add New Schedule Button Event */
        ImageButton btnAddNew = (ImageButton)getActivity().findViewById(R.id.btnAddNew);
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), CreateSchedule.class);
                startActivity(i);
            }
        });
    }


    public void setUpViewSchedulesGui(View view){

        /* Prepare Data for Recycler View */
        AppData ad = new AppData(getActivity().getApplicationContext());


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
        ad.writeToDb(); */

        ad.readFromDb();


        ArrayList<Schedule> schedulesData = new ArrayList<>(ad.getAllSchedules());
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.schedule_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new MyAdapter(schedulesData);
        mRecyclerView.setAdapter(mAdapter);


        if(ad.getAllSchedules()==null || ad.getAllSchedules().size()<1) {
            noSchedules = true;
            //showNoSchedulesInfo();
        }

        else{ noSchedules = false; }


    }


    public void showNoSchedulesInfo(){

        /* Liniar layout to hold all related components */
        LinearLayout linearLayout= new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(30,40,30,10);

        /* ImageView to show a nice picture of something vaguley relevant*/
        ImageView imageView = new ImageView(getActivity());
        imageView.setImageResource(R.drawable.welcome);
        imageView.setPadding(5,30,5,10);

        Typeface fontRaleway = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Raleway.ttf");

        /* Heading 1 and properties */
        TextView h1 = new TextView(getActivity());
        h1.setTypeface(fontRaleway);
        h1.setTextSize(28);
        h1.setPadding(5, 30, 5, 5);
        h1.setGravity(Gravity.CENTER);
        h1.setText("Welcome to HomeSafe");

        /* Heading 2 and properties */
        TextView h2 = new TextView(getActivity());
        h2.setTypeface(fontRaleway);
        h2.setTextSize(20);
        h2.setPadding(5,30,5,5);
        h2.setGravity(Gravity.CENTER);
        h2.setText("The location scheduling SMS app");

        /* Heading 3 and properties */
        TextView h3 = new TextView(getActivity());
        h3.setTypeface(fontRaleway);
        h3.setTextSize(20);
        h3.setPadding(5,30,5,5);
        h3.setGravity(Gravity.CENTER);
        h3.setText("To get started create a new schedule by touching the pink button below");

        /* Add everything to the liniar layout*/
        linearLayout.addView(h1);
        linearLayout.addView(h2);
        linearLayout.addView(imageView);
        linearLayout.addView(h3);

        /* set layout params */
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        /* put the liniear layout containing everything on the screen */
        RelativeLayout main = (RelativeLayout)getActivity().findViewById(R.id.main);
        main.addView(linearLayout, params);
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
                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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