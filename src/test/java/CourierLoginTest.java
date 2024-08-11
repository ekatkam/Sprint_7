import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CourierLoginTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierGenerator.getCourierData();
        ValidatableResponse response = courierClient.create(courier);
        int responseStatusCode = response.extract().statusCode();
        assertEquals(201, responseStatusCode);
        boolean body = response.extract().path("ok");
        assertTrue(body);
    }

    @After
    public void cleanUp() {
        courier = CourierGenerator.getCourierData();
        ValidatableResponse correctLoginResponse = courierClient.login(CourierCredentials.from(courier));
        int correctLoginResponseStatusCode = correctLoginResponse.extract().statusCode();
        assertEquals(200, correctLoginResponseStatusCode);
        courierId = correctLoginResponse.extract().path("id");
        assertNotEquals(0, courierId);

        String courierIdStr = String.valueOf(courierId);
        ValidatableResponse deleteResponse = courierClient.delete(courierIdStr);
        int deleteResponseStatusCode = deleteResponse.extract().statusCode();
        assertEquals(200, deleteResponseStatusCode);
        boolean deleteBody = deleteResponse.extract().path("ok");
        assertTrue(deleteBody);
    }


    @Test
    @DisplayName("Courier login - positive")
    @Description("Checking successful login with correct data")
    public void courierLogin() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginResponseStatusCode = getStatusCode(loginResponse);
        compareResponseStatusCodeWith200(loginResponseStatusCode);
        int courierId = getCourierId(loginResponse);
        assertCourierId(courierId);
    }

    @Test
    @DisplayName("Courier login without login")
    @Description("Checking that it is impossible to login without login")
    public void courierLoginWithoutLogin() {
        setEmptyLogin();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginResponseStatusCode = getStatusCode(loginResponse);
        compareResponseStatusCodeWith400(loginResponseStatusCode);
        String message = getResponseMessage(loginResponse);
        compareResponseMessageWithExpectedFor400(message);
    }

    @Test
    @DisplayName("Courier login without password")
    @Description("Checking that it is impossible to login without password")
    public void courierLoginWithoutPassword() {
        setEmptyPassword();
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginResponseStatusCode = getStatusCode(loginResponse);
        compareResponseStatusCodeWith400(loginResponseStatusCode);
        String message = getResponseMessage(loginResponse);
        compareResponseMessageWithExpectedFor400(message);

    }

    @Test
    @DisplayName("Courier login with incorrect credentials")
    @Description("Checking that it is impossible to login with incorrect credentials")
    public void courierLoginWithIncorrectCredentials() {
        ValidatableResponse loginResponse = courierClient.login(new CourierCredentials("random","random"));
        int loginResponseStatusCode = getStatusCode(loginResponse);
        compareResponseStatusCodeWith404(loginResponseStatusCode);
        String message = getResponseMessage(loginResponse);
        compareResponseMessageWithExpectedFor404(message);

    }

    @Step("Get response status code")
    public int getStatusCode(ValidatableResponse response) {
        int responseStatusCode = response.extract().statusCode();
        return responseStatusCode;
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

    @Step("Compare response status code with successful status code")
    public void compareResponseStatusCodeWith400(int responseStatusCode) {
        assertEquals(400, responseStatusCode);
    }

    @Step("Extract error message")
    public String getResponseMessage(ValidatableResponse response) {
        String message = response.extract().path("message");
        return message;
    }

    @Step("Compare message with expected")
    public void compareResponseMessageWithExpectedFor400(String message) {
        assertEquals("Недостаточно данных для входа", message);
    }

    @Step("Compare message with expected")
    public void compareResponseMessageWithExpectedFor404(String message) {
        assertEquals("Учетная запись не найдена", message);
    }

    @Step("Set empty login")
    public void setEmptyLogin() {
        courier.setLogin("");
    }

    @Step("Set empty password")
    public void setEmptyPassword() {
        courier.setPassword("");
    }

    @Step("Compare response status code with successful status code")
    public void compareResponseStatusCodeWith404(int responseStatusCode) {
        assertEquals(404, responseStatusCode);
    }
}


