import DTO.CourierCreateRequest;
import DTO.CourierLoginRequest;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import steps.CourierSteps;
import static config.RestConfig.BASE_URI;
import static org.hamcrest.Matchers.*;

public class CourierTest {

    private CourierSteps courierSteps;

    @Before
    public void SetUp() {
        RestAssured.baseURI = BASE_URI;
        courierSteps = new CourierSteps();
    }

    @Test
    public void checkSuccessfulCourierCreation() {
        String login = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);

        Response response = sendPostRequestCourier(courierCreateRequest);
        checkResponseBodyIsOkAndStatusCode201(response);

        int courierId = courierSteps.courierLogin(new CourierLoginRequest(login, password))
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);

    }

    @Test
    public void checkTheCreationOfTwoIdenticalCouriers() {
        String login = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);

        sendPostRequestCourier(courierCreateRequest);

        Response response = sendPostRequestCourier(courierCreateRequest);
        checkResponseBodyMessageAndStatusCode409(response);

    }

    @Test
    public void checkTheCreationOfTwoCouriersWithTheSameLogins() {
        String login = RandomStringUtils.random(10);
        String password_1 = RandomStringUtils.random(10);
        String password_2 = RandomStringUtils.random(10);
        String firstName_1 = RandomStringUtils.random(10);
        String firstName_2 = RandomStringUtils.random(10);

        CourierCreateRequest firstCourier = new CourierCreateRequest(login, password_1, firstName_1);
        CourierCreateRequest secondCourier = new CourierCreateRequest(login, password_2, firstName_2);

        sendPostRequestCourier(firstCourier);

        Response response = sendPostRequestCourier(secondCourier);
        checkResponseBodyMessageAndStatusCode409(response);

    }

    @Test
    public void checkTheCreationOfACourierWithoutLogin() {
        String password = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setPassword(password);
        courierCreateRequest.setFirst_name(firstName);

        Response response = sendPostRequestCourier(courierCreateRequest);
        checkResponseBodyMessageAndStatusCode400(response);

    }

    @Test
    public void checkTheCreationOfACourierWithoutPassword() {
        String login = RandomStringUtils.random(10);
        String firstName = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setFirst_name(firstName);

        Response response = sendPostRequestCourier(courierCreateRequest);
        checkResponseBodyMessageAndStatusCode400(response);

    }

    @Test
    public void checkTheCreationOfACourierWithoutFirstName() {
        String login = RandomStringUtils.random(10);
        String password = RandomStringUtils.random(10);

        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setPassword(password);

        Response response = sendPostRequestCourier(courierCreateRequest);
        checkResponseBodyIsOkAndStatusCode201(response);

        int courierId = courierSteps.courierLogin(new CourierLoginRequest(login, password))
                .then()
                .extract().body().path("id");

        courierSteps.deleteCourier(courierId);

    }

    @Step("Send POST request to /api/v1/courier")
    public Response sendPostRequestCourier(CourierCreateRequest courierCreateRequest){
        Response response = courierSteps.courierCreate(courierCreateRequest);
        return response;
    }

    @Step("Check response body OK and status code 201")
    public void checkResponseBodyIsOkAndStatusCode201(Response response){
        response.then()
                .assertThat().body("ok", is(true))
                .and()
                .statusCode(201);
    }

    @Step("Check response body MESSAGE and status code 409")
    public void checkResponseBodyMessageAndStatusCode409(Response response){
        response.then()
                .assertThat().body("message", is("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(409);
    }

    @Step("Check response body MESSAGE and status code 400")
    public void checkResponseBodyMessageAndStatusCode400(Response response){
        response.then()
                .assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(400);
    }

}
