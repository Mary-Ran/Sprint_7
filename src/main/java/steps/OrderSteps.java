package steps;

import DTO.OrderCreateRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static config.RestConfig.ORDER_ENDPOINT;
import static io.restassured.RestAssured.given;

public class OrderSteps {
    @Step("Send POST request to /api/v1/orders/")
    public Response orderCreate(OrderCreateRequest orderCreateRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderCreateRequest)
                .when()
                .post(ORDER_ENDPOINT);
    }

    @Step("Send GET request to /api/v1/orders/")
    public Response getOrders() {
        return given()
                .get(ORDER_ENDPOINT);
    }

}
