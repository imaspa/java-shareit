package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getAll();
    }

     */

    @GetMapping("/{userId}")
    public UserDto getEntity(@PathVariable Long userId) throws NotFoundException {
        return userService.getEntity(userId);
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto userDto) throws ConditionsException, ConflictException {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Long userId, @RequestBody UserDto userDto) throws NotFoundException, ConflictException {
        return userService.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.delete(userId);
    }

}
