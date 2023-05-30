package account.dto;

import account.enumeration.Events;
import lombok.Data;
@Data
public class EventsResponseDTO {

    private Long id;
    private String date;
    private Events action;
    private String subject;
    private String object;
    private String path;

    public EventsResponseDTO(Long id, String date, Events action, String subject, String object, String path) {
        this.id = id;
        this.date = date;
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = path;
    }
}
