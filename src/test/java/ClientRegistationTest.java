import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ClientRegistationTest {
    private String accessToken;
    ClientRegistation clientRegistation = new ClientRegistation("19051959@mail.ru", "12345", "Regina");

    BadClientRegistation badClientRegistation = new BadClientRegistation("Regina", "123456");

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.HOST;
    }

    @Test
    @DisplayName("Регистрация нового клиента")
    public void testClientCreationAndCheckingStatus() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(clientRegistation)
                        .when()
                        .post("/api/auth/register");
        response.then().assertThat().statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .body("success", is(true));
        accessToken = String.valueOf(response.jsonPath().getString("accessToken"));
        System.out.println(clientRegistation);
    }@Test
    @DisplayName("Регистрация уже зарегистрированного клиента")
    public void testSameClientCreationAndCheckingStatus() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(clientRegistation)
                        .when()
                        .post("/api/auth/register");
        response.then().assertThat().statusCode(200)
                .and()
                .body("accessToken", notNullValue())
                .body("success", is(true));
        accessToken = String.valueOf(response.jsonPath().getString("accessToken"));
        Response response2 =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(clientRegistation)
                        .when()
                        .post("/api/auth/register");
        response2.then().assertThat().statusCode(403);
    }
    @After
    public void courierDeletion() {
        RestAssured.baseURI = URL.HOST;
        // Отправляем DELETE-запрос на удаление курьера
        given()
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user");
    }
}

