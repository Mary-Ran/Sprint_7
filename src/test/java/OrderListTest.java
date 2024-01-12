import DTO.OrderCreateRequest;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import static org.apache.http.HttpStatus.*;
import static config.RestConfig.BASE_URI;
import static org.hamcrest.Matchers.*;


public class OrderListTest {

    private OrderSteps orderSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        orderSteps = new OrderSteps();
    }

    @Test
    public void checkTheListOfOrdersInTheResponseBodyWithoutCourierId() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "2", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha");

        orderSteps.orderCreate(orderCreateRequest);

        Response response = orderSteps.getOrders();
        checkThatResponseBodyContainsOrdersListAndStatusCode200(response);
    }

    @Step("Check that response body contains orders list and status code 200")
    public void checkThatResponseBodyContainsOrdersListAndStatusCode200 (Response response) {
        response.then()
                .assertThat().body("orders.id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

}
