package ewmstats.server;

import ewmstats.model.EndpointHit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Integer> {

    @Query("select count (en.id) from EndpointHit as en where en.timestamp between :start and :end and en.uri like :uri")
    Integer getViews(String start, String end, List<String> uri);

    @Query("select count (en.id) from EndpointHit as en where en.timestamp between :start and :end and" +
            " en.uri like :uri group by en.ip")
    Integer getViewsUnique(String start, String end, List<String> uri);

    EndpointHit findByUri(String uri);

}
