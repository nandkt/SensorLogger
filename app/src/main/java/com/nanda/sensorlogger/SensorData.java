package com.nanda.sensorlogger;

public class SensorData {

    private int id;
    private float x;
    private float y;
    private float z;
    private String status;

    public SensorData(int id, float x, float y, float z, String status) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public String getStatus() {
        return status;
    }
}