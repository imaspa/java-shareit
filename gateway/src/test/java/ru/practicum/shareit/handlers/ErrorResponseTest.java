package ru.practicum.shareit.handlers;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.core.handler.ErrorMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {

    @Test
    void shouldStoreErrorMessage() {
        ErrorMessage response = new ErrorMessage("Ошибка");
        assertEquals("Ошибка", response.getError());
    }
}
