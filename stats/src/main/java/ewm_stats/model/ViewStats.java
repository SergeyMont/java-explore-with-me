package ewm_stats.model;

import lombok.Data;

@Data
public class ViewStats {
    private String app;
    private String uri;
    private int hits;
}
