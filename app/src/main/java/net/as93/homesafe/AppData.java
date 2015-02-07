package net.as93.homesafe;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Alicia on 10/11/2014.
 */
public class AppData {

    ArrayList<Schedule> scheduleList;
    ArrayList<String> scheduleStrings;
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
//        ArrayList<Schedule> results = new ArrayList<Schedule>();
//        List<Schedule> serialSchedules = gson.fromJson(jsonSchedules, ArrayList.class);
//
//
//        for(int i = 0 ; i < serialSchedules.size() ; i++){
//            Schedule newSchedule = new Schedule();
//            int k = 7;
////            newSchedule.setId(serialSchedules.get(i).get("id"));
////
////            Schedule item = (Schedule) serialSchedules.get(i);
////            results.add(item);
//        }
////        this.scheduleList = results;



        List<Schedule> schedules;

        Schedule[] favoriteItems = gson.fromJson(jsonSchedules, Schedule[].class);

        schedules = Arrays.asList(favoriteItems);
        schedules = new ArrayList<Schedule>(schedules);

        scheduleList= new ArrayList<Schedule>(schedules);





    }


}
