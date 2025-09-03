package ru.practicum.shareit.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentsInputDto {

    private Long itemId;

    private Long authorId;

    @NotBlank(message = "параметр: `Комментарий` обязателен к заполнению")
    private String text;

}
