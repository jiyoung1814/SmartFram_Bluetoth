package com.example.bluetooth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.nex3z.togglebuttongroup.ToggleButtonGroup;
import com.nex3z.togglebuttongroup.button.CircularToggle;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FragmentControl extends Fragment{
    ToggleButton bt_control;
    TextView text_light, text_water;
    TimePicker timePicker;
    Date date;
    Context context;
    private OnCheckedChangeListener listener_toggle =null;
    private View.OnClickListener listener_text = null;
    int hour_current, min_current;
    static boolean isClicked_light = true;
    int hour, minute;
    String time;

    public FragmentControl(OnCheckedChangeListener listener_toggle, View.OnClickListener listener_text, Context context){
        this.listener_toggle = listener_toggle;
        this.listener_text = listener_text;
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_control,container,false);
        bt_control = rootView.findViewById(R.id.bt_control);
        text_light = rootView.findViewById(R.id.text_light);
        text_water = rootView.findViewById(R.id.text_water);
        timePicker = rootView.findViewById(R.id.timePicker);

        showWhichControl(isClicked_light);

//        calendar = Calendar.getInstance();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            hour_s = timePicker.getHour();
//            min_s = timePicker.getMinute();
//        }

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int h, int m) {
                hour = h;
                minute = m;
            }
        });

        bt_control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                long systemTime = System.currentTimeMillis();
                SimpleDateFormat formatter = new SimpleDateFormat("HH", Locale.KOREA);
                hour_current = Integer.parseInt(formatter.format(systemTime));
                formatter = new SimpleDateFormat("mm", Locale.KOREA);
                min_current = Integer.parseInt(formatter.format(systemTime));

                if(isClicked_light){ //빛제어 선택
                    if(hour>=hour_current){
                        if(minute>min_current){
                            time="";
                            if(hour<10){time +="0"+hour+":";}
                            else{time +=hour+":";}
                            if(minute<10){time +="0"+minute;}
                            else{time +=minute;}
                            time += ":00";
                            if(listener_toggle!=null){
                                listener_toggle.onCheckedChanged(compoundButton,b);
                            }
                        }
                        else{
                            showTimeAlertDialog();
                        }
                    }
                    else{
                        showTimeAlertDialog();
                    }
                }
                else{//물제어 선택택
                    if(listener_toggle!=null){
                        listener_toggle.onCheckedChanged(compoundButton,b);
                    }
                }
            }
        });

        text_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked_light = true;
                showWhichControl(isClicked_light);
                if(listener_text !=null){
                    listener_text.onClick(view);
                }

            }
        });

        text_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isClicked_light = false;
                showWhichControl(isClicked_light);
                if(listener_text !=null){
                    listener_text.onClick(view);
                }
            }
        });

        return rootView;
    }

    public void showTimeAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("시간설정 오류")
                .setMessage("시간 설정을 다시 해주세요")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showWhichControl(boolean isClicked_light){
        if(isClicked_light){
            text_light.setTextColor(getResources().getColor(R.color.white));
            text_light.setBackgroundResource(R.drawable.toggle_selected);
            text_water.setTextColor(getResources().getColor(R.color.ble_gray));
            text_water.setBackgroundResource(0);
        }
        else{
            text_water.setTextColor(getResources().getColor(R.color.white));
            text_water.setBackgroundResource(R.drawable.toggle_selected);
            text_light.setTextColor(getResources().getColor(R.color.ble_gray));
            text_light.setBackgroundResource(0);
        }
    }




}
