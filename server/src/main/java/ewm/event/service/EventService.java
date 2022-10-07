package ewm.event.service;

import ewm.category.Category;
import ewm.category.dto.CategoryDto;
import ewm.category.repository.CategoryRepository;
import ewm.event.Event;
import ewm.event.State;
import ewm.event.controller.Sort;
import ewm.event.dto.*;
import ewm.event.repository.EventRepository;
import ewm.event.repository.RatingRepository;
import ewm.exceptions.ObjectNotFoundException;
import ewm.user.dto.UserShortDto;
import ewm.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EventClient eventClient;
    private final RatingRepository ratingRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<EventFullDto> searchEvent(String text,
                                          List<Integer> categories,
                                          boolean paid,
                                          Optional<String> rangeStart,
                                          String rangeEnd,
                                          boolean onlyAvailable,
                                          Sort sort,
                                          int from,
                                          int size) {
        List<Category> categoryList = categoryRepository.findAllById(categories);
        Pageable pageableDate = PageRequest.of(from, size, JpaSort.unsafe("event_date"));
        Pageable pageableViews = PageRequest.of(from, size, JpaSort.unsafe("views"));
        LocalDateTime dateStart = LocalDateTime.parse(rangeStart.get());
        LocalDateTime dateEnd = LocalDateTime.parse(rangeEnd);
        List<Event> list = new ArrayList<>();
        switch (sort) {
            case EVENT_DATE:
                list = eventRepository
                        .findAllEventsCategoryRange(text,
                                categoryList, dateStart, dateEnd, paid, State.PUBLISHED, pageableDate);
                break;
            case VIEWS:
                list = eventRepository
                        .findAllEventsCategoryRange(text,
                                categoryList, dateStart, dateEnd, paid, State.PUBLISHED, pageableViews);
                break;
        }
        return list.stream()
                .map(this::setStatisticViews)
                .map(this::getEventFullDto)
                .sorted(Comparator.comparing(EventFullDto::getRating).reversed())
                .collect(Collectors.toList());
    }

    public EventFullDto getEventById(int id) {
        assert eventRepository.existsById(id);
        Event innerEvent = eventRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Событие не найдено"));
        innerEvent = setStatisticViews(innerEvent);
        return getEventFullDto(innerEvent);
    }

    private Event setStatisticViews(Event innerEvent) {
        innerEvent.setViews(((ViewStatsDto) eventClient.getViews(LocalDateTime.MIN.format(formatter),
                LocalDateTime.now().format(formatter),
                List.of("/events/{" + innerEvent.getId() + "}"), true).getBody()).getHits());
        return eventRepository.save(innerEvent);
    }

    public List<EventFullDto> getAllEventsByUser(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return eventRepository.findAllByInitiator(userId, pageable)
                .stream()
                .map(this::setStatisticViews)
                .map(this::getEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByUser(int userId, UpdateEventRequest updateEventRequest) {
        Event event = EventMapper.fromUpdateToEvent(updateEventRequest);
        setCategoryStateInitiator(event, updateEventRequest.getCategory(), userId);
        return getEventFullDto(eventRepository.save(event));
    }

    private void setCategoryStateInitiator(Event event, int updateEventRequest, int userId) {
        event.setCategory(categoryRepository.findById(updateEventRequest)
                .orElseThrow(() -> new ObjectNotFoundException("Категория не найдена")));
        event.setState(State.PENDING);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден")));
    }

    @Transactional
    public EventFullDto createEventByUser(int userId, NewEventDto newEventDto) {
        Event event = EventMapper.fromNewToEvent(newEventDto);
        setCategoryStateInitiator(event, newEventDto.getCategory(), userId);
        return getEventFullDto(eventRepository.save(event));
    }

    private EventFullDto getEventFullDto(Event innerEvent) {
        EventFullDto eventFullDto = EventMapper.toEventFullDto(innerEvent);
        eventFullDto.setCategory(modelMapper.map(innerEvent.getCategory(), CategoryDto.class));
        eventFullDto.setEventDate(innerEvent.getEventDate().format(formatter));
        eventFullDto.setCreatedOn(innerEvent.getCreatedOn().format(formatter));
        if (innerEvent.getPublishedOn() != null)
            eventFullDto.setPublishedOn(innerEvent.getPublishedOn().format(formatter));
        eventFullDto.setInitiator(modelMapper.map(innerEvent.getInitiator(), UserShortDto.class));
        eventFullDto.setRating(ratingRepository.getEventRating(innerEvent.getId()));
        return eventFullDto;
    }

    public EventFullDto getEventByUser(int userId, int eventId) {
        return getEventFullDto(setStatisticViews(eventRepository.findEventByIdAndInitiator(eventId, userId)));
    }

    public List<EventFullDto> searchEventByAdmin(List<Integer> users,
                                                 String states,
                                                 List<Integer> categories,
                                                 String rangeStart,
                                                 String rangeEnd,
                                                 int from,
                                                 int size) {
        Pageable pageable = PageRequest.of(from, size);
        LocalDateTime dateStart = LocalDateTime.parse(rangeStart, formatter);
        LocalDateTime dateEnd = LocalDateTime.parse(rangeEnd, formatter);
        return eventRepository.findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween(users, State.valueOf(states),
                        categories, dateStart, dateEnd, pageable).stream()
                .map(this::setStatisticViews)
                .map(this::getEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByAdmin(int eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = EventMapper.fromAdminToEvent(adminUpdateEventRequest);
        event.setCategory(categoryRepository.findById(adminUpdateEventRequest.getCategory()).get());
        return getEventFullDto(eventRepository.save(event));
    }

    @Transactional
    public EventFullDto publishEvent(int eventId) {
        Event event = new Event();
        Optional<Event> innerEvent = eventRepository.findById(eventId);
        if (innerEvent.isPresent()) {
            event = innerEvent.get();
            event.setState(State.PUBLISHED);
            eventRepository.save(event);
        }
        return getEventFullDto(event);
    }

    @Transactional
    public EventFullDto rejectEvent(int eventId) {
        Event event = new Event();
        Optional<Event> innerEvent = eventRepository.findById(eventId);
        if (innerEvent.isPresent()) {
            event = innerEvent.get();
            event.setState(State.CANCELED);
            eventRepository.save(event);
        }
        return getEventFullDto(event);
    }

}
