package ewm.event.controller;

import ewm.event.EventRating;
import ewm.event.service.RatingService;
import ewm.user.dto.UserShortDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/users/rating")
    public EventRating createRating(@RequestParam int userId,
                                    @RequestParam int eventId,
                                    @RequestParam int rating) {
        return ratingService.saveRating(userId, eventId, rating);
    }

    @DeleteMapping("/users/rating")
    public void deleteRating(@RequestParam int userId,
                             @RequestParam int eventId) {
        ratingService.deleteRating(userId, eventId);
    }

    @GetMapping("users/rating")
    public List<UserShortDto> getUsersRating() {
        return ratingService.getUsersByRating();
    }
}
