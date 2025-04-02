package com.example.environmental_data_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Waypoint {
    private double latitude;
    private double longitude;
}
