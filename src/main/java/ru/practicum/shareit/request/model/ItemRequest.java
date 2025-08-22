package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.core.model.Identifiable;

import java.time.LocalDateTime;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest implements Identifiable {

    private Long id;

    private String description;

    private Long requesterId;

    private LocalDateTime created;
}
