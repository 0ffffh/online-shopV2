package com.k0s.onlineshop;

import com.k0s.onlineshop.testcontainers.TestContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(classes = OnlineShopApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OnlineShopApplicationTests extends TestContainer {


    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;




//	@Test
//	void contextLoads() {
//        Reflect
//	}

}
