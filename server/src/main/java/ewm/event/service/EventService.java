package ewm.event.service;

import ewm.category.Category;
import ewm.category.dto.CategoryDto;
import ewm.category.repository.CategoryRepository;
import ewm.event.Event;
import ewm.event.EventRating;
import ewm.event.State;
import ewm.event.controller.Sort;
import ewm.event.dto.*;
import ewm.event.repository.EventRepository;
import ewm.exceptions.ObjectNotFoundException;
import ewm.user.dto.UserShortDto;
import ewm.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        Pageable pageableDate = PageRequest.of(from, size, JpaSort.unsafe("eventDate"));
        Pageable pageableViews = PageRequest.of(from, size, JpaSort.unsafe("views"));
        Pageable pageableRating = PageRequest.of(from, size, JpaSort.unsafe("ratings"));
        LocalDateTime dateStart = LocalDateTime.parse(rangeStart.get(), formatter);
        LocalDateTime dateEnd = LocalDateTime.parse(rangeEnd, formatter);
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
                                categoryList, dateStart, dateEnd, paid, State.PENDING, pageableViews);
                break;
            case RATING:
                list = eventRepository
                        .findAllEventsCategoryRange(text,
                                categoryList, dateStart, dateEnd, paid, State.PENDING, pageableRating);
                break;
        }
        return list.stream()
                .map(this::setStatisticViews)
                .map(this::getEventFullDto)
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
        ResponseEntity<Object> responseEntity = eventClient.getViews(
                LocalDateTime.of(1990, 01, 01, 01, 00).format(formatter),
                LocalDateTime.now().format(formatter),
                List.of("/events/{" + innerEvent.getId() + "}"), true);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            ViewStatsDto viewStatsDto = modelMapper.map(responseEntity.getBody(), ViewStatsDto.class);
            innerEvent.setViews(viewStatsDto.getHits());
        }
        return eventRepository.save(innerEvent);
    }

    public List<EventFullDto> getAllEventsByUser(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        return eventRepository.findAllByInitiatorId(userId, pageable)
                .stream()
                .map(this::setStatisticViews)
                .map(this::getEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto updateEventByUser(int userId, UpdateEventRequest updateEventRequest) {
        Event event = modelMapper.map(updateEventRequest, Event.class);
        setCategoryStateInitiator(event, updateEventRequest.getCategory(), userId);
        event.setEventDate(LocalDateTime.parse(updateEventRequest.getEventDate(), formatter));
        return getEventFullDto(eventRepository.save(event));
    }

    public EventFullDto cancelEventByUser(int userId, int eventId) {
        Event innerEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new ObjectNotFoundException("Event not found"));
        innerEvent.setState(State.CANCELED);
        return getEventFullDto(eventRepository.save(innerEvent));
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

    public EventFullDto getEventFullDto(Event innerEvent) {
        EventFullDto eventFullDto = modelMapper.map(innerEvent, EventFullDto.class);
        eventFullDto.setCategory(modelMapper.map(innerEvent.getCategory(), CategoryDto.class));
        if (eventFullDto.getEventDate() != null)
            eventFullDto.setEventDate(innerEvent.getEventDate().format(formatter));
        if (innerEvent.getCreatedOn() != null) eventFullDto.setCreatedOn(innerEvent.getCreatedOn().format(formatter));
        if (innerEvent.getPublishedOn() != null)
            eventFullDto.setPublishedOn(innerEvent.getPublishedOn().format(formatter));
        if (innerEvent.getInitiator() != null)
            eventFullDto.setInitiator(modelMapper.map(innerEvent.getInitiator(), UserShortDto.class));
        if (innerEvent.getLat() != null || innerEvent.getLon() != null) {
            eventFullDto.setLocation(new Location(innerEvent.getLat(), innerEvent.getLon()));
        }
        if (innerEvent.getRating() != null) {
            double rating;
            int sum = 0;
            List<EventRating> list = innerEvent.getRating();
            for (EventRating eventRating : list) {
                sum += eventRating.getRating();
            }
            rating = (double) sum / list.size();
            eventFullDto.setRating(rating);
        }
        return eventFullDto;
    }

    public EventFullDto getEventByUser(int userId, int eventId) {
        return getEventFullDto(setStatisticViews(eventRepository.findEventByIdAndInitiatorId(eventId, userId)));
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
        Event event = modelMapper.map(adminUpdateEventRequest, Event.class);
        event.setCategory(categoryRepository.findById(adminUpdateEventRequest.getCategory()).get());
        event.setEventDate(LocalDateTime.parse(adminUpdateEventRequest.getEventDate(), formatter));
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
