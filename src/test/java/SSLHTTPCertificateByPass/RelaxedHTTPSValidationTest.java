package SSLHTTPCertificateByPass;

import Payloads.Payload;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class RelaxedHTTPSValidationTest {

    @Test
    public void addPlaceViaHTTPS() {

        //relaxedHTTPSValidation() - it will bypass any SSL/HTTP certification if any

        RestAssured.baseURI = "https://rahulshettyacademy.com";
        //Add place
        String response = given().log().all().relaxedHTTPSValidation().queryParam("key", "qaclick123").header("Content-Type", "application/json").body(Payload.addPlace())
                .when().post("/maps/api/place/add/json").
                then().log().all().assertThat().statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();

        System.out.println("Response = " + response);

    }
}
