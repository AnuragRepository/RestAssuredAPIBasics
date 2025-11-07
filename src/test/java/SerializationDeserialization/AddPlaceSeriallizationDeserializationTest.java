package SerializationDeserialization;

import PojoPackageDeserializationSecond.PojoParent2;
import PojoPackageSerialization.PojoChildLocation;
import PojoPackageSerialization.PojoParent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class AddPlaceSeriallizationDeserializationTest {

    @Test
    public void addPlaceSerializationDeserialization() throws JsonProcessingException {
        //Serialization - Passing JavaObject into Body for Payloads.Payload generation
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
        String response =given().log().all()
                .queryParam("key","qaclick123")
                .body(pojoParentObj)
                .when().post("/maps/api/place/add/json")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

        System.out.println("Response = "  +response);

        //Deserialization - Fetching response from Java Object

       /* Since we already have response in string format, directly use response instead
        of get HTTP method*/

        // To convert a JSON string into a specific Java object (Plain Old Java Object)
        ObjectMapper objectMapper = new ObjectMapper();
        PojoParent2 pojoParent2Obj = objectMapper.readValue(response,PojoParent2.class);


        System.out.println("Status from response = "+pojoParent2Obj.getStatus());
        Assert.assertEquals(pojoParent2Obj.getStatus(),"OK");
        System.out.println("Scope from response = "+pojoParent2Obj.getScope());
        Assert.assertEquals(pojoParent2Obj.getScope(),"APP");

        //Below are dynamic values so cannot use assertion
        System.out.println("Place ID from response = "+pojoParent2Obj.getPlace_id());
        System.out.println("Reference from response = "+pojoParent2Obj.getReference());
        System.out.println("ID from response = "+pojoParent2Obj.getId());
    }

}
