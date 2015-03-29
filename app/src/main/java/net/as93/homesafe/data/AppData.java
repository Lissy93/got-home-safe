package net.as93.homesafe.data;

import android.content.Context;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alicia on 10/11/2014.
 */
public class AppData implements IAppData {

    ArrayList<Schedule> scheduleList;
    Context c;
    TinyDB tinydb;
    Gson gson = new Gson();


    public AppData(Context c){
        this.c = c;
        tinydb = new TinyDB(c);
    }

    public void addSchedule(Schedule newSchedule){
        scheduleList.add(newSchedule);
    }

    public void setScheduleList(ArrayList<Schedule> scheduleList){
        this.scheduleList = scheduleList;
    }

    public int  removeSchedule(int scheduleId){
        for(int i = 0; i < scheduleList.size(); i++){
            if(scheduleList.get(i).getId()==scheduleId){
                scheduleList.remove(i);
                return 1;
            }
        }
        return 0;
    }

    public Schedule  getScheduleById(int scheduleId){
        for(int i = 0; i < scheduleList.size(); i++){
            if(scheduleList.get(i).getId()==scheduleId){
                scheduleList.remove(i);
                return scheduleList.get(i);
            }
        }
        return null;
    }

    public List<Schedule> getAllSchedules(){
        return this.scheduleList;
    }


    public void writeToDb(){
        String jsonSchedules = gson.toJson(scheduleList);
        tinydb.putString("jsonSchedules",jsonSchedules);
    }

    public void readFromDb(){
        String jsonSchedules = tinydb.getString("jsonSchedules");
        List<Schedule> schedules;
        Schedule[] entries = gson.fromJson(jsonSchedules, Schedule[].class);
        if(entries!=null) {
            schedules = Arrays.asList(entries);
            schedules = new ArrayList<Schedule>(schedules);
            scheduleList = new ArrayList<Schedule>(schedules);
        }
        else{
            scheduleList = null;
        }
    }


}
