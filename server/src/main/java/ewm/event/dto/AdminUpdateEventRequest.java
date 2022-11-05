package ewm.event.dto;

import lombok.Data;

@Data
public class AdminUpdateEventRequest {
    private String annotation;
    private int category;
    private String description;
    private String eventDate;
    private boolean paid;
    private int participantLimit;
    private boolean requestModeration;
    private String title;
    private Location location;
}
