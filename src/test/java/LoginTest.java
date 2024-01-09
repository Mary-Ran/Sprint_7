import DTO.CourierCreateRequest;
import DTO.CourierLoginRequest;
import groovy.xml.StreamingDOMBuilder;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;
import static config.RestConfig.BASE_URI;
import static org.hamcrest.Matchers.*;


public class LoginTest {

    private CourierSteps courierSteps;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
        courierSteps = new CourierSteps();
    }

    @Test
    public void checkSuccessfulCourierAuth() {
        String login = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);

        sendPostRequestCourier(courierCreateRequest);

        Response response = sendPostRequestCourierLogin(courierLoginRequest);
        checkResponseBodyIdAndStatusCode200(response);

        int courierId = courierSteps.courierLogin(courierLoginRequest)
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);
    }

    @Test
    public void checkTheCourierAuthWithoutLogin() {
        String login = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setPassword(password);

        sendPostRequestCourier(courierCreateRequest);

        Response response = sendPostRequestCourierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode400(response);

        int courierId = courierSteps.courierLogin(new CourierLoginRequest(login, password))
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);
    }

    @Test
    public void checkTheCourierAuthWithoutPassword() {
        String login = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);

        sendPostRequestCourier(courierCreateRequest);

        Response response = sendPostRequestCourierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode400(response);

        int courierId = courierSteps.courierLogin(new CourierLoginRequest(login, password))
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);
    }

    @Test
    public void checkTheCourierAuthWithIncorrectLogin() {
        String login_1 = RandomStringUtils.random(10);
        String login_2 = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login_1, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login_2, password);

        sendPostRequestCourier(courierCreateRequest);

        Response response = sendPostRequestCourierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode404(response);

        int courierId = courierSteps.courierLogin(new CourierLoginRequest(login_1, password))
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);

    }

    @Test
    public void checkTheCourierAuthWithIncorrectPassword() {
        String login = RandomStringUtils.random(10);
        String password_1 = RandomStringUtils.random(10);
        String password_2 = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password_1, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password_2);

        sendPostRequestCourier(courierCreateRequest);

        Response response = sendPostRequestCourierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode404(response);

        int courierId = courierSteps.courierLogin(new CourierLoginRequest(login, password_1))
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);
    }

    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(CourierCreateRequest courierCreateRequest){
        Response response = courierSteps.courierCreate(courierCreateRequest);
        return response;
    }

    @Step("Send POST request to /api/v1/courier/login")
    public Response sendPostRequestCourierLogin(CourierLoginRequest courierLoginRequest){
        Response response = courierSteps.courierLogin(courierLoginRequest);
        return response;
    }

    @Step("Check response body ID and status code 200")
    public void checkResponseBodyIdAndStatusCode200(Response response){
        response.then()
                .assertThat().body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Step("Check response body MESSAGE and status code 400")
    public void checkResponseBodyMessageAndStatusCode400(Response response){
        response.then()
                .assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(400);
    }

    @Step("Check response body MESSAGE and status code 404")
    public void checkResponseBodyMessageAndStatusCode404(Response response){
        response.then()
                .assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(404);
    }

}

