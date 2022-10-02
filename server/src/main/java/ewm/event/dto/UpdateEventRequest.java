package ewm.event.dto;

import lombok.Data;

import javax.validation.constraints.Size;


@Data
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    private int category;
    @Size(min = 20, max = 7000)
    private String description;
    private String eventDate;
    private int eventId;
    private boolean paid;
    private int participantLimit;
    @Size(min = 3, max = 120)
    private String title;
    private Location location;
}
