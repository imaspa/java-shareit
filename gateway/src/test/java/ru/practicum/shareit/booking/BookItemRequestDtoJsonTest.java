package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingInputDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookItemRequestDtoJsonTest {
    private final JacksonTester<BookingInputDto> json;

    @Test
    void shouldSerializeBookingInputDtoCorrectly() throws Exception {
        BookingInputDto input = BookingInputDto.builder()
                .itemId(1L)
                .start(LocalDateTime.parse("2025-09-01T00:00:10"))
                .end(LocalDateTime.parse("2025-09-01T00:00:20"))
                .build();
        JsonContent<BookingInputDto> result = json.write(input);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2025-09-01T00:00:10");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2025-09-01T00:00:20");
    }

}