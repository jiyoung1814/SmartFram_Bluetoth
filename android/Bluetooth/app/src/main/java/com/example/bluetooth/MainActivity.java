package com.example.bluetooth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.example.bluetooth.connect.OnButtonClickListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements OnCheckedChangeListener, OnButtonClickListener, View.OnClickListener {
    FragmentHome fragmentHome;
    FragmentCamera fragmentCamera;
    FragmentControl fragmentControl;
    BottomNavigationView bottomNavigationView;
    String choice="light";

    final int graphNum = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);


        fragmentHome = new FragmentHome(this);
        fragmentCamera = new FragmentCamera();
        fragmentControl = new FragmentControl(this,this, this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main,fragmentHome).commit();



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.camera_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_main,fragmentCamera).commit();
                        return true;
                    case R.id.home_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_main,fragmentHome).commit();
                        return true;
                    case R.id.control_tab:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_main,fragmentControl).commit();
                        return true;
                }

                return false;
            }
        });

    }

    @Override
    public void onBackPressed() { //이전 엑티비티로 넘어갈때 연결 끊기
        super.onBackPressed();

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.text_light:
                choice = "light";
                break;
            case R.id.text_water:
                choice = "water";
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(choice=="light"){
            if(b){
                ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.write("on "+ fragmentControl.time);
            }
            else{
                ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.write("off");
            }
        }

        else if(choice =="water"){

            if(b){
                ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.write("WaterOn");
            }
            else{
                ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.write("WaterOff");
            }
        }




    }

    @Override
    public void onButtonClick() {

        ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.write("reset");

        new Thread(new Runnable() {
            String resetData="";
            @Override
            public void run() {
                while (true){
                    if(((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.sendData){
                        resetData = ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.data;
                        System.out.println("Main: "+resetData);

                        Bundle bundle = new Bundle();
                        bundle.putString("resetData",resetData);
                        fragmentHome.setArguments(bundle);

                        ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.data = "";
                        ((ConnectActivity)ConnectActivity.context).connectThread.manageConnectSocket.sendData = false;

                        break;

                    }
                }

            }
        }).start();

    }

}