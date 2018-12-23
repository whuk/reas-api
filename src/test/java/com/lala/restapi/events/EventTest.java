package com.lala.restapi.events;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("rest api")
                .descroption("rest api description")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javabean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescroption(description);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescroption()).isEqualTo(description);
    }
}