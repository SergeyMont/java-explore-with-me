package ewm.event;

import ewm.category.Category;
import ewm.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    private String annotation;
    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;
    private int confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "initiator_id")
    @ManyToOne
    private User initiator;
    private Float lat;
    private Float lon;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private State state;
    private String title;
    private Integer views;
    @OneToMany(mappedBy = "event")
    private Set<EventRating> ratings;
}
