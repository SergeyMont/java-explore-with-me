package ewm.request.controller;

import ewm.request.dto.ParticipationRequestDto;
import ewm.request.service.RequestService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getAllByUserId(@PathVariable Integer userId) {
        return requestService.getAllRequestByUser(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequest(@PathVariable Integer userId,
                                                 @RequestParam Integer eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Integer userId,
                                                 @PathVariable Integer requestId) {
        return requestService.cancelRequest(requestId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllByUserAndEvent(@PathVariable Integer userId,
                                                              @PathVariable Integer eventId) {
        return requestService.getAllByUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Integer userId,
                                                  @PathVariable Integer eventId,
                                                  @PathVariable Integer reqId) {
        return requestService.confirmRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Integer userId,
                                                 @PathVariable Integer eventId,
                                                 @PathVariable Integer reqId) {
        return requestService.cancelRequest(userId, eventId, reqId);

    }
}
