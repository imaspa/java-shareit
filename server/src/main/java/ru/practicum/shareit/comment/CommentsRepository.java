package ru.practicum.shareit.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.comment.model.Comments;

@Repository
public interface CommentsRepository extends JpaRepository<Comments, Long> {
}