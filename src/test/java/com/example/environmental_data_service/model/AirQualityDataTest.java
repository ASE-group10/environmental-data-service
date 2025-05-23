package com.example.environmental_data_service.model;

import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AirQualityDataTest {

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

        assertNotEquals(base, sub);
        assertNotEquals(sub, base);
    }

    @Test
    void testFullEqualsBranches() {
        AirQualityData data1 = new AirQualityData();
        data1.setLatitude(1.0);
        data1.setLongitude(2.0);
        data1.setAqi(5);

        // Reflexive check
        assertEquals(data1, data1);

        // Symmetric check with same values
        AirQualityData data2 = new AirQualityData();
        data2.setLatitude(1.0);
        data2.setLongitude(2.0);
        data2.setAqi(5);
        assertEquals(data1, data2);

        // At least one different field
        AirQualityData data3 = new AirQualityData();
        data3.setLatitude(9.9); // different
        data3.setLongitude(2.0);
        data3.setAqi(5);
        assertNotEquals(data1, data3);

        // null comparison (already tested, but safe to keep)
        assertNotEquals(null, data1);

        // Cross-type comparison
        assertNotEquals((Object) "notAirQualityData", data1);

        // canEqual returning true explicitly (same type)
        assertTrue(data1.canEqual(data2));
    }

    private AirQualityData createSampleData() {
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
        return data;
    }

    @Test
    void testAirQualityDataSettersAndGetters() {
        AirQualityData data = createSampleData();

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
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();

        assertEquals(data1, data2);
        assertEquals(data1.hashCode(), data2.hashCode());

        data2.setAqi(5);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWithNullAndDifferentClass() {
        AirQualityData data = new AirQualityData();
        assertNotEquals(null, data);
        assertNotEquals(new Object(), data);
    }

    @Test
    void testToStringNotNull() {
        AirQualityData data = createSampleData();
        String stringRep = data.toString();
        assertNotNull(stringRep);
        // Check all fields are present in toString
        assertTrue(stringRep.contains("latitude=53.3498"));
        assertTrue(stringRep.contains("longitude=-6.2603"));
        assertTrue(stringRep.contains("aqi=2"));
        assertTrue(stringRep.contains("co=0.5"));
        // Repeat for other fields...
    }

    // Tests for each field's impact on equals
    @Test
    void testEqualsWhenCoDifferent() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setCo(0.9);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWhenNoDifferent() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setNo(0.9);
        assertNotEquals(data1, data2);
    }

    // Repeat similar tests for no2, o3, so2, pm2_5, pm10, nh3
    @Test
    void testEqualsWhenNo2Different() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setNo2(0.9);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWhenO3Different() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setO3(0.9);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWhenSo2Different() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setSo2(0.9);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWhenPm2_5Different() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setPm2_5(0.9);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWhenPm10Different() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setPm10(0.9);
        assertNotEquals(data1, data2);
    }

    @Test
    void testEqualsWhenNh3Different() {
        AirQualityData data1 = createSampleData();
        AirQualityData data2 = createSampleData();
        data2.setNh3(0.9);
        assertNotEquals(data1, data2);
    }
}
