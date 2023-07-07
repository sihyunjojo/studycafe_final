package project.studycafe.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

@Slf4j
public class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
    @Override
    public LocalDateTime parse(String stringDateTime, Locale locale) throws ParseException {
//        log.info("start format parse");
//        log.info("stringDatetime = {}, locale= {}", stringDateTime, locale);
//
//        DateTimeFormatter formatter;
//
//        // 시간 포맷인 경우
//        if (stringDateTime.matches("\\d{2}:\\d{2}")) {
//            log.info("today");
//            formatter = DateTimeFormatter.ofPattern("HH:mm");
//        LocalDateTime.of()로 값 나눠서 넣어주기
//            LocalTime.parse(text, formatter).
//            return LocalDateTime.parse(stringDateTime, formatter).atDate(LocalDate.now());
//        }
//        // 날짜 포맷인 경우
//        else if (stringDateTime.matches("\\d{2}\\.\\d{2}\\.\\d{2}")) {
//            log.info("not today");
//            formatter = DateTimeFormatter.ofPattern("yy.MM.dd");
//        } else {
//            throw new ParseException("Invalid datetime format", 0);
//        }
//
//
//        return LocalDateTime.parse(stringDateTime, formatter);
        return null;
    }

    @Override
    public String print(LocalDateTime dateTime, Locale locale) {
        DateTimeFormatter formatter;

        LocalDateTime now = LocalDateTime.now();
        if (dateTime.toLocalDate().equals(now.toLocalDate())) {
            formatter =  DateTimeFormatter.ofPattern("HH:mm");
        } else {
            formatter=  DateTimeFormatter.ofPattern("yy.MM.dd");
        }

        return dateTime.format(formatter);

    }
}
