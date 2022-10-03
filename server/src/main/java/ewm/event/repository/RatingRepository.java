package ewm.event.repository;

import ewm.event.EventRating;
import ewm.event.EventRatingKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RatingRepository extends JpaRepository<EventRating, EventRatingKey> {
    @Query("select avg (e.rating) from EventRating e where e.event = ?1 ")
    Double getEventRating(int eventId);

    @Query("select avg (e.rating) from EventRating e where e.user = ?1")
    Double getUserRating(int userId);

}
