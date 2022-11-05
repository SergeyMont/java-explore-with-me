package ewm.request.dto;

import ewm.event.Event;
import lombok.Data;

@Data
public class RequestEventDto {
    private int id;
    private Event event;
    private String created;
    private int requester;
    private String status;
}
