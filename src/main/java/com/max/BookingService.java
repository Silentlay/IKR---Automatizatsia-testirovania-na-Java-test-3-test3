package com.max;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;

public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    public boolean book(String userId, LocalDateTime from, LocalDateTime to) throws CantBookException {
        if (checkTimeInBD(from, to)) {
            boolean success = createBook(userId, from, to);
            if (success) {
                logger.info("Успешное бронирование слота: {} для {}", from, to);
            }
            return success;
        }
        logger.error("Невозможность забронировать слот: {} для {}", from, to);
        throw new CantBookException();
    }

    public boolean checkTimeInBD(LocalDateTime from, LocalDateTime to) {
        return true;
    }

    public boolean createBook(String userId, LocalDateTime from, LocalDateTime to) {
        return true;
    }
}
