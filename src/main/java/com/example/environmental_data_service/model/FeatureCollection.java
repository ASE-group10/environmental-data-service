package com.example.environmental_data_service.model;

import lombok.Data;
import java.util.List;

@Data
public class FeatureCollection {
    private String type;
    private String name;
    private CRS crs;  // Added CRS object to match the "crs" section in the JSON
    private List<Feature> features;

    @Data
    public static class CRS {
        private String type;
        private Properties properties;

        @Data
        public static class Properties {
            private String name;
        }
    }
}
