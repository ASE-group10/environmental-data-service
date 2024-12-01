package com.example.environmental_data_service.repo;

import com.example.environmental_data_service.entity.FeatureCollectionEntity;
import com.example.environmental_data_service.entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureCollectionRepository extends JpaRepository<FeatureCollectionEntity, Long> {
}

