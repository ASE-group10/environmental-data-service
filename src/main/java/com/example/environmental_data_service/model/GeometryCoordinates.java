package com.example.environmental_data_service.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "geometry_coordinates", schema = "public")
public class GeometryCoordinates {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "geometry_id")
    private Long geometryId;

    @Column(name = "coordinate")
    @ElementCollection
    private List<Double[]> coordinate;

    // Getters and Setters
    public Long getGeometryId() {
        return geometryId;
    }

    public void setGeometryId(Long geometryId) {
        this.geometryId = geometryId;
    }

    public List<Double[]> getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(List<Double[]> coordinate) {
        this.coordinate = coordinate;
    }
}
