package ewm.event.dto;

import ewm.event.Event;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event fromNewToEvent(NewEventDto newEventDto) {
        return new Event(newEventDto.getAnnotation(), null, null, LocalDateTime.now(),
                newEventDto.getDescription(), LocalDateTime.parse(newEventDto.getEventDate()),
                null, null, newEventDto.getLocation().getLat(), newEventDto.getLocation().getLon(),
                newEventDto.isPaid(), newEventDto.getParticipantLimit(), null,
                newEventDto.isRequestModeration(), null, newEventDto.getTitle(), null);
    }

    public static Event fromUpdateToEvent(UpdateEventRequest updateEventRequest) {
        return new Event(updateEventRequest.getAnnotation(), null, null, null,
                updateEventRequest.getDescription(), LocalDateTime.parse(updateEventRequest.getEventDate()), null,
                null, updateEventRequest.getLocation().getLat(), updateEventRequest.getLocation().getLon(),
                updateEventRequest.isPaid(), updateEventRequest.getParticipantLimit(), null,
                null, null, updateEventRequest.getTitle(), null);
    }

    public static Event fromAdminToEvent(AdminUpdateEventRequest updateEventRequest) {
        return new Event(updateEventRequest.getAnnotation(), null, null, null,
                updateEventRequest.getDescription(), LocalDateTime.parse(updateEventRequest.getEventDate()), null,
                null, updateEventRequest.getLocation().getLat(), updateEventRequest.getLocation().getLon(),
                updateEventRequest.isPaid(), updateEventRequest.getParticipantLimit(), null,
                updateEventRequest.isRequestModeration(), null, updateEventRequest.getTitle(), null);
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getAnnotation(), null, event.getConfirmedRequests(), null,
                event.getDescription(), null, event.getId(), null, event.getPaid(),
                event.getParticipantLimit(), null, event.getRequestModeration(), event.getState().name(),
                event.getTitle(), event.getViews(), new Location(event.getLat(), event.getLon()));
    }
}
