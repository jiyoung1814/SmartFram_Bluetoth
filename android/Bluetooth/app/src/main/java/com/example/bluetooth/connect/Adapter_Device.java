package com.example.bluetooth.connect;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.R;

import java.util.ArrayList;

public class Adapter_Device extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<Data_Device> data;

    public Adapter_Device(ArrayList<Data_Device> data){
        this.data = data;
    }
    private OnItemClickListener listener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(viewType == 0){ //디바이스
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bt2, parent, false);
            return new ViewHolderDevice(view);
        }
        else if(viewType  == 1){ //설명
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bt, parent, false);
            return new ViewHolderEx(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderEx){
            ViewHolderEx viewHolderEx = (ViewHolderEx) holder;
            viewHolderEx.onBind(data.get(position));
        }
        else if(holder instanceof ViewHolderDevice){
            ViewHolderDevice viewHolderDevice = (ViewHolderDevice) holder;
            viewHolderDevice.onBind(data.get(position));
        }
    }


    public int getItemViewType(int position){
        return data.get(position).getType();
    }

    public void addItem(ArrayList<Data_Device> data){
        this.data.clear();
        this.data.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewHolderDevice extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        LinearLayout layout;
        Data_Device data;
        public ViewHolderDevice(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.textView_device);
            imageView = itemView.findViewById(R.id.imageView_device);
            layout = itemView.findViewById(R.id.layout_device);
        }

        public void onBind(Data_Device data){
            this.data = data;
            textView.setText(data.getName());
            imageView.setImageResource(data.getImg());

            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(listener != null){
                            listener.onItemClick(view,pos);
                        }
                    }
                }
            });
        }
    }

    public class ViewHolderEx extends RecyclerView.ViewHolder{
        TextView textView;
        Data_Device data;

        public ViewHolderEx(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView_explain);
        }
        public void onBind(Data_Device data){
            this.data = data;
            textView.setText(data.getName());
        }
    }

}
