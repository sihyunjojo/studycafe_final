package project.studycafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.apache.coyote.http11.Constants.a;

@SpringBootApplication
public class StudycafeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudycafeApplication.class, args);
    }


}
