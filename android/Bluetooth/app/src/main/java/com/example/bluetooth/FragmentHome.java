package com.example.bluetooth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bluetooth.connect.OnButtonClickListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;

import im.dacer.androidcharts.LineView;

public class FragmentHome extends Fragment {
    PieChart chart_temp;
    PieChart chart_humi;
    PieChart chart_soil;
    PieChart chart_illuminance;
    LineView lineView;
    Button button_reset;
    ArrayList<ArrayList<ChartValues>> chartData = new ArrayList<>();
    ArrayList<ChartValues> chartValues = new ArrayList<>();
    ArrayList<String> bottomTextList = new ArrayList<>();
    private OnButtonClickListener listener = null;

    final int graphNum = 6;
    int[] data= new int[graphNum];

    public  FragmentHome(OnButtonClickListener listener){
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home,container,false);
        chart_temp = (PieChart) rootView.findViewById(R.id.chart_temp);
        chart_humi = (PieChart) rootView.findViewById(R.id.chart_humi);
        chart_soil = (PieChart) rootView.findViewById(R.id.chart_soil);
        chart_illuminance = (PieChart) rootView.findViewById(R.id.chart_illuminance);



        button_reset = rootView.findViewById(R.id.button_reset);
        button_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onButtonClick();
                    Bundle bundle = getArguments();
                    System.out.println(bundle);

                    while(true){
                        if(bundle==null){
                            bundle = getArguments();
                        }

                        if(bundle != null){
                            String resetData =getArguments().getString("resetData");//이전 값 가지고 오지 않기 위해
                            System.out.println("Fragment: "+resetData);
                            for(int i=0;i<graphNum;i++){
                                String[] str = resetData.split(" ");
                                data[i] = Integer.parseInt(str[i]);
                            }
                            setPieChart(data);
                            break;

                        }
                    }


                }
            }
        });

        lineView = (LineView)rootView.findViewById(R.id.line_view);
        lineView.setDrawDotLine(true); //보조선
        lineView.setShowPopup(LineView.SHOW_POPUPS_All); //SHOW_POPUPS_All = 1 ->모든 데이터, SHOW_POPUPS_MAXMIN_ONLY = 2 ->최소 최대, SHOW_POPUPS_NONE = 3 -> 아무것도 보여주지 않음
        lineView.setColorArray(new int[]{Color.parseColor("#EE4F70"),Color.parseColor("#219DD9"),Color.parseColor("#D99014"),Color.parseColor("#FFCA1A")});
        for(int i=1;i<=24;i++){
            String num = Integer.toString(i);
            bottomTextList.add(num);
        }
        lineView.setBottomTextList(bottomTextList);
//        lineView.setDataList(dataLists);//데이터 채우기


        button_reset.performClick();

        return rootView;
    }

    public void setPieChart(int[] data){
        chart_temp.clearChart();
        chart_humi.clearChart();
        chart_soil.clearChart();
        chart_illuminance.clearChart();

        chart_temp.addPieSlice(new PieModel("Temperature", data[0], Color.parseColor("#EE4F70")));
//        chart_temp.addPieSlice(new PieModel("Temperature", 40-data[0], Color.parseColor("#FFFFFF")));//최대 40
        chart_humi.addPieSlice(new PieModel("Humidity", data[1], Color.parseColor("#219DD9")));
//        chart_humi.addPieSlice(new PieModel("Humidity", 80-data[1], Color.parseColor("#FFFFFF")));//최대 80
        chart_soil.addPieSlice(new PieModel("Soil Humidity", data[2], Color.parseColor("#D99014")));
//        chart_soil.addPieSlice(new PieModel("Soil Humidity", 100-data[2], Color.parseColor("#FFFFFF")));
        int illuminance =0;
        if(data[3]<0){
            illuminance = 127*(127*-(data[3])+data[4])+data[5];
        }
        else{
            illuminance = data[3]*127+data[4];
        }
        chart_illuminance.addPieSlice(new PieModel("illuminance", illuminance, Color.parseColor("#FFCA1A")));
//        chart_illuminance.addPieSlice(new PieModel("illuminance", 60, Color.parseColor("#FFFFFF")));

        chart_temp.startAnimation();
        chart_humi.startAnimation();
        chart_soil.startAnimation();
        chart_illuminance.startAnimation();
    }
}
