package SerializationDeserialization;

import PojoPackageSerialization.PojoChildLocation;
import PojoPackageSerialization.PojoParent;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class AddPlaceViaPojoSerializationTest {

    @Test
    public void addPlace()
    {

        PojoParent pojoParentObj = new PojoParent();
        pojoParentObj.setAccuracy(50);
        pojoParentObj.setName("Frontline house");
        pojoParentObj.setPhone_number("(+91) 983 893 3937");
        pojoParentObj.setAddress("29, side layout, cohen 09");
        pojoParentObj.setWebsite("http://google.com");
        pojoParentObj.setLanguage("French-IN");

        //Set value while creating childPojo Class
        PojoChildLocation PojoChildLocationObj = new PojoChildLocation();
        PojoChildLocationObj.setLat(-38.383494);
        PojoChildLocationObj.setLng(33.427362);

        //Pass child Pojo object into Parent Pojo object as it contains info for Lat, Lan
        pojoParentObj.setLocation(PojoChildLocationObj);

        //Create list for adding values and pass listObj containing values into ParentPpo Class
        List<String> types = new ArrayList<String>();
        types.add("shoe park");
        types.add("shop");
        pojoParentObj.setTypes(types);


        // add place API
        // Pass Parent pojo obj into Request/Payloads.Payload/Body which has set values
        //Serialization

        RestAssured.baseURI ="https://rahulshettyacademy.com";
        String response = given().log().all()
                .queryParam("key","qaclick123")
                .body(pojoParentObj)
                .when().post("/maps/api/place/add/json")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        System.out.println("Response = "  +response);

    }

}
