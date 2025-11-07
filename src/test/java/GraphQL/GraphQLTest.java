package GraphQL;

import org.testng.annotations.Test;

import static Payloads.Payload.graphQLMutationPayload;
import static Payloads.Payload.graphQLQueryPayload;
import static io.restassured.RestAssured.*;

public class GraphQLTest {

    //query -GET- Fetch data
    //copy payload from graphQL explorer Network tab- view source as it will give body in converted  graphQL command -> json format->String format
    //graphQL explorer and postman in background convert graphQL commands to json format

    @Test
    public void query()
    {
        int characterID = 18256;
        int locationID = 25314;
        int episodeID = 16976;
        String name = "Anurag";
        String epName = "TMKOC";

       String queryResponse = given().log().all().header("Content-Type","application/json")
                .body(graphQLQueryPayload(characterID,locationID,episodeID,name,epName))
                .when().post("https://rahulshettyacademy.com/gq/graphQL")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

    }

    //create data
    @Test
   public void mutation()
    {
        String locationName = "India";
        String locationType = "North";
        String locationDimension = "123";
        String characterName = "Anurag";
        String characterType = "IT";
        String characterStatus = "Single";
        String characterSpecies = "human";
        String characterGender = "Male";
        String characterImage = "png";
        int characterOriginId = 25309 ;
        int characterlocationId = 25309;
        String episodeName = "TMKOC";
        String episodeairDate = "12-12-25";
        String episodecustomID = "111";
        int [] locationIDForDelete = {25313};

        String mutationResponse = given().log().all().header("Content-Type","application/json")
                .body(graphQLMutationPayload(locationName,locationType,locationDimension,characterName,characterType,
                        characterStatus,characterSpecies,characterGender,characterImage,characterOriginId,characterlocationId,
                        episodeName,episodeairDate,episodecustomID,locationIDForDelete))
                .when().post("https://rahulshettyacademy.com/gq/graphQL")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();


    }



}
