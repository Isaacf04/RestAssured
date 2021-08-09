import helpers.RequestHelpers;
import io.restassured.RestAssured;
import org.testng.annotations.*;

public class Base {

    protected int articleId;

    @Parameters("host")
    @BeforeSuite(alwaysRun = true)
    public void setup(@Optional("https://api-coffee-testing.herokuapp.com") String host) {

        System.out.println(String.format("Test Host: %s", host));

        RestAssured.baseURI = host;
    }

    // before groups

    @BeforeMethod(groups = "useArticle")
    void createArticle(){
        articleId = RequestHelpers.createRandomArticleAndGetID();
    }

    @AfterMethod(groups = "useArticle")
    void deleteArticle(){
        RequestHelpers.cleanUpArticle(articleId);
    }

}
