package com.example.hw9;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Search extends Fragment {

    public Search() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search, container, false);
        Button toastBtn = view.findViewById(R.id.search);
        toastBtn.setOnClickListener(v -> {
            Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}