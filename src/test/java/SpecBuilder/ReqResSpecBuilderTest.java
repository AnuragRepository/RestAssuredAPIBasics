package SpecBuilder;

import Payloads.Payload;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ReqResSpecBuilderTest {

    @Test
    public void specBuilderTest()
    {
       RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("key","qaclick123")
                .setContentType("application/json").build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(200).expectBody("scope",equalTo("APP"))
                       .expectHeader("Server","Apache/2.4.52 (Ubuntu)").build();

       String response = given().log().all().spec(requestSpecification).body(Payload.addPlace())
               .when().post("/maps/api/place/add/json")
               .then().log().all()
               .spec(responseSpecification).extract().response().asString();

        System.out.println("Response = "+response);

        //Sample
       /* String response =  given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json").body(Payloads.Payload.addPlace())
                .when().post("/maps/api/place/add/json").
                then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP")).header("Server","Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();*/


    }


}
