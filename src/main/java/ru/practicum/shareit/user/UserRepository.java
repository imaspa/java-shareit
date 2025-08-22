package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.core.BaseRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.Users;

import java.util.Objects;

@Repository
public class UserRepository extends BaseRepository<Users> {

    public Boolean isEmailExistsAnotherUser(UserDto userDto) {
        return findAll()
                .stream()
                .filter(u -> !Objects.equals(u.getId(), userDto.getId()))
                .anyMatch(u -> u.getEmail().equals(userDto.getEmail()));
    }
}
