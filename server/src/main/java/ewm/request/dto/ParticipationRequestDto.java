package ewm.request.dto;

import lombok.Data;

@Data
public class ParticipationRequestDto {
    private int id;
    private int event;
    private String created;
    private int requester;
    private String status;
}
