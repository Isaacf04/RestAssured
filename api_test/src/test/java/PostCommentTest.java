import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import helpers.RequestHelpers;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.Article;
import model.Comment;
import model.Post;
import model.User;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static helpers.DataHelper.generateRandomEmail;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import io.restassured.builder.ResponseSpecBuilder;

public class PostCommentTest extends Base{

// Post Test Suite
    // Positive test: post.Create
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

    // Negative test: post.Create
    @Test(description = "This negative test aims to create a new post")
    public void createPostTestNegative(){

        Post newPost = new Post("some_title", "Lorem Impusim short mode");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post")
                .then()
                .log().all()
                .body("message", Matchers.equalTo("Please login first"));
    }

    // Positive test: post.All

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

    // Negative test: post.All

    @Test(description = "This Negative test aims to get all Posts")
    public void getPostsNegative(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/posts1")
                .then()
                .log().all();
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(404);
    }

    // Positive Test: post.One
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

    // Negative Test: post.One
    @Test(description = "This negative test aims to get a Post by ID")
    public void getPostByOneIDNegative(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/post/" + 010101)
                .then()
                .log().all()
                .body(Matchers.notNullValue());
    }

    // Positive test: post.Update
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
                .body("message", is("Post updated"));
    }

    // Negative test: post.Update
    @Test(description = "This test aims to update a Post by ID")
    public void updatePostByOneIDNegative(){
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
                .put("/v1/post/" + 001)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validateNegativeResponse())
                .body("message", is("Post could not be updated"));
    }

    @Test(description = "This test aims to delete a Post by ID")
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
                .body("message", is("Post deleted"));
    }
// Negative test
    @Test(description = "This test aims to update a Post by Invalid ID")
    public void deletePostByOneIDNegativeCase(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .delete("/v1/post/" + "noValid")
                .then()
                .log().all()
                .body("message", Matchers.equalTo("Invalid parameter"));
    }


    // comments Tests suite

    // Positive test: comment.Create

    @Test(description = "This test aims to create a comment by postid")
    public void createComment(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "SoloVolcano");

         // adding Basic Authentication @params testuser: String, testpass: String
        given().auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("id", Matchers.notNullValue());
    }

    // Positive test: comment.Create

    @Test(description = "This negative test aims to create a comment by postid")
    public void createCommentNegative(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "SoloVolcano");

        // adding Basic Authentication @params testuser: String, testpass: String
        given().auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id + "2339")
                .then()
                .log().all()
                .spec(ResponseSpecifications.validateNegativeResponse())
                .body(Matchers.notNullValue());
    }

// Positive test:
    @Test(description = "This test aims to get a comment by postid")
    public void getComments(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "SoloVolcano");

        Response responseComment = given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id);

        jsonPath = responseComment.jsonPath();
        String commentid = Integer.toString(jsonPath.get("id"));

        given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .get("/v1/comments/" + id)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("results", Matchers.notNullValue());

    }
// Positive Test
    @Test(description = "This test aims to get all comment by postid")
    public void getCommentbyid(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "SoloVolcano");

        Response responseComment = given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id);

        jsonPath = responseComment.jsonPath();
        String commentid = Integer.toString(jsonPath.get("id"));

        given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .get("/v1/comment/" + id + "/" + commentid)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("data.comment", Is.is("SoloVolcano"));

    }

    // Negative test

    @Test(description = "This test aims to get a comment by postid")
    public void getCommentbyidNegative(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "SoloVolcano");

        Response responseComment = given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id);

        jsonPath = responseComment.jsonPath();
        String commentid = Integer.toString(jsonPath.get("id"));

        given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .get("/v1/comment/" + id + "/" + "commentidNegative")
                .then()
                .log().all()
                .body("data.comment", equalTo(null));
    }

    //Positive test: comment.Update

    @Test(description = "This test aims to update a comment by postid")
    public void updateCommentByid(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "RinconDeLaVieja");

        Response responseComment = given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id);

        jsonPath = responseComment.jsonPath();
        String commentid = Integer.toString(jsonPath.get("id"));

        given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .put("/v1/comment/" + id + "/" + commentid)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Is.is("Comment updated"));
    }

    // Negative test: comment.Update

    @Test(description = "This test aims to update a comment by invalidPostid")
    public void updateCommentByidNegative(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "RinconDeLaVieja");

        Response responseComment = given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id);

        jsonPath = responseComment.jsonPath();
        String commentid = Integer.toString(jsonPath.get("id"));

        given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .put("/v1/comment/" + 000000 + "/" + commentid)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validateNegativeResponse())
                .body("message", Is.is("Comment could not be updated"));
    }


    //Positive test: comment.Delete

    @Test(description = "This test aims to update a comment by postid")
    public void deleteCommentByid(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "RinconDeLaVieja");

        Response responseComment = given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id);

        jsonPath = responseComment.jsonPath();
        String commentid = Integer.toString(jsonPath.get("id"));

        given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .delete("/v1/comment/" + id + "/" + commentid)
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Is.is("Comment deleted"));
    }

    // Negative test: comment.Delete

    @Test(description = "This test aims to update a comment by invalidPostid")
    public void deleteCommentByidNegative(){
        Post newPost = new Post("Volcan_Poas", "Lorem Impusim short mode");
        Response response = given().spec(RequestSpecifications.useJWTAuthentication())
                .body(newPost)
                .when()
                .post("/v1/post");

        JsonPath jsonPath = response.jsonPath();
        String id = Integer.toString(jsonPath.get("id"));
        Comment newComment = new Comment("Volcan", "RinconDeLaVieja");

        Response responseComment = given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .post("/v1/comment/" + id);

        jsonPath = responseComment.jsonPath();
        String commentid = Integer.toString(jsonPath.get("id"));

        given()
                .auth()
                .preemptive()
                .basic("testuser", "testpass")
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON)
                .body(newComment)
                .when()
                .delete("/v1/comment/" + id + "/" + "NoExist1")
                .then()
                .log().all()
                .body("message", Matchers.equalTo("Invalid parameter"));
    }

}


