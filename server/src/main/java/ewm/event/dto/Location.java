package ewm.event.dto;

import lombok.Data;

@Data
public class Location {
    private float lat;
    private float lon;

    public Location(float lat, float lon) {
        this.lat = lat;
        this.lon = lon;
    }
}
