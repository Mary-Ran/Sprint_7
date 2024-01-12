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

public class CourierTest {

    String login;
    String password;
    String firstName;
    Integer courierId;
    private CourierSteps courierSteps;

    @Before
    public void SetUp() {
        login = RandomStringUtils.randomAlphanumeric(10);
        password = RandomStringUtils.randomAlphanumeric(10);
        firstName = RandomStringUtils.randomAlphanumeric(10);
        courierSteps = new CourierSteps();
        RestAssured.baseURI = BASE_URI;
    }

    @Test
    public void checkSuccessfulCourierCreation() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);

        Response response = courierSteps.courierCreate(courierCreateRequest);
        checkResponseBodyIsOkAndStatusCode201(response);

    }

    @Test
    public void checkTheCreationOfTwoIdenticalCouriers() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest(login, password, firstName);

        courierSteps.courierCreate(courierCreateRequest);

        Response response = courierSteps.courierCreate(courierCreateRequest);
        checkResponseBodyMessageAndStatusCode409(response);

    }

    @Test
    public void checkTheCreationOfTwoCouriersWithTheSameLogins() {
        String newPassword = RandomStringUtils.randomAlphanumeric(10);
        String newFirstName = RandomStringUtils.randomAlphanumeric(10);

        CourierCreateRequest firstCourier = new CourierCreateRequest(login, password, firstName);
        CourierCreateRequest secondCourier = new CourierCreateRequest(login, newPassword, newFirstName);

        courierSteps.courierCreate(firstCourier);

        Response response = courierSteps.courierCreate(secondCourier);
        checkResponseBodyMessageAndStatusCode409(response);

    }

    @Test
    public void checkTheCreationOfACourierWithoutLogin() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setPassword(password);
        courierCreateRequest.setFirst_name(firstName);

        Response response = courierSteps.courierCreate(courierCreateRequest);
        checkResponseBodyMessageAndStatusCode400(response);

    }

    @Test
    public void checkTheCreationOfACourierWithoutPassword() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setFirst_name(firstName);

        Response response = courierSteps.courierCreate(courierCreateRequest);
        checkResponseBodyMessageAndStatusCode400(response);

    }

    @Test
    public void checkTheCreationOfACourierWithoutFirstName() {
        CourierCreateRequest courierCreateRequest = new CourierCreateRequest();
        courierCreateRequest.setLogin(login);
        courierCreateRequest.setPassword(password);

        Response response = courierSteps.courierCreate(courierCreateRequest);
        checkResponseBodyIsOkAndStatusCode201(response);

    }

    @After
    public void deleteCourier() {
        courierId = courierSteps.courierLogin(new CourierLoginRequest(login, password))
                .then()
                .extract().body().path("id");

        if (courierId!=null) {
            courierSteps.deleteCourier(courierId);
        }
    }

    @Step("Check response body OK and status code 201")
    public void checkResponseBodyIsOkAndStatusCode201(Response response){
        response.then()
                .assertThat().body("ok", is(true))
                .and()
                .statusCode(SC_CREATED);
    }

    @Step("Check response body MESSAGE and status code 409")
    public void checkResponseBodyMessageAndStatusCode409(Response response){
        response.then()
                .assertThat().body("message", is("Этот логин уже используется. Попробуйте другой."))
                .and()
                .statusCode(SC_CONFLICT);
    }

    @Step("Check response body MESSAGE and status code 400")
    public void checkResponseBodyMessageAndStatusCode400(Response response){
        response.then()
                .assertThat().body("message", is("Недостаточно данных для создания учетной записи"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

}
