package ewmstats.server;

import ewmstats.model.EndpointHit;
import ewmstats.model.ViewStats;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StatsService {
    private final EndpointHitRepository endpointHitRepository;

    public void saveHit(EndpointHit endpointHit) {
        endpointHitRepository.save(endpointHit);
    }

    public ViewStats getStats(String start, String end, List<String> uri, boolean unique) {
        Optional<Integer> count;
        if (unique) count = Optional.of(endpointHitRepository.getViewsUnique(start, end, uri));
        else count = Optional.of(endpointHitRepository.getViews(start, end, uri));
        ViewStats viewStats = new ViewStats();
        viewStats.setApp(endpointHitRepository.findByUri(uri.get(0)).getApp());
        viewStats.setUri(uri.get(0));
        viewStats.setHits(count.orElse(0));
        return viewStats;
    }
}
