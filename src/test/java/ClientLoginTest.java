import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ClientLoginTest {
    private String accessToken;
    ClientRegistation clientRegistation = new ClientRegistation("19051995@mail.ru", "12345", "Regina");

    ClientLogin clientLogin = new ClientLogin("19051995@mail.ru", "12345");

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.HOST;
    }

    @Test
    @DisplayName("Вход зарегистрированного клиента и проверка статуса")
    public void testLoginAuthClientAndCheckingStatus() {
        Response response =
                given()
                        .log().all()
                        .and()
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
                        .log().all()
                        .and()
                        .header("Content-type", "application/json")
                        .and()
                        .body(clientLogin)
                        .when()
                        .post("/api/auth/login");
        response2.then().assertThat().statusCode(200)
                .and()
                .body("accessToken", notNullValue());
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

