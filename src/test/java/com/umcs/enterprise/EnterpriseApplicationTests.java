package com.umcs.enterprise;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = EnterpriseApplication.class)
class EnterpriseApplicationTests {

	@Test
	void contextLoads() {}
}
