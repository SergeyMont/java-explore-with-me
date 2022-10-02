package ewm_stats.server;

import ewm_stats.model.EndpointHit;
import ewm_stats.model.ViewStats;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsService {
    private final EndpointHitRepository endpointHitRepository;

    public void saveHit(EndpointHit endpointHit) {
        endpointHitRepository.save(endpointHit);
    }

    public ViewStats getStats(String start, String end, List<String> uri, boolean unique) {
        int count;
        if (unique)
            count = endpointHitRepository.getViewsUnique(LocalDateTime.parse(start), LocalDateTime.parse(end), uri);
        else count = endpointHitRepository.getViews(LocalDateTime.parse(start), LocalDateTime.parse(end), uri);
        ViewStats viewStats = new ViewStats();
        viewStats.setUri(uri.get(0));
        viewStats.setHits(count);
        return viewStats;
    }
}
