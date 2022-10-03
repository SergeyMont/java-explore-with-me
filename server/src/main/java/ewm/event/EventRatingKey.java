package ewm.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class EventRatingKey implements Serializable {
    @Column(name = "event_id")
    Integer eventId;
    @Column(name = "user_id")
    Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRatingKey that = (EventRatingKey) o;
        return eventId.equals(that.eventId) && userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, userId);
    }
}
