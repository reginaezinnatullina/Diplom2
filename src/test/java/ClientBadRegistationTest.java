import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class ClientBadRegistationTest {
    BadClientRegistation badClientRegistation = new BadClientRegistation("Regina", "123456");

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.HOST;
    }

   @Test
    @DisplayName("Регистрация пользователя, без заполнения одного из обязательных полей")
    public void testBadClientCreationAndCheckingStatus() {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .and()
                        .body(badClientRegistation)
                        .when()
                        .post("/api/auth/register");
        response.then().assertThat().statusCode(403);
    }
}

