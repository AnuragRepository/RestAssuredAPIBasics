package BasicPlaceAndLibraryAPI;

import Payloads.Payload;
import UtilityPackage.Utility;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class StandAloneAddPlaceTest {


    @Test
    public void addUpdateFetchPlace()
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        //Add place
       String response =  given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json").body(Payload.addPlace())
                .when().post("/maps/api/place/add/json").
                then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP")).header("Server","Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();

       System.out.println("Response = "+response);

       JsonPath js = Utility.jsonPathUtility(response);
       //String placeID = js.get("place_id");// both works
       String placeID = js.getString("place_id");// both works, since place id is string

        System.out.println("Place ID from response = "+placeID);

        // Update the address
        String updateToAddress= "Anurag address";

        given().log().all().queryParam("place_id",placeID).queryParam("key","qaclick123").header("Content-Type","application/json").body("{\n" +
                "\"place_id\":\""+placeID+"\",\n" +
                "\"address\":\""+updateToAddress+"\",\n" +
                "\"key\":\"qaclick123\"\n" +
                "}").when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));

        // fetch updated address

        String updatedResponse = given().log().all().queryParam("place_id",placeID).queryParam("key","qaclick123")
                .when().get("/maps/api/place/get/json").then().log().all().extract().response().asString();

        System.out.println("Updated Response = "+updatedResponse);

        JsonPath updJsObj = Utility.jsonPathUtility(updatedResponse);
        String updatedAddress = updJsObj.getString("address");

        System.out.println("Updated Address From Response = "+updatedAddress);
        Assert.assertEquals(updatedAddress,updateToAddress);

    }

    // Read Payloads.Payload from external files (json content -> bytes-> string for passing in body)

    @Test
    public void addPlaceReadPayloadViaExtFile() throws IOException {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                .body(new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/main/java/Payloads/ExternalFilePayload.json"))))
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP")).extract().response().asString();
    }


}
