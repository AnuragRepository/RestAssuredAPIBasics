package Revise;
import Payloads.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class PlaceAPITest {

    public  String placeID = "";
    String newAddressToUpdate = "AnuragHome";
    String updateSuccessMessage = "";
    String newAddressUpdated = "";

    @Test(priority = 0)
    void addPlaceAPI()
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key","qaclick123")
                .header("Content-Type","application/json")
                .body(Payload.addPlace()).
                when().post("/maps/api/place/add/json").
                then().log().all().assertThat().statusCode(200).body("scope",equalTo("APP")).extract().response().asString();

        System.out.println("Response = "+response);

        JsonPath js = new JsonPath(response);
        placeID = js.get("place_id");
        System.out.println("Fetched Place ID from ADD Place API = "+placeID);
    }


    @Test(dependsOnMethods = "addPlaceAPI",priority = 1)
    void updateAPI() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeID).body("{\n" +
                        "\"place_id\":\"" + placeID + "\",\n" +
                        "\"address\":\"" + newAddressToUpdate + "\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}")
                .when().put("/maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        updateSuccessMessage = js.get("msg");

        Assert.assertEquals(updateSuccessMessage, "Address successfully updated");

    }


    @Test(priority = 2)
    void getAPI()
    {
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response =  given().log().all().queryParam("key","qaclick123")
                .queryParam("place_id",placeID)
                .when().get("/maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
        newAddressUpdated = js.get("address");
        Assert.assertEquals(newAddressToUpdate,newAddressUpdated);
    }

}
