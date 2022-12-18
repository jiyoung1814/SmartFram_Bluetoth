package com.example.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

public class ConnectThread extends Thread{
    private final BluetoothSocket socket;
    private final BluetoothDevice device;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  //클라이언트와 기기와의 연결 동의를 판단하는 기준으로 이것이 일치해야 연결 가능
    ManageConnectedSocket manageConnectSocket;
    ConnectHandler handler;

    public ConnectThread(BluetoothDevice device,  ConnectHandler handler){
        this.device = device;
        this.handler = handler;
        BluetoothSocket temp = null;

        try {
            temp = device.createRfcommSocketToServiceRecord(BT_UUID);//블루투스 소켓을 가지고옴(UUID가 서버와 일치해야 함)
        }catch (IOException e){
            System.out.println("블루투스 연결 오류 : "+e.getMessage());
       }
        socket = temp;
    }


    public void run(){
        try {
            socket.connect(); //원격 기기가 연결 수락 X, 연결이 안되거나 연결 시간 초과 시, 차단 호출이므로 스레드로 실행

//            handler.setSocket(manageConnectSocket);
        }catch (IOException e) {
            System.out.println("블루투스 소켓 연결 오류 : " + e.getMessage());
            try {
                socket.close();//소켓 닫아줌
            } catch (IOException e1) {
                System.out.println("블루투스 소켓 close 오류 : " + e1.getMessage());
            }
            return;
        }
        manageConnectSocket = new ManageConnectedSocket(socket);
        manageConnectSocket.start(); //연결
        Message message = handler.obtainMessage();//UI변경
        handler.sendMessage(message);


    }


    public void cancel(){
        try {
            socket.close();//소켓 닫아줌
        } catch (IOException e1) {
            System.out.println("블루투스 소켓 close 오류 : " + e1.getMessage());
        }

    }
}
