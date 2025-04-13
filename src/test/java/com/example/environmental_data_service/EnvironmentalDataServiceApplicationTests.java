package com.example.environmental_data_service;

import com.example.environmental_data_service.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = EnvironmentalDataServiceApplication.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
class EnvironmentalDataServiceApplicationTests {

	@Test
	void contextLoads() {
		// This confirms the context loads for the "test" profile
	}
}
