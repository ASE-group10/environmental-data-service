package com.example.environmental_data_service.model;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaypointTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        Waypoint waypoint = new Waypoint();
        waypoint.setLatitude(40.7128);
        waypoint.setLongitude(-74.0060);

        assertEquals(40.7128, waypoint.getLatitude());
        assertEquals(-74.0060, waypoint.getLongitude());
    }

    @Test
    void testAllArgsConstructor() {
        Waypoint waypoint = new Waypoint(37.7749, -122.4194);
        assertEquals(37.7749, waypoint.getLatitude());
        assertEquals(-122.4194, waypoint.getLongitude());
    }

    @Test
    void testEqualsAndHashCode() {
        Waypoint wp1 = new Waypoint(10.0, 20.0);
        Waypoint wp2 = new Waypoint(10.0, 20.0);
        Waypoint wp3 = new Waypoint(99.9, 99.9);

        assertEquals(wp1, wp2);
        assertEquals(wp2.hashCode(), wp1.hashCode());
        assertNotEquals(wp1, wp3);
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        Waypoint wp = new Waypoint(1, 1);
        assertNotEquals(null, wp);
        assertNotEquals(wp, new Object());
    }

    @Test
    void testToStringNotNull() {
        Waypoint wp = new Waypoint(1, 1);
        String stringRep = wp.toString();
        assertNotNull(stringRep);
        assertTrue(stringRep.contains("latitude=1.0"));
    }

    @Data
    static class SubWaypoint extends Waypoint {
        public SubWaypoint(double latitude, double longitude) {
            super(latitude, longitude);
        }

        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void testEqualsFailsWhenSubclassOverridesCanEqual() {
        Waypoint base = new Waypoint(5.0, 5.0);
        SubWaypoint sub = new SubWaypoint(5.0, 5.0);

        assertNotEquals(base, sub);
        assertNotEquals(sub, base);
    }
}
