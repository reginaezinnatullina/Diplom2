import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class BadClientLoginTest {
    private String accessToken;

    ClientLogin clientLogin = new ClientLogin("19011990@mail.ru", " ");

    @Before
    public void setUp() {
        RestAssured.baseURI = URL.HOST;
    }

    @Test
    @DisplayName("Вход несуществующего пользователя")
    public void testClientCreationAndCheckingStatus() {
        Response response =
                given()
                        .body(clientLogin)
                        .when()
                        .post("/api/auth/login");
        response.then().assertThat().statusCode(401);
    }
}

