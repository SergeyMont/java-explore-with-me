package ewm.event.service;

import ewm.event.EventRating;
import ewm.event.EventRatingKey;
import ewm.event.repository.EventRepository;
import ewm.event.repository.RatingRepository;
import ewm.exceptions.BadConditionException;
import ewm.user.dto.UserShortDto;
import ewm.user.repository.UserRepository;
import ewm.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserService userService;

    public EventRating saveRating(int userId, int eventId, int rating) {
        if (rating < 0 || rating > 10) throw new BadConditionException("Рейтинг должен быть от 0 до 10");
        EventRating eventRating = new EventRating(
                new EventRatingKey(eventId, userId),
                eventRepository.findById(eventId).get(),
                userRepository.findById(userId).get(),
                rating
        );
        return ratingRepository.save(eventRating);
    }

    public void deleteRating(int userId, int eventId) {
        ratingRepository.deleteById(new EventRatingKey(eventId, userId));
    }

    public List<UserShortDto> getUsersByRating() {
        return userService.getAllUsers().stream()
                .peek(u -> u.setRating(ratingRepository.getUserRating(u.getId())))
                .collect(Collectors.toList());
    }
}
