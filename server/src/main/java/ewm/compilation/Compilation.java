package ewm.compilation;

import ewm.event.Event;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Compilation {
    @Id
    private int id;
    private boolean pinned;
    private String title;
    @OneToMany
    private List<Event> events;
}
