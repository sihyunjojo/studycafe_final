package project.studycafe;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

// 이렇게 하면 application-test.properties 파일을 높은 우선순위로 인식합니다.
//@ActiveProfiles("test")
@SpringBootTest
class StudycafeApplicationTests {

	@Test
	void contextLoads() {
	}

}
