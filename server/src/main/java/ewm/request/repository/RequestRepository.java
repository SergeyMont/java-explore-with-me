package ewm.request.repository;

import ewm.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {

    List<Request> findAllByRequesterId(Integer userId);

    List<Request> findAllByRequesterIdAndEventId(int userId, int eventId);
}
