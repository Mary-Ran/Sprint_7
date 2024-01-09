package steps;

import DTO.CourierCreateRequest;
import DTO.CourierLoginRequest;
import DTO.OrderCreateRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    public Response courierCreate(CourierCreateRequest courierCreateRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierCreateRequest)
                .when()
                .post("/api/v1/courier");
    }

    public Response courierLogin(CourierLoginRequest courierLoginRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierLoginRequest)
                .when()
                .post("/api/v1/courier/login");
    }

    public Response orderCreate(OrderCreateRequest orderCreateRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(orderCreateRequest)
                .when()
                .post("/api/v1/orders");
    }

    public Response getOrders() {
        return given()
                .get("/api/v1/orders");
    }

    public Response deleteCourier(int courierId) {
        return given()
                .delete("/api/v1/courier/" + courierId);
    }

}
