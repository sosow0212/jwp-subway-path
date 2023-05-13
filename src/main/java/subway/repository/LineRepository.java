package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.line.LineDao;
import subway.dao.section.SectionDao;
import subway.dao.station.StationDao;
import subway.domain.subway.Line;
import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineRepository(final LineDao lineDao, final SectionDao sectionDao, final StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public Long insertLine(final LineEntity lineEntity) {
        return lineDao.insert(lineEntity);
    }

    public List<LineEntity> findAll() {
        return lineDao.findAll();
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

    public Line findByLineNameAndSections(final String lineName, final Sections sections) {
        LineEntity lineEntity = lineDao.findByName(lineName);
        return new Line(sections, lineEntity.getLineNumber(), lineEntity.getName(), lineEntity.getColor());
    }

    public void updateLine(final Sections sections, final long lineNumber) {
        LineEntity lineEntity = lineDao.findByLineNumber(lineNumber);
        List<SectionEntity> sectionEntities = sections.getSections().stream()
                .map(section -> {
                    Station upStation = section.getUpStation();
                    Station downStation = section.getDownStation();
                    StationEntity upStationEntity = stationDao.findByName(upStation.getName());
                    StationEntity downStationEntity = stationDao.findByName(downStation.getName());

                    return new SectionEntity(null, lineEntity.getLineId(), upStationEntity.getStationId(), downStationEntity.getStationId(), section.getDistance());
                })
                .collect(Collectors.toList());

        sectionDao.deleteAllByLineId(lineEntity.getLineId());
        sectionDao.insertBatchSections(sectionEntities);
    }
}
