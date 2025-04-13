package com.example.environmental_data_service.model;

import com.example.environmental_data_service.model.OpenWeatherResponse.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenWeatherResponseTest {

    final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testDeserialization_fullStructure() throws Exception {
        String json = """
        {
          "coord": { "lon": -6.26, "lat": 53.34 },
          "list": [
            {
              "main": { "aqi": 2 },
              "components": {
                "co": 0.1, "no": 0.2, "no2": 0.3, "o3": 0.4,
                "so2": 0.5, "pm2_5": 0.6, "pm10": 0.7, "nh3": 0.8
              },
              "dt": 123456789
            }
          ]
        }
        """;
        OpenWeatherResponse res = mapper.readValue(json, OpenWeatherResponse.class);
        assertNotNull(res);
        assertNotNull(res.getCoord());
        assertEquals(-6.26, res.getCoord().getLon());
        assertEquals(53.34, res.getCoord().getLat());

        assertNotNull(res.getList());
        assertEquals(1, res.getList().size());

        AirData airData = res.getList().get(0);
        assertEquals(2, airData.getMain().getAqi());
        assertEquals(0.6, airData.getComponents().getPm2_5());
        assertEquals(123456789L, airData.getDt());
    }

    @Test
    void testDeserialization_missingFields() throws Exception {
        String json = """
        {
          "coord": {},
          "list": [
            {
              "main": {},
              "components": {},
              "dt": null
            }
          ]
        }
        """;
        OpenWeatherResponse res = mapper.readValue(json, OpenWeatherResponse.class);
        assertNotNull(res.getCoord());
        assertEquals(0.0, res.getCoord().getLat());
        assertEquals(0.0, res.getCoord().getLon());

        AirData data = res.getList().get(0);
        assertNotNull(data.getMain());
        assertEquals(0, data.getMain().getAqi());
        assertNotNull(data.getComponents());
        assertEquals(0.0, data.getComponents().getCo());
        assertNull(data.getDt());
    }

    @Test
    void testDeserialization_emptyJson() throws Exception {
        String json = "{}";
        OpenWeatherResponse res = mapper.readValue(json, OpenWeatherResponse.class);
        assertNull(res.getCoord());
        assertNull(res.getList());
    }

    @Test
    void testDeserialization_nullList() throws Exception {
        String json = """
        {
          "coord": { "lon": 1.0, "lat": 2.0 },
          "list": null
        }
        """;
        OpenWeatherResponse res = mapper.readValue(json, OpenWeatherResponse.class);
        assertEquals(1.0, res.getCoord().getLon());
        assertEquals(2.0, res.getCoord().getLat());
        assertNull(res.getList());
    }

    @Test
    void testEquality_andToString() {
        Coord c1 = new Coord(); c1.setLon(1); c1.setLat(2);
        Coord c2 = new Coord(); c2.setLon(1); c2.setLat(2);
        Coord c3 = new Coord(); c3.setLon(9); c3.setLat(9);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
        assertNotNull(c1.toString());

        Main m1 = new Main(); m1.setAqi(5);
        Main m2 = new Main(); m2.setAqi(5);
        Main m3 = new Main(); m3.setAqi(9);
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);

        Components comp1 = new Components();
        comp1.setCo(1); comp1.setNo(2); comp1.setNo2(3); comp1.setO3(4); comp1.setSo2(5);
        comp1.setPm2_5(6); comp1.setPm10(7); comp1.setNh3(8);

        Components comp2 = new Components();
        comp2.setCo(1); comp2.setNo(2); comp2.setNo2(3); comp2.setO3(4); comp2.setSo2(5);
        comp2.setPm2_5(6); comp2.setPm10(7); comp2.setNh3(8);

        Components comp3 = new Components(); // empty
        assertEquals(comp1, comp2);
        assertNotEquals(comp1, comp3);
    }

    @Data
    static class SubCoord extends Coord {
        @Override public boolean canEqual(Object other) { return false; }
    }

    @Test
    void testCanEqualBranch_coord() {
        Coord base = new Coord(); base.setLat(1); base.setLon(2);
        SubCoord sub = new SubCoord(); sub.setLat(1); sub.setLon(2);

        assertNotEquals(base, sub);
        assertNotEquals(sub, base);
    }

    @Test
    void testEquality_hashCode_toString_allBranches() {
        // Coord
        Coord coord1 = new Coord();
        coord1.setLon(1.0);
        coord1.setLat(2.0);

        Coord coord2 = new Coord();
        coord2.setLon(1.0);
        coord2.setLat(2.0);

        Coord coordDiff = new Coord();
        coordDiff.setLon(9.0);
        coordDiff.setLat(9.0);

        assertEquals(coord1, coord2);
        assertEquals(coord1.hashCode(), coord2.hashCode());
        assertNotEquals(coord1, coordDiff);
        assertNotEquals(null, coord1);
        assertNotEquals(new Object(), coord1);  // cross-type
        assertTrue(coord1.canEqual(coord2));
        assertNotNull(coord1.toString());

        // Main
        Main main1 = new Main();
        main1.setAqi(5);
        Main main2 = new Main();
        main2.setAqi(5);
        Main main3 = new Main();
        main3.setAqi(10);

        assertEquals(main1, main2);
        assertEquals(main1.hashCode(), main2.hashCode());
        assertNotEquals(main1, main3);
        assertNotEquals(null, main1);
        assertNotEquals(new Object(), main1);
        assertTrue(main1.canEqual(main2));
        assertNotNull(main1.toString());

        // Components
        Components comp1 = new Components();
        comp1.setCo(1.0); comp1.setNo(2.0); comp1.setNo2(3.0); comp1.setO3(4.0);
        comp1.setSo2(5.0); comp1.setPm2_5(6.0); comp1.setPm10(7.0); comp1.setNh3(8.0);

        Components comp2 = new Components();
        comp2.setCo(1.0); comp2.setNo(2.0); comp2.setNo2(3.0); comp2.setO3(4.0);
        comp2.setSo2(5.0); comp2.setPm2_5(6.0); comp2.setPm10(7.0); comp2.setNh3(8.0);

        Components comp3 = new Components(); // different values

        assertEquals(comp1, comp2);
        assertEquals(comp1.hashCode(), comp2.hashCode());
        assertNotEquals(comp1, comp3);
        assertNotEquals(null, comp1);
        assertNotEquals(new Object(), comp1);
        assertTrue(comp1.canEqual(comp2));
        assertNotNull(comp1.toString());

        // AirData
        AirData air1 = new AirData();
        air1.setDt(123L);
        air1.setMain(main1);
        air1.setComponents(comp1);

        AirData air2 = new AirData();
        air2.setDt(123L);
        air2.setMain(main1);
        air2.setComponents(comp1);

        AirData air3 = new AirData(); // different

        assertEquals(air1, air2);
        assertEquals(air1.hashCode(), air2.hashCode());
        assertNotEquals(air1, air3);
        assertNotEquals(null, air1);
        assertNotEquals(new Object(), air1);
        assertTrue(air1.canEqual(air2));
        assertNotNull(air1.toString());
    }

    @Test
    void testAirDataMethods_fullCoverage() {
        Main main = new Main(); main.setAqi(3);
        Components components = new Components(); components.setPm10(10.0);

        AirData air1 = new AirData();
        air1.setMain(main);
        air1.setComponents(components);
        air1.setDt(123L);

        assertEquals(main, air1.getMain());
        assertEquals(components, air1.getComponents());
        assertEquals(123L, air1.getDt());

        AirData air2 = new AirData();
        air2.setMain(main);
        air2.setComponents(components);
        air2.setDt(123L);

        AirData airDiff = new AirData();
        airDiff.setMain(new Main()); // different values

        assertEquals(air1, air2);
        assertEquals(air1.hashCode(), air2.hashCode());
        assertNotEquals(air1, airDiff);
        assertNotEquals(null, air1);
        assertNotEquals(new Object(), air1);

        assertTrue(air1.canEqual(air2));
        assertNotNull(air1.toString());
    }

    @Data
    static class SubAirData extends AirData {
        @Override
        public boolean canEqual(Object other) {
            return false;
        }
    }

    @Test
    void testCanEqualBranch_airData() {
        AirData air = new AirData();
        air.setDt(1L);

        SubAirData sub = new SubAirData();
        sub.setDt(1L);

        assertNotEquals(air, sub);
        assertNotEquals(sub, air);
    }

}
