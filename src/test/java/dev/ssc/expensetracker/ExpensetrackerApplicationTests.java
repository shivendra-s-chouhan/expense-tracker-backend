package dev.ssc.expensetracker;

import dev.ssc.expensetracker.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(SecurityConfig.class)
class ExpensetrackerApplicationTests {

	@Test
	void contextLoads() {
	}

}
