package account.repository;

import account.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Event, Long> {
    Optional<List<Event>> findAllByOrderByIdAsc();
}
