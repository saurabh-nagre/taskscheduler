package com.master.taskscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;


public class Task_Insertion extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button mstarttimeButton,mstartdateButton,menddateButton,mendtimeButton,msubmitButton;
    EditText mtasknameET,mtaskdescET;
    DBHelper dbHelper;
    String startdate ="";
    String starttime = "";
    String enddate = "";
    String endtime = "";
    TimeOperations timeOperations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_insertion);
        msubmitButton = findViewById(R.id.submitButton);
        mtasknameET = findViewById(R.id.addTaskEditText);
        mtaskdescET = findViewById(R.id.addDescriptionEditText);
        mstartdateButton = findViewById(R.id.startdateButton);
        mstarttimeButton = findViewById(R.id.starttimeButton);
        menddateButton = findViewById(R.id.enddateButton);
        mendtimeButton = findViewById(R.id.endtimeButton);
        dbHelper = new DBHelper(this);
        timeOperations = new TimeOperations();

        mstartdateButton.setOnClickListener(v->{
            mstartdateButton.setActivated(true);
            menddateButton.setActivated(false);
            mstartdateButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            DialogFragment datepicker = new DatePickerFragment();
            datepicker.show(getSupportFragmentManager(),"Date Picker");
        });

        mstarttimeButton.setOnClickListener(v->{
            mstarttimeButton.setActivated(true);
            mendtimeButton.setActivated(false);
            mstarttimeButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            DialogFragment timepicker= new TimePickerFragment();
            timepicker.show(getSupportFragmentManager(),"TimePicker");
        });

        menddateButton.setOnClickListener(v->{
            menddateButton.setActivated(true);
            mstartdateButton.setActivated(false);
            menddateButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            DialogFragment datepicker = new DatePickerFragment();
            datepicker.show(getSupportFragmentManager(),"Date Picker");
        });

        mendtimeButton.setOnClickListener(v->{
            mendtimeButton.setActivated(true);
            mstarttimeButton.setActivated(false);
            mendtimeButton.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            DialogFragment timepicker= new TimePickerFragment();
            timepicker.show(getSupportFragmentManager(),"TimePicker");
        });

        mtasknameET.setOnClickListener(v->{
            mtasknameET.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);} );

        mtaskdescET.setOnClickListener(v -> {
            mtaskdescET.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        });

        msubmitButton.setOnClickListener(this::submitTask);
    }



    void submitTask(View v){

        if(mtasknameET.getText().toString().equals("")){
            mtasknameET.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
        }
        else if(mtaskdescET.getText().toString().equals("")){
            mtaskdescET.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_error,0);
        }
        else if(startdate.equals("") || enddate.equals("") || starttime.equals("") || endtime.equals("")){
            Snackbar.make(v,"Select Date & Time",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
        }
        else if(!timeOperations.compareDate(startdate+starttime,enddate+endtime)){
            Snackbar.make(v,"Choose End Date & Time Ahead of Start Date & Time",Snackbar.LENGTH_SHORT).setAction("Action",null).show();
        }
        else{
            dbHelper.insert(mtasknameET.getText().toString(),mtaskdescET.getText().toString(),startdate+starttime,enddate+endtime);
            Toast.makeText(this,"Task Insertion Successful",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finishAffinity();
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        StringBuilder builder = new StringBuilder();
        builder.append(year);
        builder.append(",");
        if(month<=9){
            builder.append("0");
        }
        builder.append(month+1);
        builder.append(",");
        if(dayOfMonth<=9){
            builder.append("0");
        }
        builder.append(dayOfMonth);
        builder.append(",");

        StringBuilder textbuilder = new StringBuilder();
        textbuilder.append(year);
        textbuilder.append("/");
        if(month+1<10){
            textbuilder.append("0");
        }
        textbuilder.append(month+1);
        textbuilder.append("/");
        if(dayOfMonth<10){
            textbuilder.append("0");
        }
        textbuilder.append(dayOfMonth);
        if(mstartdateButton.isActivated()){
            startdate = builder.toString();
            mstartdateButton.setText(textbuilder.toString());
            mstarttimeButton.setVisibility(View.VISIBLE);
        }
        else{
            enddate  = builder.toString();
            menddateButton.setText(textbuilder.toString());
            mendtimeButton.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        StringBuilder builder = new StringBuilder();
        if(hourOfDay<=9)
            builder.append("0");
        builder.append(hourOfDay);
        builder.append(":");
        if(minute<=9)
            builder.append("0");
        builder.append(minute);
        builder.append(":00");

        StringBuilder builder1 = new StringBuilder();
        if(hourOfDay<=9){
            builder1.append("0");
        }
        builder1.append(hourOfDay);
        builder1.append(",");
        if(minute<=9)
            builder1.append("0");
        builder1.append(minute);

        if(mstarttimeButton.isActivated()){
            starttime = builder1.toString();
            mstarttimeButton.setText(builder.toString());
            menddateButton.setVisibility(View.VISIBLE);
        }
        else
        {
            mendtimeButton.setText(builder.toString());
            endtime = builder1.toString();
        }

    }


}