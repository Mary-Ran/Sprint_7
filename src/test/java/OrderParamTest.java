import DTO.CourierCreateRequest;
import DTO.OrderCreateRequest;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.CourierSteps;
import java.util.List;
import static config.RestConfig.BASE_URI;
import static org.hamcrest.Matchers.*;

@RunWith(Parameterized.class)
public class OrderParamTest {
    private final List<String> color;
    private CourierSteps courierSteps;

    public OrderParamTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[] getColor() {
        return new Object[]{
                List.of("BLACK"),
                List.of("GREY"),
                List.of("BLACK", "GREY"),
                List.of(),
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierSteps = new CourierSteps();
    }

    @Test
    public void checkSuccessfulOrderCreationWithColor() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest("Naruto", "Uchiha", "Konoha, 142 apt.", "2", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha");
        orderCreateRequest.setColor(color);

        Response response = sendPostRequestOrders(orderCreateRequest);
        checkResponseBodyTrackAndStatusCode201(response);

    }

    @Step("Send POST request to /api/v1/orders")
    public Response sendPostRequestOrders(OrderCreateRequest orderCreateRequest){
        Response response = courierSteps.orderCreate(orderCreateRequest);
        return response;
    }

    @Step("Check response body TRACK and status code 201")
    public void checkResponseBodyTrackAndStatusCode201(Response response){
        response.then()
                .assertThat().body("track", notNullValue())
                .and()
                .statusCode(201);
    }

}
