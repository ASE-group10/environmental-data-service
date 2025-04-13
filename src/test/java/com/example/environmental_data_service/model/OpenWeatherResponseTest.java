package com.example.environmental_data_service.model;

import com.example.environmental_data_service.model.OpenWeatherResponse.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OpenWeatherResponseTest {

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

    // ✨ Force Lombok-generated canEqual to return false by subclassing
    @Data
    static class SubCoord extends Coord {
        @Override public boolean canEqual(Object other) { return false; }
    }

    @Test
    void testCanEqualBranch_coord() {
        Coord base = new Coord(); base.setLat(1); base.setLon(2);
        SubCoord sub = new SubCoord(); sub.setLat(1); sub.setLon(2);

        assertFalse(base.equals(sub));
        assertFalse(sub.equals(base));
    }
}
