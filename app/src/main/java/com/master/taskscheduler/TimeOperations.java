package com.master.taskscheduler;


import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeOperations {

    SimpleDateFormat sdf;
    boolean compareDate(String startdatetime,String enddatetime){

        String[] starttime = startdatetime.split(",");
        String[] endtime = enddatetime.split(",");

        int hour_diff = 0;
        int days_diff = 0;
        int month_diff = 0;
        int year_diff = 0;


        int minutes_diff = Integer.parseInt(starttime[4])-Integer.parseInt(endtime[4]);

        if(minutes_diff<0){
            hour_diff--;
        }

        hour_diff+=Integer.parseInt(starttime[3])-Integer.parseInt(endtime[3]);
        if(hour_diff<0){
            days_diff--;
        }

        days_diff+=Integer.parseInt(starttime[2])-Integer.parseInt(endtime[2]);
        if(days_diff<0){
            month_diff--;
        }

        month_diff+=Integer.parseInt(starttime[1])-Integer.parseInt(endtime[1]);
        if(month_diff<0){
            year_diff--;
        }

        year_diff+=Integer.parseInt(starttime[0])-Integer.parseInt(endtime[0]);

        return year_diff < 0;
    }

    int checkStateofTime(String startDateTime,String endDateTime){

        sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());
        int state =0;


        if(!compareDate(currentDateTime,startDateTime))
            state++;

        if(!compareDate(currentDateTime,endDateTime)){
            state++;
        }

        return state;
    }

}
