import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class OrderListTest {

    @Test
    public void checkOrderList() {
        OrderClient orderClient = new OrderClient();
        ValidatableResponse response = orderClient.getOrderList();
        int responseStatusCode = getStatusCode(response);
        compareResponseStatusCodeWith200(responseStatusCode);
        ArrayList<String> body = getBody(response);
        checkListSize(body);
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

    @Step("Extract array list")
    public ArrayList<String> getBody(ValidatableResponse response) {
        ArrayList<String> body = response.extract().path("orders");
        return body;
    }

    @Step("Check that list of orders is not empty")
    public void checkListSize(ArrayList<String> body) {
        assertNotEquals(0, body.size());
    }

}
