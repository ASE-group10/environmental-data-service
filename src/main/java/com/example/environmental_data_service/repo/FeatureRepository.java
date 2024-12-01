package com.example.environmental_data_service.repo;

import com.example.environmental_data_service.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<FeatureEntity, Long> {
}
