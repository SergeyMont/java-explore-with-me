package ewm_stats.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "endpoints")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
