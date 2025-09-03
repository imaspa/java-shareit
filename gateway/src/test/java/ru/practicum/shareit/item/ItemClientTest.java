package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.comment.dto.CommentsInputDto;
import ru.practicum.shareit.item.dto.ItemDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ItemClient.class)
class ItemClientTest {

    @Autowired
    private ItemClient itemClient;

    private RestTemplate restTemplate;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        restTemplate = (RestTemplate) ReflectionTestUtils.getField(itemClient, "rest");
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldCreateItem() {
        ItemDto itemDto = ItemDto.builder()
                .name("itemName")
                .description("desc")
                .build();


//                new ItemDto(
//                null,
//                "itemName",
//                "desc",
//                true,
//                null,
//                null,
//                null,
//                Collections.emptyList()
//        );

        String jsonResponse = "{\n" +
                "  \"id\": 1,\n" +
                "  \"name\": \"itemName\",\n" +
                "  \"description\": \"desc\",\n" +
                "  \"available\": true,\n" +
                "  \"requestId\": null,\n" +
                "  \"lastBooking\": null,\n" +
                "  \"nextBooking\": null,\n" +
                "  \"comments\": []\n" +
                "}";

        server.expect(requestTo("http://localhost:9090/items"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.create(1L, itemDto);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        server.verify();
    }

    @Test
    void shouldGetItemById() {
        Long userId = 1L;
        Long itemId = 2L;
        String jsonResponse = "{\n" +
                "  \"id\": 2,\n" +
                "  \"name\": \"itemName2\",\n" +
                "  \"description\": \"desc2\",\n" +
                "  \"available\": true,\n" +
                "  \"requestId\": null,\n" +
                "  \"lastBooking\": null,\n" +
                "  \"nextBooking\": null,\n" +
                "  \"comments\": []\n" +
                "}";

        server.expect(requestTo("http://localhost:9090/items/" + itemId))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", userId.toString()))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.getEntity(userId, itemId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();

        server.verify();
    }

    @Test
    void shouldGetItemsByOwner() {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        String path = "http://localhost:9090/items";
        String jsonResponse = "[]";

        server.expect(requestTo(path))
                .andExpect(method(HttpMethod.GET))
                .andExpect(header("X-Sharer-User-Id", userId.toString()))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.getAllItemOwner(userId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        server.verify();
    }

    @Test
    void shouldUpdateItem() {
        Long userId = 1L;
        Long itemId = 2L;
        ItemDto itemDto = ItemDto.builder()
                .id(itemId)
                .name("updatedName")
                .description("updatedDesc")
                .available(true)
                .build();

        //new ItemDto(itemId, "updatedName", "updatedDesc", true, null, null, null, Collections.emptyList());
        String jsonResponse = "{\n" +
                "  \"id\": 2,\n" +
                "  \"name\": \"updatedName\",\n" +
                "  \"description\": \"updatedDesc\",\n" +
                "  \"available\": true,\n" +
                "  \"requestId\": null,\n" +
                "  \"lastBooking\": null,\n" +
                "  \"nextBooking\": null,\n" +
                "  \"comments\": []\n" +
                "}";

        server.expect(requestTo("http://localhost:9090/items/" + itemId))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(header("X-Sharer-User-Id", userId.toString()))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.update(userId, itemId, itemDto);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        server.verify();
    }


    @Test
    void shouldGetItemsBySearchQuery() {
        String text = "search";
        String path = "http://localhost:9090/items/search?text=" + text;
        String jsonResponse = "[]";

        server.expect(requestTo(path))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.find(1L, text);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        server.verify();
    }

    @Test
    void shouldCreateComment() {
        Long userId = 1L;
        Long itemId = 2L;
        CommentsInputDto commentDto = CommentsInputDto
                .builder()
                .text("comment")
                .build();

        String jsonResponse = "{\n" +
                "  \"id\": 1,\n" +
                "  \"text\": \"comment\"\n" +
                "}";

        server.expect(requestTo("http://localhost:9090/items/" + itemId + "/comment"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("X-Sharer-User-Id", userId.toString()))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));

        ResponseEntity<Object> response = itemClient.createComments(userId, itemId, commentDto);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        server.verify();
    }
}
