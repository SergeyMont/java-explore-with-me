package ewm.event.dto;

import ewm.event.Event;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event fromNewToEvent(NewEventDto newEventDto) {
        return new Event(newEventDto.getAnnotation(), null, 0, LocalDateTime.now(),
                newEventDto.getDescription(), LocalDateTime.parse(newEventDto.getEventDate(), formatter),
                null, null, newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon(),
                newEventDto.isPaid(), newEventDto.getParticipantLimit(), null,
                newEventDto.isRequestModeration(), null, newEventDto.getTitle(), null, null);
    }

    public static Event fromUpdateToEvent(UpdateEventRequest updateEventRequest) {
        return new Event(updateEventRequest.getAnnotation(), null, 0, null,
                updateEventRequest.getDescription(), LocalDateTime.parse(updateEventRequest.getEventDate(), formatter),
                null, null, updateEventRequest.getLocation().getLat(),
                updateEventRequest.getLocation().getLon(), updateEventRequest.isPaid(),
                updateEventRequest.getParticipantLimit(), null,
                null, null, updateEventRequest.getTitle(), null, null);
    }

    public static Event fromAdminToEvent(AdminUpdateEventRequest updateEventRequest) {
        return new Event(updateEventRequest.getAnnotation(), null, 0, null,
                updateEventRequest.getDescription(), LocalDateTime.parse(updateEventRequest.getEventDate(), formatter),
                null, null, updateEventRequest.getLocation().getLat(),
                updateEventRequest.getLocation().getLon(), updateEventRequest.isPaid(),
                updateEventRequest.getParticipantLimit(), null,
                updateEventRequest.isRequestModeration(), null, updateEventRequest.getTitle(), null, null);
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getAnnotation(), null, event.getConfirmedRequests(), null,
                event.getDescription(), null, event.getId(), null, event.getPaid(),
                event.getParticipantLimit(), null, event.getRequestModeration(), event.getState().name(),
                event.getTitle(), null, new Location(event.getLat(), event.getLon()), null);
    }
}
