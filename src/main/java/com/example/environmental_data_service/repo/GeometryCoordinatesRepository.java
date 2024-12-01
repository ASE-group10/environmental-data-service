package com.example.environmental_data_service.repo;


import com.example.environmental_data_service.model.GeometryCoordinates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeometryCoordinatesRepository extends JpaRepository<GeometryCoordinates, Long> {
}
