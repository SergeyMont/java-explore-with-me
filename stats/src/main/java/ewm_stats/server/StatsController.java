package ewm_stats.server;

import ewm_stats.model.EndpointHit;
import ewm_stats.model.ViewStats;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public ViewStats getViews(@RequestParam String start,
                              @RequestParam String end,
                              @RequestParam List<String> uris,
                              @RequestParam boolean unique) {
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public void saveHit(@RequestBody EndpointHit endpointHit) {
        statsService.saveHit(endpointHit);
    }
}
