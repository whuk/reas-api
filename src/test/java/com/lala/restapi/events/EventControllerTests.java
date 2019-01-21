package com.lala.restapi.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lala.restapi.common.RestDocsConfiguration;
import com.lala.restapi.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ModelMapper modelMapper;


    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder().name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 23, 23, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 23, 23, 59))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 23, 23, 00))
                .endEventDateTime(LocalDateTime.of(2018, 12, 23, 23, 59))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("D2 Factory")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event))
        ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update an existing event"),
                                linkWithRel("profile").description("link to profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of Event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event")

                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location Header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content Type")
                        ),
                        responseFields(  // relaxed prefix 는 모든 필드를 안해도, 사실 링크는 위에 이미 했음...
                                fieldWithPath("id").description("Identifier of Event"),
                                fieldWithPath("name").description("Name of Event"),
                                fieldWithPath("description").description("Description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("BeginEnrollmentDateTime of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("CloseEnrollmentDateTime of new event"),
                                fieldWithPath("beginEventDateTime").description("BeginEventDateTime of new event"),
                                fieldWithPath("endEventDateTime").description("EndEventDateTime of new event"),
                                fieldWithPath("location").description("Location of new event"),
                                fieldWithPath("basePrice").description("BasePrice of new event"),
                                fieldWithPath("maxPrice").description("MaxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("LimitOfEnrollment of new event"),
                                fieldWithPath("free").description("Is Free of new event"),
                                fieldWithPath("offline").description("Is Offline of new event"),
                                fieldWithPath("eventStatus").description("Event Status of new event"),
                                fieldWithPath("_links.self.href").description("Link to self"),
                                fieldWithPath("_links.query-events.href").description("Link to query events"),
                                fieldWithPath("_links.update-event.href").description("Link to update event"),
                                fieldWithPath("_links.profile.href").description("Link to profile")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder().name("Spring")
                .id(100)
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 23, 23, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 23, 23, 59))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 23, 23, 00))
                .endEventDateTime(LocalDateTime.of(2018, 12, 23, 23, 59))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("D2 Factory")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력값이 잘못된 경우 에러가 발생하는 테스")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API Development with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 26, 23, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 25, 23, 59))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 24, 23, 00))
                .endEventDateTime(LocalDateTime.of(2018, 12, 23, 23, 59))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("D2 Factory")
                .build();

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())
        ;
    }

    @Test
    @TestDescription("30개의 이벤트를 10개씩 두번째 페이지 조회하기")
    public void queryEvents() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateEvent);

        // When
        this.mockMvc.perform(get("/api/events")
                .param("page", "1") // 1이 두번째 페이지
                .param("size", "10")
                .param("sort", "name,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;

    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event " + index)
                .description("test event")
                .beginEnrollmentDateTime(LocalDateTime.of(2018, 12, 23, 23, 00))
                .closeEnrollmentDateTime(LocalDateTime.of(2018, 12, 23, 23, 59))
                .beginEventDateTime(LocalDateTime.of(2018, 12, 23, 23, 00))
                .endEventDateTime(LocalDateTime.of(2018, 12, 23, 23, 59))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("D2 Factory")
                .free(false)
                .offline(true)
                .eventStatus(EventStatus.DRAFT)
                .build();
        return this.eventRepository.save(event);
    }

    @Test
    @TestDescription("기존의 이벤트 하나를 조회하기")
    public void getEvent() throws Exception {
        // Given
        Event event = this.generateEvent(100);

        // When & Then
        this.mockMvc.perform(get("/api/events/{id}", event.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;

    }

    @Test
    @TestDescription("이벤트를 정상적으로 수정하기")
    public void updateEvent() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        String eventName = "Updated Event";
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setName(eventName);

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(this.objectMapper.writeValueAsString(eventDto))
        )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("name").value(eventName))
                    .andExpect(jsonPath("_links.self").exists())
                    .andDo(document("update-event"))

        ;
    }

    @Test
    @TestDescription("입력값이 비어있는 경우 이벤트 수정 실패")
    public void updateEvent400_empty() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = new EventDto();

        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("입력값이 잘못된 경우 이벤트 수정 실패")
    public void updateEvent400_wrong() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(10000);


        // When & Then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @TestDescription("존재하지 않는 이벤트 수정 실패")
    public void updateEvent404() throws Exception {
        // Given
        Event event = this.generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);

        // When & Then
        this.mockMvc.perform(put("/api/events/123456")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto))
        )
                .andDo(print())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    @TestDescription("아무것도 없는 데이터 조회시 404 응답받기")
    public void getEvent404() throws Exception {
        // When & Then
        this.mockMvc.perform(get("/api/events/12345"))
                .andExpect(status().isNotFound())
        ;
    }
}
