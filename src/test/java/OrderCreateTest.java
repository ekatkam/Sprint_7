import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(Parameterized .class)
public class OrderCreateTest {

    private final String[] color;

    public OrderCreateTest(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object [][] getColor() {
        return new Object[][] {
                {new String[] {"BLACK"}},
                {new String[] {"GREY"}},
                {new String[] {"BLACK", "GREY"}},
                {new String[] {}},
        };
    }

    private Order order;
    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getOrderData();
    }

    @Test
    public void checkCreateOrder() {
        setColor(color);
        ValidatableResponse response = orderClient.create(order);
        int responseStatusCode = getStatusCode(response);
        compareResponseStatusCodeWith201(responseStatusCode);
        int body = getResponseBody(response);
        checkBody(body);

    }


    @Step("Set color")
    public void setColor(String[] color) {
        order.setColor(color);
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
    public int getResponseBody(ValidatableResponse response) {
        int body = response.extract().path("track");
        return body;
    }

    @Step("Check that track != 0")
    public void checkBody(int body) {
        assertNotEquals(0, body);
    }

}
