package net.as93.homesafe.data;

import java.util.ArrayList;
import java.util.List;

public interface IAppData {

    public void addSchedule(Schedule newSchedule);

    public void setScheduleList(ArrayList<Schedule> scheduleList);

    public int  removeSchedule(int scheduleId);

    public Schedule  getScheduleById(int scheduleId);

    public List<Schedule> getAllSchedules();

    public void writeToDb();

    public void readFromDb();

}
