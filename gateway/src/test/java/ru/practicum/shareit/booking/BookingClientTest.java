package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingClientTest {

    @Mock
    private RestTemplateBuilder builder;

    @Mock
    private RestTemplate restTemplate;

    private BookingClient bookingClient;

    @BeforeEach
    void setUp() {
        when(builder.uriTemplateHandler(any(DefaultUriBuilderFactory.class))).thenReturn(builder);
        when(builder.requestFactory(any(Supplier.class))).thenReturn(builder);
        when(builder.build()).thenReturn(restTemplate);

        bookingClient = new BookingClient("http://localhost:9090", builder);
    }


    @Test
    void shouldGetBookingById() {
        ResponseEntity<Object> expected = ResponseEntity.ok("booking");

        when(restTemplate.exchange(
                eq("/1"),
                eq(HttpMethod.GET),
                any(), // любой HttpEntity
                eq(Object.class)
        )).thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.getEntity(1L, 1L);

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void shouldUpdateBookingStatus() {
        ResponseEntity<Object> expected = ResponseEntity.ok("updated");

        when(restTemplate.exchange(
                contains("?approved=true"),
                eq(HttpMethod.PATCH),
                any(),
                eq(Object.class)
        )).thenReturn(expected);

        ResponseEntity<Object> response = bookingClient.bookingApproved(1L, 1L, true);

        assertThat(response).isEqualTo(expected);
    }
}