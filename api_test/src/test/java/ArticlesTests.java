import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import helpers.RequestHelpers;
import io.restassured.response.Response;
import model.Article;
import model.User;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class ArticlesTests extends Base{

    @Test(description = "This test aims to create a new article")
    public void createArticleTest(){

        Article testArticle = new Article("randomTitle", "Lorem Impusim short mode");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testArticle)
            .when()
                .post("/v1/article")
            .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Article created"));
    }

    @Test(description = "This test aims to get all articles", groups = "useArticle")
    public void getAllArticlesTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/articles")
                .then()
                .log().all()
                .statusCode(200)
                .body("results[0].data[0].id", Matchers.equalTo(articleId));
    }

    @Test(description = "This test aims to get all articles")
    public void getOneArticlesTest(){

        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/article/" + articleId);

        assertThat(response.asString(), matchesJsonSchemaInClasspath("article.schema.json"));
        assertThat(response.path("data.id"), Matchers.equalTo(articleId));

    }

}
