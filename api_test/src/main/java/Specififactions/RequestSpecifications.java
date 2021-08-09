package Specififactions;

import helpers.RequestHelpers;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecifications {
    public static RequestSpecification useJWTAuthentication() {
        String token = RequestHelpers.getAuthToken();
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", "Bearer " + token);
        builder.addHeader("User-Agent", "PostmanRuntime/7.26.8");
        builder.addHeader("Accept", "*/*");
        builder.addHeader("Accept-Encoding", "gzip, deflate, br");

        return builder.build();
    }

    public static RequestSpecification useFakeJWTAuthentication() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", "Bearer fakeTokenDoesNotExist");

        return builder.build();
    }

    public static RequestSpecification userBasicAuthentication() {
        RequestSpecBuilder builder = new RequestSpecBuilder();
        builder.addHeader("Authorization", "Basic dGVzdHVzZXI6dGVzdHBhc3M=");

        return builder.build();
    }
}
