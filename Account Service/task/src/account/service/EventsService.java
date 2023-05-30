package account.service;

import account.dto.EmptyJsonResponse;
import account.dto.EventsResponseDTO;
import account.enumeration.Events;
import account.model.Event;
import account.repository.EventsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class EventsService {

    @Autowired
    private EventsRepository eventsRepository;

    public Object getAllEvents(){
        Optional<List<Event>> events = eventsRepository.findAllByOrderByIdAsc();
        if(events.isPresent()){
            List<EventsResponseDTO> result = new ArrayList<>();
            for(Event event : events.get()){
                result.add(
                        new EventsResponseDTO(
                                event.getId(),
                                event.getDate(),
                                event.getAction(),
                                event.getSubject(),
                                event.getObject(),
                                event.getPath()
                        ));
            }
            return result;
        }
        return List.of(new EmptyJsonResponse());
    }

    public void saveEvent(Events action, String subject, String object, String path){
        Event event = new Event(
                null,
                LocalDateTime.now().toString(),
                action,
                subject == null ? "Anonymous" : subject,
                object,
                path
        );
        log.info(event.toString());
        eventsRepository.save(event);
    }
}
