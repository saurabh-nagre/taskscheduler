package com.master.taskscheduler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    static final String DatabaseName = "TaskScheduler";
    static final int DatabaseVersion = 1;

    static final String workingTasks = "WorkingTasks";
    static final String arrivingTasks = "ArrivingTasks";
    static final String finishedTasks = "FinishedTasks";

    List<String> listnames = new ArrayList<>();
    List<String> listdesc = new ArrayList<>();
    List<String> liststartdatetime = new ArrayList<>();
    List<String> listenddatetime = new ArrayList<>();
    List<String> liststate = new ArrayList<>();
    public DBHelper(@Nullable Context context) {
        super(context, DatabaseName, null, DatabaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table If Not Exists " + workingTasks + "(TaskName Varchar2,TaskDesc Varchar2,TaskStartDateTime Varchar2(16),TaskEndDateTime Varchar2(16),State Varchar2(10))");
        db.execSQL("Create Table If Not Exists " + arrivingTasks + "(TaskName Varchar2,TaskDesc Varchar2,TaskStartDateTime Varchar2(16),TaskEndDateTime Varchar2(16),State Varchar2(10))");
        db.execSQL("Create Table If Not Exists " + finishedTasks + "(TaskName Varchar2,TaskDesc Varchar2,TaskStartDateTime Varchar2(16),TaskEndDateTime Varchar2(16),State Varchar2(10))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.needUpgrade(newVersion);
    }

    public void insert(String name, String description, String startdatetime, String enddatetime) {
        TimeOperations operations = new TimeOperations();
        SQLiteDatabase db  = this.getWritableDatabase();
        int state = operations.checkStateofTime(startdatetime,enddatetime);

        ContentValues contentValues = new ContentValues();
        contentValues.put("TaskName",name);
        contentValues.put("TaskDesc",description);
        contentValues.put("TaskStartDateTime",startdatetime);
        contentValues.put("TaskEndDateTime",enddatetime);

        if(state==0){
            contentValues.put("State","Arriving");
            db.insert(arrivingTasks,null,contentValues);

        }
        else if(state==1){
            contentValues.put("State","On Track");
            db.insert(workingTasks,null,contentValues);
        }
        else {
            contentValues.put("State", "Finished");
            db.insert(finishedTasks, null, contentValues);
        }
        db.close();
    }

    void getlist(int state){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor ;
        listnames.clear();
        listdesc.clear();
        liststartdatetime.clear();
        listenddatetime.clear();
        liststate.clear();
        if(state==0){
            cursor = db.rawQuery("Select * from "+arrivingTasks+" order by TaskStartDateTime",null);
        }
        else if(state==1)
            cursor = db.rawQuery("Select * from "+workingTasks+" order by TaskEndDateTime",null);
        else
            cursor = db.rawQuery("Select * from "+finishedTasks+" order by TaskEndDateTime",null);

        cursor.moveToFirst();
        int count = cursor.getCount();
        while (count>0){
            count--;
            listnames.add(cursor.getString(cursor.getColumnIndex("TaskName")));
            listdesc.add(cursor.getString(cursor.getColumnIndex("TaskDesc")));
            liststartdatetime.add(cursor.getString(cursor.getColumnIndex("TaskStartDateTime")));
            listenddatetime.add(cursor.getString(cursor.getColumnIndex("TaskEndDateTime")));
            liststate.add(cursor.getString(cursor.getColumnIndex("State")));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();
    }

    void deleterow(int state,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        if(state==0)
            db.delete(arrivingTasks,"TaskName=?",new String[]{name});
        else if(state==1){
            db.delete(workingTasks,"TaskName=?",new String[]{name});
        }
        else
            db.delete(finishedTasks,"TaskName=?",new String[]{name});

        db.close();
    }
    List<String> getListnames(){
        return listnames;
    }
    List<String> getListdesc(){
        return listdesc;
    }
    List<String> getListstartdatetime(){
        return liststartdatetime;
    }
    List<String> getListenddatetime(){
        return listenddatetime;
    }
    List<String> getListstate(){
        return liststate;
    }

    void setStartTimetoNow(String name){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm",Locale.getDefault());
        String currentdatetime  = sdf.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        String desc="",enddate="";

        Cursor cursor = db.rawQuery("Select * from "+arrivingTasks+" where TaskName = '"+name+"' order by TaskStartDateTime",null);
        cursor.moveToFirst();

        desc = cursor.getString(cursor.getColumnIndex("TaskDesc"));
        enddate = cursor.getString(cursor.getColumnIndex("TaskEndDateTime"));
        ContentValues contentValues = new ContentValues();

        contentValues.put("TaskName",name);
        contentValues.put("TaskDesc",desc);
        contentValues.put("TaskStartDateTime",currentdatetime);
        contentValues.put("TaskEndDateTime",enddate);
        contentValues.put("State","On Track");

        db.delete(arrivingTasks,"TaskName = ?",new String[]{name});

        db.insert(workingTasks,null,contentValues);
        cursor.close();
        db.close();
    }
    void setEndTimetoNow(String name){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm",Locale.getDefault());
        String currentdatetime  = sdf.format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        String desc="",enddate="";
        Cursor cursor = db.rawQuery("Select * from "+workingTasks+" where TaskName = '"+name+"' order by TaskStartDateTime",null);
        cursor.moveToFirst();

        desc = cursor.getString(cursor.getColumnIndex("TaskDesc"));
        enddate = cursor.getString(cursor.getColumnIndex("TaskStartDateTime"));

        ContentValues contentValues = new ContentValues();

        contentValues.put("TaskName",name);
        contentValues.put("TaskDesc",desc);
        contentValues.put("TaskStartDateTime",currentdatetime);
        contentValues.put("TaskEndDateTime",enddate);
        contentValues.put("State","Finished");
        db.delete(workingTasks,"TaskName = ?",new String[]{name});
        db.insert(finishedTasks,null,contentValues);
        cursor.close();
        db.close();
    }

}

