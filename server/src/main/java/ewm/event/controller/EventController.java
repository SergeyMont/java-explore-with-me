package ewm.event.controller;

import ewm.event.dto.AdminUpdateEventRequest;
import ewm.event.dto.EventFullDto;
import ewm.event.dto.NewEventDto;
import ewm.event.dto.UpdateEventRequest;
import ewm.event.service.EventClient;
import ewm.event.service.EventService;
import ewmstats.model.EndpointHit;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class EventController {
    private final EventService eventService;
    private final EventClient eventClient;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @GetMapping("/events")
    public List<EventFullDto> searchEvent(@RequestParam String text,
                                          @RequestParam List<Integer> categories,
                                          @RequestParam boolean paid,
                                          @RequestParam Optional<String> rangeStart,
                                          @RequestParam String rangeEnd,
                                          @RequestParam(defaultValue = "false") boolean onlyAvailable,
                                          @RequestParam Sort sort,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size,
                                          HttpServletRequest request) {
        if (rangeStart.isEmpty()) {
            rangeStart.orElse(LocalDateTime.now().format(formatter));
        }
        saveHit(request);
        return eventService.searchEvent(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    private void saveHit(HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit();
        endpointHit.setUri(request.getRequestURI());
        endpointHit.setApp("ewm-server");
        endpointHit.setIp(request.getRemoteAddr());
        endpointHit.setTimestamp(LocalDateTime.now().format(formatter));
        eventClient.saveHit(endpointHit);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable int id, HttpServletRequest request) {
        saveHit(request);
        return eventService.getEventById(id);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventFullDto> getAllEventsByUser(@PathVariable int userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        return eventService.getAllEventsByUser(userId, from, size);

    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEventByUser(@PathVariable int userId,
                                          @RequestBody UpdateEventRequest updateEventRequest) {
        return eventService.updateEventByUser(userId, updateEventRequest);

    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto createEventByUser(@PathVariable int userId,
                                          @RequestBody NewEventDto newEventDto) {
        return eventService.createEventByUser(userId, newEventDto);

    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable int userId,
                                       @PathVariable int eventId) {
        return eventService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable int userId,
                                    @PathVariable int eventId,
                                    @RequestBody UpdateEventRequest updateEventRequest) {
        return eventService.updateEventByUser(userId, updateEventRequest);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> searchEventAdmin(@RequestParam List<Integer> users,
                                               @RequestParam List<String> states,
                                               @RequestParam List<Integer> categories,
                                               @RequestParam String rangeStart,
                                               @RequestParam String rangeEnd,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return eventService.searchEventByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);

    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable int eventId,
                                         @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        return eventService.updateEventByAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable int eventId) {
        return eventService.publishEvent(eventId);
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable int eventId) {
        return eventService.rejectEvent(eventId);
    }
}
