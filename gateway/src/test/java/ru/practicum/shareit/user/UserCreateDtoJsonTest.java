package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.dto.UserDto;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserCreateDtoJsonTest {
    private final JacksonTester<UserDto> json;

    @Test
    void shouldSerializeUserDtoCorrectly() throws Exception {
        UserDto input = UserDto.builder()
                .name("no")
                .email("no@ya.ru")
                .build();

        JsonContent<UserDto> result = json.write(input);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(input.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(input.getEmail());
    }


}