package com.example.environmental_data_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {
    private Coord coord;
    private List<AirData> list;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AirData {
        private Main main;
        private Components components;
        private Long dt;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Main {
        private int aqi;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Components {
        private double co;
        private double no;
        private double no2;
        private double o3;
        private double so2;

        @JsonProperty("pm2_5")
        private double pm2_5;

        private double pm10;
        private double nh3;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Coord {
        private double lon;
        private double lat;
    }
}