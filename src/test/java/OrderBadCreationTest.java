import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class OrderBadCreationTest {
    private String accessToken;
    ClientRegistation clientRegistation = new ClientRegistation("87668015990@mail.ru", "12345", "Regina");
    private List<String> ingredientsList;
    Order order = new Order(new Object[]{});

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.HOST;
    }

    @Test
    @DisplayName("Создание заказа с авторизацией, без ингредиентов")
    public void testOrderCreationWithAuthWithoutIngredientsAndCheckingStatus() {
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
                        .header("Authorization", accessToken)
                        .and()
                        .body("ingredients")
                        .when()
                        .post("/api/orders");
        response2.then().assertThat().statusCode(400);
    }
    @Test
    @DisplayName("Создание заказа без авторизации с ингредиентом")
    public void testOrderCreationWithoutAuthAndCheckingStatus() {
        ingredientsList = given()
                .when()
                .get("/api/ingredients")
                .then()
                .extract()
                .path("data._id");
        Random random = new Random();
        String randomIngredientFromList = ingredientsList.get(random.nextInt(ingredientsList.size()));
        order.setIngredients(new Object[]{randomIngredientFromList});
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
                        .log().all()
                        .and()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
        response2.then().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Создание заказа c авторизаций с ингредиентом")
    public void testOrderCreationWithAuthAndCheckingStatus() {
        ingredientsList = given()
                .when()
                .get("/api/ingredients")
                .then()
                .extract()
                .path("data._id");
        Random random = new Random();
        String randomIngredientFromList = ingredientsList.get(random.nextInt(ingredientsList.size()));
        order.setIngredients(new Object[]{randomIngredientFromList});
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
                        .header("Authorization", accessToken)
                        .and()
                        .header("Content-type", "application/json")
                        .and()
                        .body(order)
                        .when()
                        .post("/api/orders");
        response2.then().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Создание заказа c авторизаций с неверным хешом ингредиентом")
    public void testOrderCreationWithInvalidOrderWithAuthAndCheckingStatus() {
        ingredientsList = given()
                .when()
                .get("/api/ingredients")
                .then()
                .extract()
                .path("data._id");
        Random random = new Random();
        String randomIngredientFromList = ingredientsList.get(random.nextInt(ingredientsList.size()));
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
                        .header("Authorization", accessToken)
                        .and()
                        .body("ingredients" + "609646e4dc916e00276b2870")
                        .when()
                        .post("/api/orders");
        response2.then().assertThat().statusCode(400);
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

