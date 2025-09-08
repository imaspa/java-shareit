package ru.practicum.shareit.request.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.BatchSize;
import ru.practicum.shareit.core.model.Identifiable;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.Users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_request")
@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @BatchSize(size = 50)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "requestor_id")
    private Users requestor;

    @Column(name = "created")
    private LocalDateTime created;
}
