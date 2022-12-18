package com.example.bluetooth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.connect.Adapter_Device;
import com.example.bluetooth.connect.OnButtonClickListener;
import com.example.bluetooth.connect.OnItemClickListener;

public class FragmentEnable extends Fragment {
    RecyclerView recyclerView;
    Adapter_Device adapter_device;
    Button btSearch;
    private OnButtonClickListener listener = null;

    public FragmentEnable(Adapter_Device adapter_device,  Button btSearch){
        this.adapter_device = adapter_device;
        this.btSearch = btSearch;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_enable,container,false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = rootView.findViewById(R.id.recycler_bt);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerViewDecoration(60));
        recyclerView.setAdapter(adapter_device);

        btSearch = rootView.findViewById(R.id.bt_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onButtonClick();
                }
            }
        });
        return rootView;
    }
}
