package com.master.taskscheduler.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.master.taskscheduler.CustomAdapter;
import com.master.taskscheduler.R;
import com.master.taskscheduler.databinding.FragmentArrivingBinding;


public class ArrivingFragment extends Fragment {

    FragmentArrivingBinding binding;
    RecyclerView recyclerView;
    CustomAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentArrivingBinding.inflate(inflater,container,false);
        View root = binding.getRoot();
        recyclerView = binding.arrivingRecyclerview;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CustomAdapter(getContext(),0);
        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}