package com.master.taskscheduler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<Holder> implements Filterable {


    DBHelper dbHelper;
    Context context;
    List<String> taskNames;
    List<String> taskDesc;
    List<String> taskstartTime,taskendTime;
    List<String> taskstate;
    int state;
    @Override
    public Filter getFilter() {
        return null;
    }

    public CustomAdapter(Context context,int state){
        dbHelper = new DBHelper(context);
        this.context = context;
        this.state = state;
        reload();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cardview, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, @SuppressLint("RecyclerView") int position) {
        holder.mname.setText(taskNames.get(position));
        holder.mdesc.setText(taskDesc.get(position));
        holder.mstate.setText(taskstate.get(position));
        gettimer(holder, position);

        holder.mmore.setOnClickListener(v->popupmenu(v,position));

    }

    void popupmenu(View v,int position){
        PopupMenu popup = new PopupMenu(context,v);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.cardview_menu, popup.getMenu());
        Menu menu = popup.getMenu();

        if(state==1){
            menu.removeItem(R.id.startnow);
        }
        else if(state==2){
            menu.removeItem(R.id.startnow);
            menu.removeItem(R.id.completeButton);
        }

        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){

                    case R.id.startnow:
                        dbHelper.setStartTimetoNow(taskNames.get(position));
                        remove(position);
                        break;
                    case R.id.completeButton:
                        dbHelper.setEndTimetoNow(taskNames.get(position));
                        remove(position);
                        break;
                    case R.id.cancelButton:
                        dbHelper.deleterow(state,taskNames.get(position));
                        remove(position);
                        break;
                    default:
                        Toast.makeText(context,"Something Went Wrong",Toast.LENGTH_SHORT).show();

                }
                return false;
            }
        });
    }

    void gettimer(Holder holder,int position){

        String  str_before_split;
        if(state==0)
            str_before_split =taskstartTime.get(position);
        else{
            str_before_split = taskendTime.get(position);
        }

        String[] settime =str_before_split.split(",");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String[] currenttime = currentDateandTime.split(",");


        int hour_diff = 0;
        int days_diff = 0;
        int month_diff = 0;
        int year_diff = 0;
        int sec_diff = Integer.parseInt(currenttime[5]);

        int minutes_diff = Integer.parseInt(settime[4])-Integer.parseInt(currenttime[4]);

        if(minutes_diff<0){
            minutes_diff+=60;
            hour_diff--;
        }

        hour_diff+=Integer.parseInt(settime[3])-Integer.parseInt(currenttime[3]);
        if(hour_diff<0){
            hour_diff+=24;
            days_diff--;
        }

        days_diff+=Integer.parseInt(settime[2])-Integer.parseInt(currenttime[2]);
        if(days_diff<0){
            days_diff+=Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            month_diff--;
        }

        month_diff+=Integer.parseInt(settime[1])-Integer.parseInt(currenttime[1]);
        if(month_diff<0){
            month_diff+=12;
            year_diff--;
        }

        year_diff+=Integer.parseInt(settime[0])-Integer.parseInt(currenttime[0]);

        if(year_diff<0){
            holder.mchronometer.setFormat("Finished%s");
        }
        else {
            holder.mchronometer.setCountDown(true);
            if(year_diff==0 && month_diff==0 && days_diff==0)
                holder.mchronometer.setFormat("%s");
            else if(year_diff==0 && month_diff==0){
                holder.mchronometer.setFormat(days_diff+"Days "+"%s");
            }
            else if(year_diff==0)
                holder.mchronometer.setFormat(month_diff+"Months "+ days_diff+"Days "+"%s");
            else
                holder.mchronometer.setFormat(year_diff+"Year "+month_diff+"Months "+days_diff+"Days "+"%s");

            long hours = hour_diff*60*60*1000L;
            long minutes = minutes_diff*60*1000L;

            holder.mchronometer.setBase(SystemClock.elapsedRealtime() + hours + minutes + sec_diff*1000L);
            holder.mchronometer.start();
        }

        holder.mchronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                String str = chronometer.getText().toString();

                if(str.equals("00:00") && state == 0){
                    dbHelper.setStartTimetoNow(taskNames.get(position));
                    remove(position);
                }
                else if(str.equals("00:00") && state==1){
                    dbHelper.setEndTimetoNow(taskNames.get(position));
                    remove(position);
                }
            }
        });
    }
    void remove(int position){

        taskNames.remove(position);
        taskDesc.remove(position);
        taskstartTime.remove(position);
        taskendTime.remove(position);
        taskstate.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, 1);
    }
    void reload(){

        dbHelper.getlist(state);
        taskNames = dbHelper.getListnames();
        taskDesc = dbHelper.getListdesc();
        taskstartTime = dbHelper.getListstartdatetime();
        taskendTime = dbHelper.getListenddatetime();
        taskstate = dbHelper.getListstate();
    }
    @Override
    public int getItemCount() {
        return taskNames.size();
    }

}



