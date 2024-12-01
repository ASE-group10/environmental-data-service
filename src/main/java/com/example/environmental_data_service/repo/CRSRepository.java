package com.example.environmental_data_service.repo;

import com.example.environmental_data_service.entity.FeatureCollectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CRSRepository extends JpaRepository<FeatureCollectionEntity.CRSEntity, Long> {
}
