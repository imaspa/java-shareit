package ru.practicum.shareit;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.core.handler.ErrorMessage;
import ru.practicum.shareit.core.handler.HandlerException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class HandlerExceptionTest {

    @InjectMocks
    private HandlerException handlerException;

    @Test
    void handleNotFoundException_ReturnsNotFound() {
        NotFoundException ex = new NotFoundException("Item not found");

        ResponseEntity<ErrorMessage> response = handlerException.notFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Item not found", response.getBody().getError());
    }

    @Test
    void handleConditionsException_ReturnsBadRequest() {
        ConditionsException ex = new ConditionsException("Invalid state");

        ResponseEntity<ErrorMessage> response = handlerException.conditionsException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid state", response.getBody().getError());
    }

    @Test
    void handleConflictException_ReturnsConflict() {
        ConflictException ex = new ConflictException("Already exists");

        ResponseEntity<ErrorMessage> response = handlerException.conflictException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Already exists", response.getBody().getError());
    }


    @Test
    void handleAllExceptions_ReturnsInternalServerError() {
        Exception ex = new RuntimeException("Boom!");

        ResponseEntity<ErrorMessage> response = handlerException.handleAllExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Произошла непредвиденная ошибка", response.getBody().getError());
    }
}