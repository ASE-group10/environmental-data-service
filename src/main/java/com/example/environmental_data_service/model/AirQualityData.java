package com.example.environmental_data_service.model;

import lombok.Data;

@Data
public class AirQualityData {
    private double latitude;
    private double longitude;
    private int aqi;
    private double co;
    private double no;
    private double no2;
    private double o3;
    private double so2;
    private double pm2_5;
    private double pm10;
    private double nh3;
}