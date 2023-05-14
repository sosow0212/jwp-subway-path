package subway.controller.station;

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
import subway.dto.station.StationCreateRequest;
import subway.dto.station.StationEditRequest;
import subway.dto.station.StationsResponse;
import subway.service.StationService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
class StationControllerIntegrationTest {

    @Autowired
    private StationService stationService;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("역을 생성한다.")
    void create_station_success() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
        long id = 1L;

        // when & then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationCreateRequest)
                .when().post("/stations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", "/stations/" + id);
    }

    @Test
    @DisplayName("모든 역을 조회한다.")
    void show_stations_success() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
        stationService.saveStation(stationCreateRequest);

        // when & then
        RestAssured
                .given()
                .when().get("/stations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("stations[0].id", equalTo(1))
                .body("stations[0].name", equalTo("잠실역"));
    }

    @Test
    @DisplayName("역을 조회한다.")
    void show_station_success() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
        stationService.saveStation(stationCreateRequest);

        // when & then
        RestAssured
                .given()
                .when().get("/stations/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(1))
                .body("name", equalTo("잠실역"));
    }

    @Test
    @DisplayName("역을 삭제한다.")
    void delete_station_success() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
        stationService.saveStation(stationCreateRequest);

        // when & then
        RestAssured
                .given()
                .when().delete("/stations/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        StationsResponse stations = stationService.findAllStationResponses();
        assertThat(stations.getStations().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("역을 수정한다.")
    void edit_station_success() {
        // given
        StationCreateRequest stationCreateRequest = new StationCreateRequest("잠실역");
        long id = stationService.saveStation(stationCreateRequest);

        StationEditRequest stationEditRequest = new StationEditRequest("판교역");

        // when & then
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(stationEditRequest)
                .when().patch("/stations/" + id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        StationsResponse stations = stationService.findAllStationResponses();
        assertThat(stations.getStations().get(0).getName()).isEqualTo(stationEditRequest.getName());
    }
}
