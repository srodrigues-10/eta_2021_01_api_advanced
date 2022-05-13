package core;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

public class BaseTest implements Constants {

    @BeforeTest
    public void before(){
        //Setting baseURL
        RestAssured.baseURI = BASEURL;

        //Setting json
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
        requestSpecBuilder.setContentType(APP_CONTENT_TYPE);
        RestAssured.requestSpecification = requestSpecBuilder.build();

        //if fail display logs
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

}
