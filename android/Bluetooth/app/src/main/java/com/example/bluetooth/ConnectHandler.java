package com.example.bluetooth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ConnectHandler extends Handler {
    Context context;


    public ConnectHandler( Context context) {
        this.context = context;
    }


    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);


        Intent intent = new Intent(context,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("socket", manageConnectSocket);
        context.startActivity(intent);
//        context.finish();
    }

}
