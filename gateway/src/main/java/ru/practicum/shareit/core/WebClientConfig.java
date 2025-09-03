package ru.practicum.shareit.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.booking.BookingClient;
import ru.practicum.shareit.item.ItemClient;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.user.UserClient;

@Configuration
public class WebClientConfig {

    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .build();
    }

    @Bean
    public UserClient userClient(RestTemplate restTemplate) {
        return new UserClient(restTemplate);
    }

    @Bean
    public ItemClient itemClient(RestTemplate restTemplate) {
        return new ItemClient(restTemplate);
    }

    @Bean
    public BookingClient bookingClient(RestTemplate restTemplate) {
        return new BookingClient(restTemplate);
    }

    @Bean
    public ItemRequestClient itemRequestClient(RestTemplate restTemplate) {
        return new ItemRequestClient(restTemplate);
    }

}
