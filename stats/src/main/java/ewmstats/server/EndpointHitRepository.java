package ewmstats.server;

import ewmstats.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("select count (en.id) from EndpointHit as en where en.timestamp between ?1 and ?2 and en.uri like ?3")
    Integer getViews(LocalDateTime start, LocalDateTime end, List<String> uri);

    @Query("select count (en.id) from EndpointHit as en where en.timestamp between ?1 and ?2 and en.uri like ?3 order by en.ip")
    Integer getViewsUnique(LocalDateTime start, LocalDateTime end, List<String> uri);

}
