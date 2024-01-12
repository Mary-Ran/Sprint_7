import DTO.CourierCreateRequest;
import DTO.CourierLoginRequest;
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
import static org.apache.http.HttpStatus.*;

public class LoginTest {

    String login;
    String password;
    String firstName;
    Integer courierId;

    private CourierSteps courierSteps;

    @Before
    public void setUp() {
        login = RandomStringUtils.randomAlphanumeric(10);
        password = RandomStringUtils.randomAlphanumeric(10);
        firstName = RandomStringUtils.randomAlphanumeric(10);
        courierSteps = new CourierSteps();
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void checkSuccessfulCourierAuth() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, password);

        courierSteps.courierCreate(courierCreateRequest);

        Response response = courierSteps.courierLogin(courierLoginRequest);
        checkResponseBodyIdAndStatusCode200(response);
    }

    @Test
    public void checkTheCourierAuthWithoutLogin() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setPassword(password);

        courierSteps.courierCreate(courierCreateRequest);

        Response response = courierSteps.courierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode400(response);
    }

    @Test
    public void checkTheCourierAuthWithoutPassword() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest();
        courierLoginRequest.setLogin(login);

        courierSteps.courierCreate(courierCreateRequest);

        Response response = courierSteps.courierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode400(response);

    }

    @Test
    public void checkTheCourierAuthWithIncorrectLogin() {
        String newLogin = RandomStringUtils.randomAlphanumeric(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(newLogin, password);

        courierSteps.courierCreate(courierCreateRequest);

        Response response = courierSteps.courierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode404(response);
    }

    @Test
    public void checkTheCourierAuthWithIncorrectPassword() {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);
        CourierLoginRequest courierLoginRequest = new CourierLoginRequest(login, newPassword);

        courierSteps.courierCreate(courierCreateRequest);

        Response response = courierSteps.courierLogin(courierLoginRequest);
        checkResponseBodyMessageAndStatusCode404(response);
    }

    @After
    public void deleteCourier() {
        courierId = courierSteps.courierLogin(new CourierLoginRequest(login, password))
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);
    }

    @Step("Check response body ID and status code 200")
    public void checkResponseBodyIdAndStatusCode200(Response response){
        response.then()
                .assertThat().body("id", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Step("Check response body MESSAGE and status code 400")
    public void checkResponseBodyMessageAndStatusCode400(Response response){
        response.then()
                .assertThat().body("message", is("Недостаточно данных для входа"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Step("Check response body MESSAGE and status code 404")
    public void checkResponseBodyMessageAndStatusCode404(Response response){
        response.then()
                .assertThat().body("message", is("Учетная запись не найдена"))
                .and()
                .statusCode(SC_NOT_FOUND);
    }

}

