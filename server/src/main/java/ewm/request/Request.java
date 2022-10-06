package ewm.request;

import ewm.event.Event;
import ewm.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table (name = "requests")
public class Request {
    @Id
    private int id;
    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;
    private LocalDateTime created;
    @JoinColumn(name = "requester_id")
    @ManyToOne
    private User requester;
    @Enumerated(EnumType.STRING)
    private Status status;
}
