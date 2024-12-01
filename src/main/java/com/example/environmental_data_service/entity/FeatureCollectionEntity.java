package com.example.environmental_data_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feature_collections")
@Data
public class FeatureCollectionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "crs_id")
    private CRSEntity crs;

    @OneToMany(mappedBy = "featureCollection", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeatureEntity> features = new ArrayList<>();

    @Entity
    @Table(name = "crs")
    @Data
    public static class CRSEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String type;

        @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "properties_id")
        private PropertiesEntity properties;

        @Entity
        @Table(name = "crs_properties")
        @Data
        public static class PropertiesEntity {
            @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

            private String name;
        }
    }
}
