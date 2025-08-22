package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "параметр: `Наименование` обязателен к заполнению")
    private String name;
    @NotBlank(message = "параметр: `Описание` обязателен к заполнению")
    private String description;

    @NotNull(message = "параметр: `Статус` обязателен к заполнению")
    private Boolean available;

    @NotNull(message = "параметр: `Владелец` обязателен к заполнению")
    private Long ownerId;

    private Long requestId;
}
