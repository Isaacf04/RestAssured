import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import helpers.RequestHelpers;
import model.User;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;

public class AuthenticationTests extends Base{

    @Test(description = "This test aims to register a user")
    public void testRegister(){
        User testUser = new User(
                    "Pablo Juan",
                    generateRandomEmail(),
                    "password");

        String email = testUser.getEmail();

        given()
                .body(testUser)
            .when()
                .post("/v1/user/register")
            .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("user.email", Matchers.equalTo(email))
                .body("user.name", Matchers.equalTo("Pablo Juan"));
    }

    @Test(description = "This test aims to register login a user")
    public void testDuplicateRegister(){
        User testUser = new User(
                "Pablo Juan",
                "juan@jose.com",
                "password");

        given()
                .body(testUser)
                .when()
                .post("/v1/user/register")
                .then()
                .statusCode(406)
                .body("message", Matchers.equalTo("User already exists"));
    }

    @Test(description = "This test aims to register login a user")
    public void testLogin(){
        User testUser = new User(
                "Pablo Juan",
                "Alexie_Robel@yahoo.com",
                "pasword");

        given()
                .body(testUser)
                .when()
                .post("/v1/user/login")
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("token.access_token", Matchers.notNullValue())
                .body("user.email", Matchers.equalTo("Alexie_Robel@yahoo.com"));
    }

    @Test(description = "This test aims to register an invalid user")
    public void testInvalidLogin(){
        User testUser = new User(
                "Pablo Juan",
                "asdasdasnotexist@testemail.com",
                "password");

        given()
                .body(testUser)
                .when()
                .post("/v1/user/login")
                .then()
                .statusCode(404)
                .body("message", Matchers.equalTo("Invalid login details"));
    }

    @Test(description = "This aims to test logout")
    public void testLogOut(){

        given()
                .spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/user/logout")
                .then()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Successfully logged out"));
    }

    @Test(description = "This aims to test logout")
    public void testLogOutFakeUser(){

        given()
                .spec(RequestSpecifications.useFakeJWTAuthentication())
                .when()
                .get("/v1/user/logout")
                .then()
                .statusCode(400)
                .body("message", Matchers.equalTo("User not logged in"));
    }

}
