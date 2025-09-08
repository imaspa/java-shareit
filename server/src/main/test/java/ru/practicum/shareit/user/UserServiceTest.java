package ru.practicum.shareit.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.core.exception.ConditionsException;
import ru.practicum.shareit.core.exception.ConflictException;
import ru.practicum.shareit.core.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.Users;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {

    private final UserService userService;
    private final EntityManager entityManager;

    @Test
    void create_ShouldPersistUser_WhenValidData() throws ConditionsException, ConflictException {
        UserDto input = UserDto.builder()
                .name("no")
                .email("no@ya.ru")
                .build();

        userService.create(input);

        Users user = findUserByEmail(input.getEmail());

        assertAll(
                () -> assertThat(user.getId(), notNullValue()),
                () -> assertThat(user.getName(), equalTo(input.getName())),
                () -> assertThat(user.getEmail(), equalTo(input.getEmail()))
        );
    }

    @Test
    void create_WithDuplicateEmail_ShouldThrowConflictException() throws ConditionsException, ConflictException {
        UserDto userDto = UserDto.builder()
                .name("no")
                .email("no@ya.ru")
                .build();

        userService.create(userDto);

        UserDto duplicate = UserDto.builder()
                .name("no1")
                .email("no@ya.ru")
                .build();

        assertThrows(ConflictException.class,
                () -> userService.create(duplicate),
                "Ожидается исключение при повторном email");
    }

    @Test
    void update_WithNewName_ShouldChangeOnlyName() throws ConditionsException, ConflictException, NotFoundException {
        UserDto created = createUser("no", "no@ya.ru");
        UserDto update = UserDto.builder()
                .name("no1")
                .email(created.getEmail())
                .build();

        userService.update(created.getId(), update);

        Users user = findUserById(created.getId());

        assertAll(
                () -> assertThat(user.getId(), equalTo(created.getId())),
                () -> assertThat(user.getName(), equalTo("no1")),
                () -> assertThat(user.getEmail(), equalTo(created.getEmail()))
        );
    }

    @Test
    void update_WithNewEmail_ShouldChangeOnlyEmail() throws ConditionsException, ConflictException, NotFoundException {
        UserDto created = createUser("no", "no@ya.ru");
        UserDto update = UserDto.builder()
                .name(created.getName())
                .email("no1@ya.ru")
                .build();

        userService.update(created.getId(), update);

        Users user = findUserById(created.getId());

        assertAll(
                () -> assertThat(user.getId(), equalTo(created.getId())),
                () -> assertThat(user.getEmail(), equalTo("no1@ya.ru")),
                () -> assertThat(user.getName(), equalTo(created.getName()))
        );
    }

    @Test
    void update_UnknownUser_ShouldThrowNotFoundException() {
        UserDto update = UserDto.builder()
                .name("no")
                .email("no2@ya.ru")
                .build();

        assertThrows(NotFoundException.class,
                () -> userService.update(1L, update));
    }

    @Test
    void getEntity_UnknownId_ShouldThrowNotFoundException() {
        assertThrows(NotFoundException.class,
                () -> userService.getEntity(1L));
    }

    @Test
    void getEntity_ExistingId_ShouldReturnUserDto() throws ConditionsException, ConflictException, NotFoundException {
        UserDto created = createUser("no", "no@ya.ru");
        UserDto found = userService.getEntity(created.getId());

        assertAll(
                () -> assertThat(found.getId(), equalTo(created.getId())),
                () -> assertThat(found.getName(), equalTo(created.getName())),
                () -> assertThat(found.getEmail(), equalTo(created.getEmail()))
        );
    }

    @Test
    void delete_ExistingUser_ShouldRemoveFromDatabase() throws ConditionsException, ConflictException, NotFoundException {
        UserDto created = createUser("no", "no@ya.ru");
        userService.delete(created.getId());

        assertThrows(NoResultException.class,
                () -> findUserById(created.getId()));
    }


    private UserDto createUser(String name, String email) throws ConditionsException, ConflictException {
        UserDto dto = UserDto.builder().name(name).email(email).build();
        return userService.create(dto);
    }

    private Users findUserByEmail(String email) {
        TypedQuery<Users> query = entityManager.createQuery("SELECT u FROM Users u WHERE u.email = :email", Users.class);
        return query.setParameter("email", email).getSingleResult();
    }

    private Users findUserById(Long id) {
        TypedQuery<Users> query = entityManager.createQuery("SELECT u FROM Users u WHERE u.id = :id", Users.class);
        return query.setParameter("id", id).getSingleResult();
    }
}