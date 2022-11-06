package ewmstats.server;

import ewmstats.model.EndpointHit;
import ewmstats.model.ViewStats;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
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

    @KafkaListener(
            topics = "clicks",
            containerFactory = "kafkaListenerContainerFactory")
    public void greetingListener(EndpointHit endpointHit) {
        statsService.saveHit(endpointHit);
    }
}
