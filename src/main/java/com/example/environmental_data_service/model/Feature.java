package com.example.environmental_data_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.List;

@Data
public class Feature {
    private String type;
    private Properties properties;
    private Geometry geometry;

    @Data
    public static class Properties {
        private String road_id;
        private String osm_id;
        private String osm_code;
        private String osm_fclass;
        private String osm_name;
        private String osm_ref;
        private String osm_oneway;
        private String osm_maxspe;
        private String osm_layer;
        private String osm_bridge;
        private String osm_tunnel;
        private Integer NO2points;
        private String NO2drives;
        private Double NO2_ugm3;
        private Integer NOpoints;
        private String NOdrives;
        private Double NO_ugm3;
        private Integer CO2points;
        private String CO2drives;
        private Double CO2_mgm3;
        private Integer COpoints;
        private String COdrives;
        private Double CO_mgm3;
        private Integer O3points;
        private String O3drives;
        private Double O3_ugm3;
        private Integer PM25points;
        private String PM25drives;
        private Double PM25_ugm3;
    }

    @Data
    public static class Geometry {
        private String type;
        private List<List<Double>> coordinates; // Adjusted to handle the nested list of coordinates

        @JsonSerialize(using = GeometrySerializer.class)
        public static class GeometrySerializer extends com.fasterxml.jackson.databind.JsonSerializer<Geometry> {
            @Override
            public void serialize(Geometry geometry, com.fasterxml.jackson.core.JsonGenerator jsonGenerator, com.fasterxml.jackson.databind.SerializerProvider serializerProvider) throws java.io.IOException {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("type", geometry.getType());
                jsonGenerator.writeArrayFieldStart("coordinates");
                for (List<Double> coordinatePair : geometry.getCoordinates()) {
                    jsonGenerator.writeStartArray();
                    for (Double coordinate : coordinatePair) {
                        jsonGenerator.writeNumber(coordinate);
                    }
                    jsonGenerator.writeEndArray();
                }
                jsonGenerator.writeEndArray();
                jsonGenerator.writeEndObject();
            }
        }
    }
}
