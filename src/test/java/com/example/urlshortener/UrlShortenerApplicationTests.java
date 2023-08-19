package com.example.urlshortener;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
class UrlShortenerApplicationTests {

	@Test
	void contextLoads() {
	}

}
