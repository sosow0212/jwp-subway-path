package subway.dao.section;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("section_id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getLong("distance")
            );

    public List<SectionEntity> findSectionsByLineId(final Long lineId) {
        String sql = "SELECT section_id, line_id, up_station_id, down_station_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void deleteAllByLineId(final long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insertBatchSections(final List<SectionEntity> sectionEntities) {
        String sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) VALUES (?, ?, ?, ?)";
        List<Object[]> batchValues = new ArrayList<>();

        for (SectionEntity entity : sectionEntities) {
            Object[] values = new Object[]{
                    entity.getLineId(),
                    entity.getUpStationId(),
                    entity.getDownStationId(),
                    entity.getDistance()
            };
            batchValues.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchValues);
    }
}
