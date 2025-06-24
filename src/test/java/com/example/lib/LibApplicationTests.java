package com.example.lib;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Disabled until context loading is fixed")
class LibApplicationTests {
	@Test void contextLoads() {}
}
