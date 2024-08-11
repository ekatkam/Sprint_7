import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {

    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String LOGIN_PATH = "api/v1/courier/login";

    @Step("Create courier")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Delete courier")
    public ValidatableResponse delete(String courierId) {
        return given()
                .spec(getBaseSpec())
                .delete(COURIER_PATH + courierId)
                .then();
        }

    @Step("Login courier")
    public ValidatableResponse login(CourierCredentials courierCredentials) {
        return given()
                .spec(getBaseSpec())
                .body(courierCredentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }
}
