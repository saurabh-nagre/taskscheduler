package com.master.taskscheduler;

import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class Holder extends RecyclerView.ViewHolder {

    ImageButton mmore;
    TextView mname;
    TextView mdesc;
    Chronometer mchronometer;
    TextView mstate;
    public Holder(View view){
        super(view);

        this.mname = view.findViewById(R.id.listNameTextView);
        this.mdesc = view.findViewById(R.id.descriptionTextView);
        this.mchronometer = view.findViewById(R.id.timer);
        this.mstate = view.findViewById(R.id.stateTextView);
        this.mmore = view.findViewById(R.id.moreverticalButton);
    }
}