package com.k0s.onlineshop;

import com.k0s.onlineshop.testcontainers.TestContainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = OnlineShopApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnlineShopApplicationTests extends TestContainer {
	@Test
	void contextLoads() {
	}

}
