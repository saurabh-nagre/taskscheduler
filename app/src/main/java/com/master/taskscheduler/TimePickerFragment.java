package com.master.taskscheduler;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int hourofDay = c.get(Calendar.HOUR);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getActivity(),hourofDay,minute,
                DateFormat.is24HourFormat(getActivity()));
    }
}

