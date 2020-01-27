package core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.junit.BeforeClass;

public class BaseTest implements Constantes {

    @BeforeClass
    public static void setup(){

        RestAssured.baseURI = BASE_URL;
        RestAssured.port = PORT;

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.setContentType(CONTENT_TYPE);
        RestAssured.requestSpecification = reqBuilder.build();

        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

}
