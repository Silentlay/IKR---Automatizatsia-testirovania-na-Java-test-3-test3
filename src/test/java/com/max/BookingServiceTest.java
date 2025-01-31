package com.max;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Spy
    private BookingService bookingService;

    @Test
    void testBookSlotSuccessfully() throws CantBookException {
        String userId = "user123";
        LocalDateTime from = LocalDateTime.of(2025, 1, 28, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 1, 28, 11, 0);

        // Мокирую метод checkTimeInBD для возврата true (слот доступен)
        when(bookingService.checkTimeInBD(from, to)).thenReturn(true);

        // Мокирую метод createBook для возврата true (бронирование успешно)
        when(bookingService.createBook(userId, from, to)).thenReturn(true);

        // Проверяю, что метод book возвращает true, если слот доступен
        boolean result = bookingService.book(userId, from, to);

        // Проверяю, что метод checkTimeInBD был вызван
        verify(bookingService).checkTimeInBD(from, to);
        verify(bookingService).createBook(userId, from, to);

        // Утверждаю, что бронирование прошло успешно
        assertTrue(result);
    }

    @Test
    void testCantBookSlot() {
        String userId = "user123";
        LocalDateTime from = LocalDateTime.of(2025, 1, 28, 10, 0);
        LocalDateTime to = LocalDateTime.of(2025, 1, 28, 11, 0);

        // Мокирую метод checkTimeInBD для возврата false (слот занят)
        when(bookingService.checkTimeInBD(from, to)).thenReturn(false);

        // Проверяю, что метод book выбрасывает исключение CantBookException
        CantBookException exception = assertThrows(CantBookException.class, () -> bookingService.book(userId, from, to));
        assertNotNull(exception);

        // Проверяю, что метод checkTimeInBD был вызван
        verify(bookingService).checkTimeInBD(from, to);
    }
}

