package com.example.environmental_data_service.model;

import lombok.Data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AirQualityDataTest {

    @Test
    void testAirQualityDataSettersAndGetters() {
        AirQualityData data = new AirQualityData();
        data.setLatitude(53.3498);
        data.setLongitude(-6.2603);
        data.setAqi(2);
        data.setCo(0.5);
        data.setNo(0.1);
        data.setNo2(0.2);
        data.setO3(0.3);
        data.setSo2(0.4);
        data.setPm2_5(0.6);
        data.setPm10(0.7);
        data.setNh3(0.8);

        assertEquals(53.3498, data.getLatitude());
        assertEquals(-6.2603, data.getLongitude());
        assertEquals(2, data.getAqi());
        assertEquals(0.5, data.getCo());
        assertEquals(0.1, data.getNo());
        assertEquals(0.2, data.getNo2());
        assertEquals(0.3, data.getO3());
        assertEquals(0.4, data.getSo2());
        assertEquals(0.6, data.getPm2_5());
        assertEquals(0.7, data.getPm10());
        assertEquals(0.8, data.getNh3());
    }

    @Test
    void testEqualsAndHashCode() {
        AirQualityData data1 = new AirQualityData();
        data1.setAqi(3);
        data1.setLatitude(1.1);
        data1.setLongitude(2.2);

        AirQualityData data2 = new AirQualityData();
        data2.setAqi(3);
        data2.setLatitude(1.1);
        data2.setLongitude(2.2);

        // Standard equality
        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());

        // Change one value and test inequality
        data2.setAqi(5);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        AirQualityData data = new AirQualityData();
        assertNotEquals(data, null);
        assertNotEquals(data, new Object());
    }

    @Test
    void testToStringNotNull() {
        AirQualityData data = new AirQualityData();
        data.setAqi(1);
        String stringRep = data.toString();
        assertNotNull(stringRep);
        assertTrue(stringRep.contains("aqi=1"));
    }

    // Using an annotated subclass to force Lombok to generate equals/canEqual for the subclass.
    @Data
    static class SubAirQualityData extends AirQualityData {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void testEqualsFailsWhenSubclassOverridesCanEqual() {
        AirQualityData base = new AirQualityData();
        base.setAqi(10);

        SubAirQualityData sub = new SubAirQualityData();
        sub.setAqi(10);

        // Because the subclass’s equals (generated via @Data) now checks that the other is a SubAirQualityData,
        // we expect the comparisons to return false.
        assertFalse(base.equals(sub), "Expected base.equals(sub) to be false when subclass overrides canEqual()");
        assertFalse(sub.equals(base), "Expected sub.equals(base) to be false when subclass overrides canEqual()");
    }
}
