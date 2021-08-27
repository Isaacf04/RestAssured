import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import helpers.RequestHelpers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Article;
import model.Post;
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

public class PostCommentTest extends Base{

    @Test(description = "This test aims to create a new post")
    public void createPostTest(){

        Post newPost = new Post("some_title", "Lorem Impusim short mode");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
            .when()
                .post("/v1/post")
            .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                 .body("message", Matchers.equalTo("Post created"));
    }

    @Test(description = "This test aims to get all Posts")
    public void getPosts(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/posts")
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("results", Matchers.notNullValue());
    }

    @Test(description = "This test aims to get a Post by ID")
    public void getPostByOneID(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/post/" + id)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("data", Matchers.notNullValue());
    }

    @Test(description = "This test aims to update a Post by ID")
    public void updatePostByOneID(){
        Post newPost = new Post("Volcan_Barva", "For visitor in the hook");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Post updatePost = new Post("Volcan_Barva", "For visitor in the hook");
        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(updatePost)
                .when()
                .put("/v1/post/" + id)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.notNullValue());
    }

    @Test(description = "This test aims to update a Post by ID")
    public void deletePostByOneID(){
        Post newPost = new Post("Volcan_Braulio", "For visitor in the chimmey");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");
        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .delete("/v1/post/" + id)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.notNullValue());
    }

    @Test(description = "This test aims to update a Post by Invalid ID")
    public void deletePostByOneIDNegativeCase(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .delete("/v1/post/" + "noValid")
                .then()
                .log().all()
                .body("message", Matchers.equalTo("Invalid parameter"));
    }

}
