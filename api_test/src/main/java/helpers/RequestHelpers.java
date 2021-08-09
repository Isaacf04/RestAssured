package helpers;

import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Article;
import model.User;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class RequestHelpers {

    public static String TOKEN = "";

    //   get a json token from the login request
    public static String getAuthToken () {

        User testUser = new User(
                "Pablo Juan",
                "Alexie_Robel@yahoo.com",
                "pasword");

        Response response = given().body(testUser).post("/v1/user/login");
        JsonPath jsonPath = response.jsonPath();
        TOKEN = jsonPath.get("token.access_token");
        System.out.println("New token fetched" + TOKEN);
        return TOKEN;
    }

    public static int createRandomArticleAndGetID () {
        Article randomArticle = new Article("randome", "randome");
       Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(randomArticle)
                .when()
                .log().all()
                .post("/v1/article");

        JsonPath jsonPath = response.jsonPath();
        return jsonPath.get("id");
    }

    public static void cleanUpArticle (int id) {
        given().spec(RequestSpecifications.useJWTAuthentication())
                .delete("/v1/article/"+ id);
    }

}
