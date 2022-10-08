package ewm.event.repository;

import ewm.category.Category;
import ewm.event.Event;
import ewm.event.State;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("select e from Event e where lower(e.annotation) like lower(:text)  or lower(e.description)  like lower(:text) " +
            " and e.category in :category and e.eventDate between :rangeStart and :rangeEnd" +
            " and e.paid = :paid and e.state = :state ")
    List<Event> findAllEventsCategoryRange(String text,
                                           List<Category> category,
                                           LocalDateTime rangeStart,
                                           LocalDateTime rangeEnd,
                                           boolean paid,
                                           State state,
                                           Pageable pageable);

    List<Event> findAllByInitiatorId(int initiator, Pageable pageable);

    Event findEventByIdAndInitiatorId(int id, int initiator);

    List<Event> findAllByInitiatorIdInAndStateAndCategoryIdInAndEventDateBetween(List<Integer> initiator,
                                                                                 State state,
                                                                                 List<Integer> category,
                                                                                 LocalDateTime rangeStart,
                                                                                 LocalDateTime rangeEnd,
                                                                                 Pageable pageable);
}
