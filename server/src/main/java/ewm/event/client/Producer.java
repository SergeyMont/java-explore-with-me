package ewm.event.client;

import ewm.event.dto.EndpointHitDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class Producer {
    private static final String TOPIC ="clicks";
    private KafkaTemplate<String, EndpointHitDto> kafkaTemplate;

    public void sendMessage (EndpointHitDto endpointHitDto){
        log.info("### Send message to Kafka Hit={}",endpointHitDto);
        kafkaTemplate.send(TOPIC,endpointHitDto);
    }

}
