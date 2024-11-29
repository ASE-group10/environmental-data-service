package com.example.environmental_data_service.service;

import com.example.environmental_data_service.model.FeatureCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;

@Service
public class EnvironmentalDataService {

    private static final String GEOJSON_URL = "https://data.smartdublin.ie/dataset/4976e11e-a015-4ef9-9179-dc7c27fb5a81/resource/ec7f3108-c12b-4e07-b482-470f28f52aca/download/airview_dublincity_roaddata_ugm3.geojson";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void GeoJsonService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public FeatureCollection fetchGeoJsonData() throws IOException {
        // Fetch the GeoJSON data
        String geoJsonResponse = restTemplate.getForObject(GEOJSON_URL, String.class);

        // Convert the JSON response to FeatureCollection object
        return objectMapper.readValue(geoJsonResponse, FeatureCollection.class);
    }
}
