package ewm.event.controller;

import ewm.event.EventRating;
import ewm.event.service.RatingService;
import ewm.user.dto.UserShortDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/events")
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/{eventId}/rate")
    public EventRating createRating(@RequestParam int userId,
                                    @PathVariable int eventId,
                                    @RequestParam int rating) {
        return ratingService.saveRating(userId, eventId, rating);
    }

    @PutMapping("/{eventId}/rate")
    public EventRating updateRating(@RequestParam int userId,
                                    @PathVariable int eventId,
                                    @RequestParam int rating) {
        return ratingService.saveRating(userId, eventId, rating);
    }

    @DeleteMapping("/{eventId}/rate")
    public void deleteRating(@RequestParam int userId,
                             @PathVariable int eventId) {
        ratingService.deleteRating(userId, eventId);
    }

    @GetMapping("/users/rate")
    public List<UserShortDto> getUsersRating() {
        return ratingService.getUsersByRating();
    }
}
