package subway.controller.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.line.LineCreateRequest;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.dto.station.StationCreateRequest;
import subway.service.LineService;
import subway.service.SectionService;
import subway.service.StationService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class SectionControllerIntegrationTest {

    @Autowired
    private StationService stationService;

    @Autowired
    private LineService lineService;

    @Autowired
    private SectionService sectionService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("Section을 생성한다.")
    void create_section_success() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineCreateRequest);

        StationCreateRequest stationCreateRequest1 = new StationCreateRequest("잠실역");
        StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
        stationService.saveStation(stationCreateRequest1);
        stationService.saveStation(stationCreateRequest2);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3L);

        // when & then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionCreateRequest)
                .when().post("/sections")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("Section을 삭제한다.")
    void delete_section_success() {
        // given
        LineCreateRequest lineCreateRequest = new LineCreateRequest("2호선", 2L, "초록색");
        lineService.saveLine(lineCreateRequest);

        StationCreateRequest stationCreateRequest1 = new StationCreateRequest("잠실역");
        StationCreateRequest stationCreateRequest2 = new StationCreateRequest("잠실새내역");
        stationService.saveStation(stationCreateRequest1);
        stationService.saveStation(stationCreateRequest2);

        SectionCreateRequest sectionCreateRequest = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3L);
        sectionService.insertSection(sectionCreateRequest);

        SectionDeleteRequest sectionDeleteRequest = new SectionDeleteRequest(2L, "잠실역");

        // when & then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(sectionDeleteRequest)
                .when().delete("/sections")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
