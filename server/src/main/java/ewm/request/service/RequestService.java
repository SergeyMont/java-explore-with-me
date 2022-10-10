package ewm.request.service;

import ewm.event.Event;
import ewm.event.State;
import ewm.event.repository.EventRepository;
import ewm.event.service.EventService;
import ewm.exceptions.AccessException;
import ewm.exceptions.BadConditionException;
import ewm.exceptions.ConflictException;
import ewm.exceptions.ObjectNotFoundException;
import ewm.request.Request;
import ewm.request.Status;
import ewm.request.dto.ParticipationRequestDto;
import ewm.request.dto.RequestEventDto;
import ewm.request.repository.RequestRepository;
import ewm.user.User;
import ewm.user.repository.UserRepository;
import ewm.user.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository repository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventService eventService;

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional
    public RequestEventDto createRequest(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        if (!userService.isUserCreated(userId)) throw new ObjectNotFoundException("Пользователь не найден");
        if (Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ConflictException("Создатель мероприятия не может быть участником");
        }
        if (event.getState() != State.PUBLISHED) {
            throw new BadConditionException("Событие уже опубликовано!");
        }
        if (event.getParticipantLimit() < event.getConfirmedRequests()) {
            throw new AccessException("Количество подтвержденных участников - максимальное");
        }
        Request request = new Request();
        request.setRequester(user);
        request.setCreated(LocalDateTime.now());
        request.setStatus(Status.PENDING);
        request.setEvent(event);
        return toRequestEventDto(repository.save(request));
    }

    public List<ParticipationRequestDto> getAllRequestByUser(int userId) {
        return repository.findAllByRequesterId(userId).stream()
                .map(this::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(int requestId) {
        Request request = repository.findById(requestId).orElse(null);
        assert request != null;
        request.setStatus(Status.CANCELED);
        repository.save(request);
        return toRequestDto(request);
    }

    public List<RequestEventDto> getAllByUserEvent(int userId, int eventId) {
        return repository.findAllByEventId(eventId).stream()
                .map(this::toRequestEventDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto confirmRequest(int userId, int eventId, int reqId) {
        if (!userService.isUserCreated(userId)) throw new ObjectNotFoundException("Пользователь не найден");
        Event event = eventRepository.findById(eventId).orElseThrow();
        if (event.getConfirmedRequests() < event.getParticipantLimit()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            if (event.getConfirmedRequests() == event.getParticipantLimit()) {
                for (Request cancelEvent : repository.findAllByEventId(eventId)) {
                    cancelEvent.setStatus(Status.REJECTED);
                }
            }
            Request request = repository.findById(reqId).orElseThrow(() -> new ObjectNotFoundException("Запрос не найден"));
            request.setStatus(Status.CONFIRMED);
            repository.save(request);
            return toRequestDto(request);
        } else throw new AccessException("Достигнуто максимальное количество участников мероприятия");
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(int userId, int eventId, int reqId) {
        if (!userService.isUserCreated(userId)) throw new ObjectNotFoundException("Пользователь не найден");
        Request request = new Request();
        eventRepository.findById(eventId).orElseThrow();
        if (repository.findById(reqId).isPresent()) {
            request = repository.findById(reqId).get();
            request.setStatus(Status.REJECTED);
            repository.save(request);
        } else {
            throw new ObjectNotFoundException("Запрос не найден");
        }
        return toRequestDto(request);
    }

    private Request toRequest(ParticipationRequestDto requestDto) {
        Request request = modelMapper.map(requestDto, Request.class);
        request.setCreated(LocalDateTime.now());
        request.setStatus(Status.PENDING);
        return request;
    }

    private ParticipationRequestDto toRequestDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setRequester(request.getRequester().getId());
        dto.setCreated(request.getCreated().format(formatter));
        dto.setStatus(request.getStatus().name());
        dto.setEvent(request.getEvent().getId());
        dto.setId(request.getId());
        return dto;
    }

    private RequestEventDto toRequestEventDto(Request request) {
        RequestEventDto requestEventDto = new RequestEventDto();
        requestEventDto.setRequester(request.getRequester().getId());
        requestEventDto.setCreated(request.getCreated().format(formatter));
        requestEventDto.setStatus(request.getStatus().name());
        requestEventDto.setEvent(request.getEvent());
        requestEventDto.setId(request.getId());
        return requestEventDto;
    }
}
