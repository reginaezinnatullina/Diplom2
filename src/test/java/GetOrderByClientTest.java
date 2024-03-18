import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class GetOrderByClientTest {
    private String accessToken;
    ClientRegistation clientRegistation = new ClientRegistation("34011990@mail.ru", "12345", "Regina");
    Order order = new Order(new Object[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"});

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.HOST;
    }

    @Test
    @DisplayName("Получение заказа авторизованного клиента и проверка статуса")
    public void testGetOrderByAUthClientAndCheckingStatus() {
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
                        .header("Authorization", accessToken)
                        .and()
                        .and()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
        response2.then().assertThat().statusCode(200);
        Response response3 =
                given()
                        .log().all()
                        .header("Authorization", accessToken)
                        .and()
                        .and()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .get("/api/orders");
        response3.then().assertThat().statusCode(200)
                .and()
                .body("success", is(true));
    }
    @Test
    @DisplayName("Получение заказа неавторизованного клиента и проверка статуса")
    public void testGetOrderByClientAndCheckingStatus() {
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
                        .header("Authorization", accessToken)
                        .and()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
        response2.then().assertThat().statusCode(200);
        Response response3 =
                given()
                        .log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .get("/api/orders");
        response3.then().assertThat().statusCode(401);

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

