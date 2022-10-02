package ewm.event.repository;

import ewm.category.Category;
import ewm.event.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query("select e from Event e where lower(e.annotation) like lower(?1)  or lower(e.description)  like lower(?1) " +
            " and e.category in ?2 and e.eventDate between ?3 and ?4 and e.paid = ?5 order by e.eventDate")
    List<Event> findAllByAnnotationDescriptionCategoryEventDateBetweenPaidOrderByEventDate(String text,
                                                                                           List<Category> category,
                                                                                           LocalDateTime rangeStart,
                                                                                           LocalDateTime rangeEnd,
                                                                                           boolean paid,
                                                                                           Pageable pageable);

    @Query("select e from Event e where lower(e.annotation) like lower(?1)  or lower(e.description)  like lower(?1) " +
            " and e.category in ?2 and e.eventDate between ?3 and ?4 and e.paid = ?5 order by e.views")
    List<Event> findAllByAnnotationDescriptionCategoryEventDateBetweenPaidOrderByViews(String text,
                                                                                       List<Category> category,
                                                                                       LocalDateTime rangeStart,
                                                                                       LocalDateTime rangeEnd,
                                                                                       boolean paid,
                                                                                       Pageable pageable);

    List<Event> findAllByInitiator(int initiator, Pageable pageable);

    Event findEventByIdAndInitiator(int id, int initiator);

    List<Event> findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateBetween(List<Integer> initiator,
                                                                                   List<String> state,
                                                                                   List<Integer> category,
                                                                                   LocalDateTime rangeStart,
                                                                                   LocalDateTime rangeEnd,
                                                                                   Pageable pageable);
}
