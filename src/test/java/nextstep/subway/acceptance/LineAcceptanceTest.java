package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("노선도 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineAcceptanceTest {
    @LocalServerPort
    int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }
    /**
     * when 지하철 노선을 생성하면
     * then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철노선 생성")
    @Test
    void createSubwayLine() {
        //given
        Map<String, String> upStation = new HashMap<>();
        upStation.put("name", "강남역");

        String upStationId = RestAssured.given().log().all()
                .body(upStation)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> downStation = new HashMap<>();
        downStation.put("name", "양재역");

        String downStationId = RestAssured.given().log().all()
                .body(downStation)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> lineParams = new HashMap<>();
        lineParams.put("name", "신분당선");
        lineParams.put("color", "bg-red-600");
        lineParams.put("upStationId", upStationId);
        lineParams.put("downStationId", downStationId);
        lineParams.put("distance", "10");

        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .body(lineParams).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();

        Assertions.assertAll(
                () -> assertThat(extract.jsonPath().getList("stations.name")).containsExactly("강남역", "양재역"),
                () -> assertThat(extract.jsonPath().getString("name")).contains("신분당선"),
                () -> assertThat(extract.jsonPath().getList("stations.id")).contains(1, 2),
                () -> assertThat(extract.response().statusCode()).isEqualTo(HttpStatus.CREATED.value())
        );

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록 조회")
    @Test
    void getSubwayLines() {
        Map<String, String> upSinbundangParam = new HashMap<>();
        upSinbundangParam.put("name", "강남역");

        String upSinbundagStationId = RestAssured.given().log().all()
                .body(upSinbundangParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> downSingbundangParam = new HashMap<>();
        downSingbundangParam.put("name", "양재역");

        String downSinbundangStationId = RestAssured.given().log().all()
                .body(downSingbundangParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> sinbundangLine = new HashMap<>();
        sinbundangLine.put("name", "신분당선");
        sinbundangLine.put("color", "bg-red-600");
        sinbundangLine.put("upStationId", upSinbundagStationId);
        sinbundangLine.put("downStationId", downSinbundangStationId);
        sinbundangLine.put("distance", "10");

        RestAssured
                .given().log().all()
                .body(sinbundangLine).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();




        Map<String, String> upSinlimParam = new HashMap<>();
        upSinlimParam.put("name", "신림역");

        String upSinlimStationId = RestAssured.given().log().all()
                .body(upSinlimParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> downSinlimParam = new HashMap<>();
        downSinlimParam.put("name", "당곡역");

        String downSinlimStationId = RestAssured.given().log().all()
                .body(downSinlimParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> sinlimLine = new HashMap<>();
        sinlimLine.put("name", "신림선");
        sinlimLine.put("color", "bg-red-600");
        sinlimLine.put("upStationId", upSinlimStationId);
        sinlimLine.put("downStationId", downSinlimStationId);
        sinlimLine.put("distance", "10");

        RestAssured
                .given().log().all()
                .body(sinlimLine).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();

        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();

        Assertions.assertAll(
                () -> assertThat(extract.jsonPath().getList("name")).contains("신분당선", "신림선"),
                () -> assertThat(extract.jsonPath().getList("station").size()).isEqualTo(2),
                () -> assertThat(extract.response().statusCode()).isEqualTo(HttpStatus.OK.value())
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철노선 조회")
    @Test
    void getSubwayLine() {
        Map<String, String> upSinlimParam = new HashMap<>();
        upSinlimParam.put("name", "신림역");

        String upSinlimStationId = RestAssured.given().log().all()
                .body(upSinlimParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> downSinlimParam = new HashMap<>();
        downSinlimParam.put("name", "당곡역");

        String downSinlimStationId = RestAssured.given().log().all()
                .body(downSinlimParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> sinlimLine = new HashMap<>();
        sinlimLine.put("name", "신림선");
        sinlimLine.put("color", "bg-red-600");
        sinlimLine.put("upStationId", upSinlimStationId);
        sinlimLine.put("downStationId", downSinlimStationId);
        sinlimLine.put("distance", "10");

        RestAssured
                .given().log().all()
                .body(sinlimLine).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();

        ExtractableResponse<Response> extract = RestAssured
                .given().log().all()
                .when().get("/lines/1")
                .then().log().all().extract();

        Assertions.assertAll(
                () -> assertThat(extract.jsonPath().getString("name")).isEqualTo("신림선"),
                () -> assertThat(extract.jsonPath().getList("stations").size()).isEqualTo(2),
                () -> assertThat(extract.jsonPath().getList("stations.name")).containsExactly("신림역","당곡역")
        );
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철노선 수정")
    @Test
    void updateSubwayLine() {
        Map<String, String> upSinlimParam = new HashMap<>();
        upSinlimParam.put("name", "신림역");

        String upSinlimStationId = RestAssured.given().log().all()
                .body(upSinlimParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> downSinlimParam = new HashMap<>();
        downSinlimParam.put("name", "당곡역");

        String downSinlimStationId = RestAssured.given().log().all()
                .body(downSinlimParam)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getString("id");

        Map<String, String> sinlimLine = new HashMap<>();
        sinlimLine.put("name", "신림선");
        sinlimLine.put("color", "bg-red-600");
        sinlimLine.put("upStationId", upSinlimStationId);
        sinlimLine.put("downStationId", downSinlimStationId);
        sinlimLine.put("distance", "10");

        //저장
        RestAssured
                .given().log().all()
                .body(sinlimLine).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all();

        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("name", "구미선");
        updateParam.put("color", "bg-blue-30000");

        //수정
        ExtractableResponse<Response> putExtract = RestAssured
                .given().log().all().body(updateParam).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/1")
                .then().log().all().extract();

        //수정된 데이터 출력
        ExtractableResponse<Response> getExtract = RestAssured
                .given().log().all()
                .when().get("/lines/1")
                .then().log().all().extract();
        assertThat(getExtract.jsonPath().getString("name")).isEqualTo("구미선");
        assertThat(getExtract.response().statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철노선 삭제")
    @Test
    void deleteSubwayLine() {
        
    }
}
