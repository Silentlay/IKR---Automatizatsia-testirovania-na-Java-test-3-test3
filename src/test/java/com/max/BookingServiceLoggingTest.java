package com.max;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceLoggingTest {

    @Spy
    private BookingService bookingService;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        // Получаю логгер BookingService
        Logger logger = (Logger) LoggerFactory.getLogger(BookingService.class);

        // Создаю ListAppender для перехвата логов
        listAppender = new ListAppender<>();
        listAppender.start();

        // Добавляю appender в логгер
        logger.addAppender(listAppender);
    }

    @Test
    void testLoggingOnSuccessfulBooking() throws CantBookException {
        String userId = "user123";
        LocalDateTime from = LocalDateTime.of(2025, 1, 28, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 1, 28, 11, 0);

        // Мокаю, что слот доступен
        when(bookingService.checkTimeInBD(from, to)).thenReturn(true);
        when(bookingService.createBook(userId, from, to)).thenReturn(true);

        // Вызываю метод book
        bookingService.book(userId, from, to);

        // Проверяю, что в логах есть нужное сообщение
        assertTrue(listAppender.list.stream().anyMatch(
                log -> log.getFormattedMessage().contains("Успешное бронирование слота")
        ));
    }

    @Test
    void testLoggingOnFailedBooking() {
        String userId = "user123";
        LocalDateTime from = LocalDateTime.of(2025, 1, 28, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 1, 28, 11, 0);

        // Мокаю, что слот занят
        when(bookingService.checkTimeInBD(from, to)).thenReturn(false);

        // Ожидаю исключение
        try {
            bookingService.book(userId, from, to);
        } catch (CantBookException ignored) {
        }

        // Проверяю, что в логах есть нужное сообщение
        assertTrue(listAppender.list.stream().anyMatch(
                log -> log.getFormattedMessage().contains("Невозможность забронировать слот")
        ));
    }
}
