package com.example.environmental_data_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "crs")
public class CRSEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "properties_id")
    private PropertiesEntity properties;

    @Entity
    @Table(name = "crs_properties")  // This table should exist
    public static class PropertiesEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
    }
}
