package com.example.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.connect.Adapter_Device;
import com.example.bluetooth.connect.Data_Device;
import com.example.bluetooth.connect.OnButtonClickListener;
import com.example.bluetooth.connect.OnItemClickListener;

import java.util.ArrayList;
import java.util.Set;

import lib.kingja.switchbutton.SwitchMultiButton;

public class ConnectActivity extends AppCompatActivity implements OnButtonClickListener{
    private final static int REQUEST_ENABLE_BT = 1;  //블루투스 활성화
    static int numPaired;
    Set<BluetoothDevice> pairedDevices;
    ArrayList<Data_Device> dataList;
    SwitchMultiButton mSwitchMultiButton;
    RecyclerView recyclerView;
    Adapter_Device adapter_device;
    BluetoothAdapter btAdapter;
    ConnectThread connectThread;
    int[] bt_img = {R.drawable.ic_paired,R.drawable.ic_search};
    String[] stringEx ={"등록된 블루투스","연결 가능한 블루투스"};
    public static Context context;
    Button btSearch;

    FragmentEnable fragmentEnable;
    FragmentDisable fragmentDisable;

    ConnectHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        context = this;
        dataList = new ArrayList<>();
        adapter_device = new Adapter_Device(dataList);

        fragmentDisable = new FragmentDisable();
        fragmentEnable = new FragmentEnable(adapter_device, btSearch);

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main,fragmentEnable).commit();

        String[] permission_list = { //위치 권한 허용
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(ConnectActivity.this, permission_list,  1); //권한 요청

        btAdapter = BluetoothAdapter.getDefaultAdapter();//해당 장치가 블루투스 기능을 지원하는지 알아 오는 메서드

        if (!btAdapter.isEnabled()) { //블루투스가 활성화 되어 있지 않을 때
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //안드로이드에게 블루투스 활성화 요청
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); // 사용자 활성화 여부 묻고 그 결과에 따라 활성화/비활성화 결정
        }

        dataList.add(new Data_Device(stringEx[0],null,0, 1));

        //페어링 된 기기 리스트에 추가
        pairedDevices = btAdapter.getBondedDevices(); // 페어링 된 기기 집합 쿼리
        if(pairedDevices.size() > 0){
            numPaired = pairedDevices.size(); //설명1 + 검색된 기기
            for(BluetoothDevice device : pairedDevices){
                dataList.add(new Data_Device(device.getName(),device.getAddress(),bt_img[0], 0));
            }
        }

        dataList.add(new Data_Device(stringEx[1],null,0, 1));
        searchBT();
        handler = new ConnectHandler(context);

        adapter_device.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                if(pos !=0 && pos != numPaired+1){
                    btAdapter.cancelDiscovery(); //검색 중지(검색이 진행 중인 경우 연결시도가 현저히 느려짐)

                    final BluetoothDevice device = btAdapter.getRemoteDevice(dataList.get(pos).getPath()); //접속하고자 하는 원격 장치의 하드웨어 주소 지정
                    connectThread = new ConnectThread(device,handler);
                    connectThread.start();
                }
            }
        });

        mSwitchMultiButton = findViewById(R.id.switchButton);
        mSwitchMultiButton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {

                if(btAdapter == null) Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기 입니다.",Toast.LENGTH_SHORT).show();
                else{
                    if(position == 0){
                        if(!btAdapter.isEnabled()){
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //안드로이드에게 블루투스 활성화 요청
                            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT); // 사용자 활성화 여부 묻고 그 결과에 따라 활성화/비활성화 결정
                            getSupportFragmentManager().beginTransaction().addToBackStack(null).commit(); //다시 원래 화면으로 돌아옴
                        }
                    }
                    else if(position == 1){
                        if(btAdapter.isEnabled()){ //활성화 X
                            btAdapter.disable(); //연결 끊기
                            getSupportFragmentManager().beginTransaction().add(R.id.container_main,fragmentDisable).commit();//화면 전환 필요

                        }
                    }

                }
            }
        });
    }

    public void searchBT(){
        if(btAdapter.isDiscovering()){ // 블루투스 검색 중인지 확인
            btAdapter.cancelDiscovery(); // 검색 중단
        }
        else{
            if(btAdapter.isEnabled()){ //블루투스 활성화?
                btAdapter.startDiscovery(); //기기 검색 시작
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);
            }
            else{
                Toast.makeText(getApplicationContext(),"bluetooth not on",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action  = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); //추가되는 디바이스 묶음(검색된 디바이스) 얻기
//                System.out.println(device.getName());  //확인해 보면 null값 들어옴
                if(device.getName() !=null){ //null 값 제거
                    dataList.add(new Data_Device(device.getName(),device.getAddress(),bt_img[1], 0));
                    adapter_device.notifyDataSetChanged(); //어뎁터에 변경 사항 알림
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onButtonClick() {
        int size = dataList.size();
        for(int i= size-1; i>= numPaired+2; i--){

            System.out.println(i+"" +dataList.get(i).getName());
            dataList.remove(i);
            adapter_device.notifyDataSetChanged();
        }
        searchBT();
    }

}

