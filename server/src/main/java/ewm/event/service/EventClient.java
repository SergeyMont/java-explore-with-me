package ewm.event.service;

import ewm.event.client.BaseClient;
import ewm.event.dto.EndpointHitDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class EventClient extends BaseClient {

    public EventClient(RestTemplate rest) {
        super(rest);
    }

    public ResponseEntity<Object> getViews(String start, String end, List<String> uris, boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("stats?start={start}&end={end}&uris={uris}&unique={unique}", parameters);
    }

    public void saveHit(EndpointHitDto endpointHit) {
        post("hit", endpointHit);
    }
}
