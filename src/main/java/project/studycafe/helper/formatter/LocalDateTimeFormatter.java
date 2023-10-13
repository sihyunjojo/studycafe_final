package project.studycafe.helper.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

// 객체를 특정한 형식으로 변환하는 역할을한다.
// 주로 데이터바인딩 시 사용된다.
@Slf4j
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    @Override
    public LocalDateTime parse(String stringDateTime, Locale locale) throws ParseException {
        log.info("start format parse");
        log.info("stringDateTime = {}, locale= {}", stringDateTime, locale);

        DateTimeFormatter formatter;

        // Check if it's a time format (e.g., 12:34)
        if (stringDateTime.matches("\\d{2}:\\d{2}")) {
            log.info("time format 2-2");
            formatter = DateTimeFormatter.ofPattern("HH:mm");
            // Parse the time and return LocalDateTime with today's date
            LocalTime parsedTime = LocalTime.parse(stringDateTime, formatter);
            return LocalDateTime.of(LocalDate.now(), parsedTime);
        }

        // Check if it's a date format (e.g., 2023-07-04)
        else if (stringDateTime.matches("\\d{4}-\\d{2}-\\d{2}")) {
            log.info("date format 4-2-2");
            //  DateTimeFormatter.ISO_DATE === DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatter = DateTimeFormatter.ISO_DATE;
        }
        // Check if it's a date format (e.g., 21.07.04)
        else if (stringDateTime.matches("\\d{2}\\.\\d{2}\\.\\d{2}")) {
            log.info("date format 2-2-2");
            formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
        } else {
            throw new ParseException("Invalid datetime format", 0);
        }
        log.info("time = {}", LocalDateTime.parse(stringDateTime, formatter));
        return LocalDateTime.parse(stringDateTime, formatter);
    }

    @Override
    public String print(LocalDateTime dateTime, Locale locale) {
        DateTimeFormatter formatter;

        LocalDateTime now = LocalDateTime.now();
        if (dateTime.toLocalDate().equals(now.toLocalDate())) {
            formatter =  DateTimeFormatter.ofPattern("HH:mm");
        } else {
            formatter=  DateTimeFormatter.ofPattern("yy-MM-dd");
        }

        return dateTime.format(formatter);

    }
}
