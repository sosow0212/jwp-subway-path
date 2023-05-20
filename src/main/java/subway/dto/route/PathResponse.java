package subway.dto.route;

import subway.domain.subway.Station;
import subway.dto.station.StationResponse;

import java.util.Set;

public class PathResponse {

    private final StationResponse station;
    private final Set<String> lineNames;

    public PathResponse(final StationResponse station, final Set<String> lineNames) {
        this.station = station;
        this.lineNames = lineNames;
    }

    public static PathResponse from(final Station station, final Set<String> lineName) {
        return new PathResponse(StationResponse.from(station), lineName);
    }

    public StationResponse getStation() {
        return station;
    }

    public Set<String> getLineNames() {
        return lineNames;
    }
}
