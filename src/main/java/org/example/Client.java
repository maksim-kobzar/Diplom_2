package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.http.ContentType;
public class Client {
    protected RequestSpecification getSpec(){
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .build();
    }
}
