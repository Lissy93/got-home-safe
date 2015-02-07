package net.as93.homesafe;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ListActivity {

//    private SimpleAdapter sa;
    ListView lstSchedules;

    private ProgressDialog m_ProgressDialog = null;
    private ArrayList<Schedule> m_schedules = null;
    private ScheduleAdapter m_adapter;
    private Runnable viewSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUpGui();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void setUpGui(){
        /* Hide title bar and set XML layout */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_main);

        /* Get elements */
        TextView txtTitle = (TextView)findViewById(R.id.txtTitle);
        Button btnAddNew  = (Button)findViewById(R.id.btnAddNew);
//        lstSchedules = (ListView)findViewById(R.id.list);

        /* Set Fonts */
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Raleway.ttf");
        txtTitle.setTypeface(myTypeface);

        /* Get Schedules and Populate List */
        m_schedules = new ArrayList<Schedule>();
        this.m_adapter = new ScheduleAdapter(this, R.layout.list_item, m_schedules);
        setListAdapter(this.m_adapter);
        viewSchedules = new Runnable(){
            @Override
            public void run() {
                getSchedules();
            }
        };
        Thread thread =  new Thread(null, viewSchedules, "MagentoBackground");
        thread.start();
        m_ProgressDialog = ProgressDialog.show(MainActivity.this,
                "Please wait...", "Retrieving data ...", true);


        /* Add New Schedule Button Event */
        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapSelect.class);
                startActivity(intent);
            }
        });
    }

    private Runnable returnRes = new Runnable() {

        @Override
        public void run() {
            if(m_schedules != null && m_schedules.size() > 0){
                m_adapter.notifyDataSetChanged();
                for(int i=0;i<m_schedules.size();i++)
                    m_adapter.add(m_schedules.get(i));
            }
            m_ProgressDialog.dismiss();
            m_adapter.notifyDataSetChanged();
        }
    };

    private void getSchedules(){
        AppData ad = new AppData(getApplicationContext());

        //Dummy Data
        Schedule s1 = new Schedule();
        s1.setId(1);
        s1.setContact("07712044239");
        s1.setLocation("Andover");
        s1.setMessage("Arrived at Andover");
        Schedule s2 = new Schedule();
        s2.setId(2);
        s2.setContact("07717477592");
        s2.setLocation("OX1 1RX");
        s2.setMessage("Yo, am at Falklands House");
        Schedule s3 = new Schedule();
        s3.setId(3);
        s3.setContact("07412194121");
        s3.setLocation("E1 1ES");
        s3.setMessage("Arrived home safely");
        ArrayList<Schedule> dummyData = new ArrayList<Schedule>();
        dummyData.add(s1);
        dummyData.add(s2);
        dummyData.add(s3);

        ad.setScheduleList(dummyData);
        ad.writeToDb();


        ad.readFromDb();
        m_schedules = new ArrayList<Schedule>(ad.getAllSchedules());
    try {
        Thread.sleep(500);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    runOnUiThread(returnRes);
}


    private class ScheduleAdapter extends ArrayAdapter<Schedule> {

        private ArrayList<Schedule> items;

        public ScheduleAdapter(Context context, int textViewResourceId, ArrayList<Schedule> items) {
            super(context, textViewResourceId, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.list_item, null);
            }
            Schedule s = items.get(position);
            if (s != null) {
                TextView tt = (TextView) v.findViewById(R.id.contact_location);
                TextView bt = (TextView) v.findViewById(R.id.message);
                if (tt != null) {
                    tt.setText("To: "+s.getContact()+"  Location: "+s.getLocation());                            }
                if(bt != null){
                    bt.setText("Message: "+ s.getMessage());
                }
            }
            return v;
        }
    }




}




