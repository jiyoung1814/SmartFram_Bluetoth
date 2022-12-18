package com.example.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.logging.Handler;

public class ManageConnectedSocket extends Thread{
    private static final long serialVersionUID =1L;
    private Handler handler;
    private final BluetoothSocket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    boolean sendData=  false;
    String data="";

    public ManageConnectedSocket(BluetoothSocket socket){
        this.socket = socket;

        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try{
            tmpIn = socket.getInputStream();
        }catch (IOException e){ System.out.println("Error occurred when creating input stream: "+e.getMessage());}
        try{
            tmpOut = socket.getOutputStream();
        }catch (IOException e){System.out.println("Error occurred when creating output stream: "+e.getMessage());}

        inputStream = tmpIn;
        outputStream = tmpOut;
    }

    public void run(){
//        write("on");
        int bytes;
        while(true){
            try {
                bytes = inputStream.available();
                if(bytes !=0){
                    byte[] buffer = new byte[bytes]; //스트림 버퍼 저장소
                    inputStream.read(buffer);
                    for(int i=0;i<bytes;i++){
                        if(buffer[i]==-128) {
                            sendData = true;
                        }
                        else{
                            data += buffer[i]+" ";
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("inputSystem 에러"+e.getMessage());
            }
//            message = bundle.getString("control");
//            System.out.println(message+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
//            if(!message.equals("")){
//                write(message);
//            }

//            try {
//                bytes = inputStream.available();
//            } catch (IOException e) { System.out.println("입력 스트림으로 읽을 수 있는 바이트 반환 실패: "+e.getMessage());}
//            if(bytes!=0){
//
//            }
        }
    }
    public void write(String str){
        byte[] bytes = str.getBytes();
        try{
            outputStream.write(bytes);
        }catch (IOException e){
            System.out.println("데이터 전송 에러"+e.getMessage());
        }
    }

    public void cancel() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
}
