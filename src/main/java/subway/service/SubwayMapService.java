package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.subway.LineMap;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.dto.station.LineMapResponse;
import subway.dto.station.StationResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SubwayMapService(final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional(readOnly = true)
    public LineMapResponse showLineMap(final Long lineNumber) {
        Sections sections = sectionRepository.findSectionsByLineNumber(lineNumber);
        LineMap lineMap = new LineMap(sections);

        List<Station> orderedStations = lineMap.getOrderedStations(sections);
        List<StationResponse> stations = orderedStations.stream()
                .map(station -> {
                    Long stationId = stationRepository.findStationIdByStationName(station.getName());
                    return StationResponse.from(stationId, station);
                })
                .collect(Collectors.toList());

        return LineMapResponse.from(stations);
    }
}
