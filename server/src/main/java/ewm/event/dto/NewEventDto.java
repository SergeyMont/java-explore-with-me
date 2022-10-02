package ewm.event.dto;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.Size;

@Data
public class NewEventDto {
    @Size(min = 20, max = 2000)
    private String annotation;
    private int category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private Location location;
    private boolean paid;
    @Value("0")
    private int participantLimit;
    @Value("true")
    private boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;
}
