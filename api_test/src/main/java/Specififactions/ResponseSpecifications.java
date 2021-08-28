package Specififactions;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

public class ResponseSpecifications {
    public static ResponseSpecification validatePositiveResponse() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(200);
        builder.expectBody(Matchers.notNullValue());
        builder.expectContentType("application/json");
        return builder.build();
    }

    public static ResponseSpecification validateNegativeResponse() {
        ResponseSpecBuilder builder = new ResponseSpecBuilder();
        builder.expectStatusCode(404|406);
        builder.expectBody("message", Matchers.notNullValue());
        builder.expectContentType("application/json");
        return builder.build();
    }
}
