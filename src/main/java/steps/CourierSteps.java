package steps;

import DTO.CourierCreateRequest;
import DTO.CourierLoginRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static config.RestConfig.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Send POST request to /api/v1/courier/")
    public Response courierCreate(CourierCreateRequest courierCreateRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierCreateRequest)
                .when()
                .post(COURIER_ENDPOINT);
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response courierLogin(CourierLoginRequest courierLoginRequest) {
        return given()
                .contentType(ContentType.JSON)
                .body(courierLoginRequest)
                .when()
                .post(LOGIN_ENDPOINT);
    }

    @Step("Send DELETE request to /api/v1/courier/courierId")
    public Response deleteCourier(int courierId) {
        return given()
                .delete(COURIER_ENDPOINT + courierId);
    }

}
