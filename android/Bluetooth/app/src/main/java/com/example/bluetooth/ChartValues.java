package com.example.bluetooth;

import android.graphics.Color;

public class ChartValues {
    String name;
    int value;
    int color;

    public ChartValues(String name, int value, int color) {
        this.name = name;
        this.value = value;
        this.color = color;
    }

    public ChartValues(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
}
