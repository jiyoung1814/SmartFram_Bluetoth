package com.example.bluetooth.connect;

public class Data_Device {
    String name;
    String path;
    int img;
    int type;


    public Data_Device(String name, String path, int img, int type){
        this.name =name;
        this.path = path;
        this.img = img;
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
