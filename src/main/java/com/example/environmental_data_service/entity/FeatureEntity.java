package com.example.environmental_data_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "features")
@Data
public class FeatureEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @ManyToOne
    @JoinColumn(name = "feature_collection_id")
    private FeatureCollectionEntity featureCollection;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "properties_id")
    private PropertiesEntity properties;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "geometry_id")
    private GeometryEntity geometry;

    @Entity
    @Table(name = "feature_properties")
    @Data
    public static class PropertiesEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

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

    @Entity
    @Table(name = "feature_geometry")
    @Data
    public static class GeometryEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String type;

        @ElementCollection
        @CollectionTable(name = "geometry_coordinates", joinColumns = @JoinColumn(name = "geometry_id"))
        @Column(name = "coordinate")
        private List<List<Double>> coordinates;
    }
}
