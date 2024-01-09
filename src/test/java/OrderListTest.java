import DTO.OrderCreateRequest;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;
import static config.RestConfig.BASE_URI;
import static org.hamcrest.Matchers.*;


public class OrderListTest {

    private CourierSteps courierSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierSteps = new CourierSteps();
    }

    @Test
    public void checkTheListOfOrdersInTheResponseBodyWithoutCourierId() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "2", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha");

        sendPostRequestOrders(orderCreateRequest);

        Response response = sendGetRequestOrders();
        checkThatResponseBodyContainsOrdersListAndStatusCode200(response);
    }

    @Step("Send POST request to /api/v1/orders")
    public Response sendPostRequestOrders(OrderCreateRequest orderCreateRequest){
        Response response = courierSteps.orderCreate(orderCreateRequest);
        return response;
    }

    @Step("Send GET request to /api/v1/orders")
    public Response sendGetRequestOrders(){
        Response response = courierSteps.getOrders();
        return response;
    }

    @Step("Check that response body contains orders list and status code 200")
    public void checkThatResponseBodyContainsOrdersListAndStatusCode200 (Response response) {
        response.then()
                .assertThat().body("orders.id", notNullValue())
                .and()
                .statusCode(200);
    }

}
