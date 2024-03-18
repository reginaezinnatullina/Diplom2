import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ClientChangingTest {
    private String accessToken;
    ClientRegistation clientRegistation = new ClientRegistation("31011990@mail.ru", "12345", "Regina");
    ClientRegistation clientRegistation2 = new ClientRegistation("32011990@mail.ru", "12345", "Anna");

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.HOST;
    }

    @Test
    @DisplayName("Изменение данных пользователя с авторизацией")
    public void testClientChangingAndCheckingStatus() {
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
        Response response2 =
                given()
                        .header("Authorization", accessToken)
                        .and()
                        .body(clientRegistation2)
                        .when()
                        .patch("api/auth/user");
        response2.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
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

