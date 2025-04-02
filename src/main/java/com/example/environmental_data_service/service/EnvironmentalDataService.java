package com.example.environmental_data_service.service;

import com.example.environmental_data_service.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnvironmentalDataService {

    private final RestTemplate restTemplate;

    @Value("${openweather.apikey}")
    private String openWeatherApiKey;

    public List<AirQualityData> getAirQualityData(List<Waypoint> waypoints) {
        return waypoints.stream()
                .map(this::fetchAirQualityForWaypoint)
                .collect(Collectors.toList());
    }

    private AirQualityData fetchAirQualityForWaypoint(Waypoint waypoint) {
        String url = String.format(
                "http://api.openweathermap.org/data/2.5/air_pollution?lat=%f&lon=%f&appid=%s",
                waypoint.getLatitude(), waypoint.getLongitude(), openWeatherApiKey
        );

        ResponseEntity<OpenWeatherResponse> response = restTemplate.getForEntity(url, OpenWeatherResponse.class);
        return mapToAirQualityIndex(response.getBody(), waypoint);
    }

    private AirQualityData mapToAirQualityIndex(OpenWeatherResponse response, Waypoint waypoint) {
        AirQualityData index = new AirQualityData();

        // Set location coordinates from the original waypoint
        index.setLatitude(waypoint.getLatitude());
        index.setLongitude(waypoint.getLongitude());

        if (response != null && response.getList() != null && !response.getList().isEmpty()) {
            OpenWeatherResponse.AirData airData = response.getList().get(0);

            if (airData.getMain() != null) {
                index.setAqi(airData.getMain().getAqi());
            }

            if (airData.getComponents() != null) {
                OpenWeatherResponse.Components components = airData.getComponents();
                index.setCo(components.getCo());
                index.setNo(components.getNo());
                index.setNo2(components.getNo2());
                index.setO3(components.getO3());
                index.setSo2(components.getSo2());
                index.setPm2_5(components.getPm2_5());
                index.setPm10(components.getPm10());
                index.setNh3(components.getNh3());
            }
        }
        return index;
    }
}