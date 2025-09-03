package ru.practicum.shareit.user.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank(message = "параметр: `Имя` обязателен к заполнению")
    private String name;

    @NotBlank(message = "параметр: `Адрес электронной почты` обязателен к заполнению")
    @Email(message = "параметр: `Адрес электронной почты` должен быть валидным")
    private String email;
}
