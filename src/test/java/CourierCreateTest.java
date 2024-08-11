import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class CourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getCourierData();
    }


    @Test
    @DisplayName("Courier create - positive")
    @Description("Checking successful status code and response body after creating a courier with correct data")
    public void courierCreate() {
        ValidatableResponse response = courierClient.create(courier);
        int responseStatusCode = getStatusCode(response);
        compareResponseStatusCodeWith201(responseStatusCode);
        boolean body = getResponseBody(response);
        assertBody(body);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginResponseStatusCode = getStatusCode(loginResponse);
        compareResponseStatusCodeWith200(loginResponseStatusCode);
        int courierId = getCourierId(loginResponse);
        assertCourierId(courierId);

        String courierIdStr = String.valueOf(courierId);
        ValidatableResponse deleteResponse = courierClient.delete(courierIdStr);
        int deleteResponseStatusCode = getStatusCode(deleteResponse);
        compareResponseStatusCodeWith200(deleteResponseStatusCode);
        boolean deleteBody = getResponseBody(deleteResponse);
        assertBody(deleteBody);
    }

    @Test
    @DisplayName("Create the same courier twice")
    @Description("Checking that it is impossible to create two same couriers")
    public void courierCreateTwice() {
        courier.setLogin("sameLogin");
        courierClient.create(courier);
        ValidatableResponse response = courierClient.create(courier);
        int responseStatusCode = getStatusCode(response);
        compareResponseStatusCodeWith409(responseStatusCode);
        String message = getResponseMessage(response);
        compareResponseMessageWithExpectedFor409(message);

    }

    @Test
    @DisplayName("Create courier without login")
    @Description("Checking that it is impossible to create courier without login")
    public void courierCreateWithoutLogin() {
        courier.setLogin("");
        ValidatableResponse response = courierClient.create(courier);
        int responseStatusCode = getStatusCode(response);
        compareResponseStatusCodeWith400(responseStatusCode);
        String message = getResponseMessage(response);
        compareResponseMessageWithExpectedFor400(message);
    }

    @Test
    @DisplayName("Create courier without password")
    @Description("Checking that it is impossible to create courier without password")
    public void courierCreateWithoutPassword() {
        courier.setPassword("");
        ValidatableResponse response = courierClient.create(courier);
        int responseStatusCode = getStatusCode(response);
        compareResponseStatusCodeWith400(responseStatusCode);
        String message = getResponseMessage(response);
        compareResponseMessageWithExpectedFor400(message);
    }

    @Test
    @DisplayName("Create courier without first name")
    @Description("Checking successful status code and response body after creating a courier without first name")
    public void courierCreateWithoutFirstName() {
        courier.setFirstName("");
        ValidatableResponse response = courierClient.create(courier);
        int responseStatusCode = getStatusCode(response);
        compareResponseStatusCodeWith201(responseStatusCode);
        boolean body = getResponseBody(response);
        assertBody(body);

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginResponseStatusCode = getStatusCode(loginResponse);
        compareResponseStatusCodeWith200(loginResponseStatusCode);
        int courierId = getCourierId(loginResponse);
        assertCourierId(courierId);

        String courierIdStr = String.valueOf(courierId);
        ValidatableResponse deleteResponse = courierClient.delete(courierIdStr);
        int deleteResponseStatusCode = getStatusCode(deleteResponse);
        compareResponseStatusCodeWith200(deleteResponseStatusCode);
        boolean deleteBody = getResponseBody(deleteResponse);
        assertBody(deleteBody);
    }

    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse response) {
        int responseStatusCode = response.extract().statusCode();
        return responseStatusCode;
    }

    @Step("Compare response status code with successful status code")
    public void compareResponseStatusCodeWith201(int responseStatusCode) {
        assertEquals(201, responseStatusCode);
    }

    @Step("Get response body")
    public boolean getResponseBody(ValidatableResponse response) {
        boolean body = response.extract().path("ok");
        return body;
    }

    @Step("Check that ok == true")
    public void assertBody(boolean body) {
        assertTrue(body);
    }

    @Step("Compare response status code with successful status code")
    public void compareResponseStatusCodeWith200(int responseStatusCode) {
        assertEquals(200, responseStatusCode);
    }

    @Step("Get courierId")
    public int getCourierId(ValidatableResponse response) {
        int courierId = response.extract().path("id");
        return courierId;
    }

    @Step("Check that courierId has value")
    public void assertCourierId(int courierId) {
        assertNotEquals(0, courierId);
    }

    @Step("Compare response status code with expected")
    public void compareResponseStatusCodeWith409(int responseStatusCode) {
        assertEquals(409, responseStatusCode);
    }

    @Step("Extract error message")
    public String getResponseMessage(ValidatableResponse response) {
        String message = response.extract().path("message");
        return message;
    }

    @Step("Compare message with expected")
    public void compareResponseMessageWithExpectedFor409(String message) {
        assertEquals("Этот логин уже используется", message);
    }

    @Step("Compare response status code with expected")
    public void compareResponseStatusCodeWith400(int responseStatusCode) {
        assertEquals(400, responseStatusCode);
    }

    @Step("Compare message with expected")
    public void compareResponseMessageWithExpectedFor400(String message) {
        assertEquals("Недостаточно данных для создания учетной записи", message);
    }

}
