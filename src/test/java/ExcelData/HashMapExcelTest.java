package ExcelData;

import UtilityPackage.Utility;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.*;

public class HashMapExcelTest {

    @Test
    public void addPlaceAPIViaHashMapExcelData() throws FilloException {

        //get Data from excel

        Recordset recordsetObj = Utility.excelReadUtilityFillo("select * from AddPlaceAPIData");
        ArrayList<String> arrayListTypes = new ArrayList<String>();

        recordsetObj.next();
        String getLat = recordsetObj.getField("lat");
        String getLng = recordsetObj.getField("lng");
        String getAccuracy = recordsetObj.getField("accuracy");
        String getName = recordsetObj.getField("name");
        String getPhone = recordsetObj.getField("phone_number");
        String getAddress = recordsetObj.getField("address");
        do {
            arrayListTypes.add(recordsetObj.getField("types"));
        }
        while (recordsetObj.next());
        // after do while pointer goes to row2 , website data and language data are in row 1 so redirect pointer to
        // 0row0column for moving reading row1 or else use list at end
        recordsetObj.moveFirst();
        String getWebsite = recordsetObj.getField("website");
        String getLanguage = recordsetObj.getField("language");


        //Create HashMap for Json object

        HashMap<String,Object> subHashMapObj = new HashMap<String,Object>();
        subHashMapObj.put("lat",getLat);
        subHashMapObj.put("lng",getLng);

        //child hashmap for location object
        HashMap<String,Object> superHashMapObj = new HashMap<String,Object>();

        superHashMapObj.put("location",subHashMapObj);
        superHashMapObj.put("accuracy",getAccuracy);
        superHashMapObj.put("name",getName);
        superHashMapObj.put("phone_number",getPhone);
        superHashMapObj.put("address",getAddress);
        superHashMapObj.put("types",arrayListTypes.toArray()); //even list arrayListTypes works
        superHashMapObj.put("website",getWebsite);
        superHashMapObj.put("language",getLanguage);



        RestAssured.baseURI = "https://rahulshettyacademy.com";

        String response = given().log().all()
                .header("Content-Type", "application/json")
                .queryParam("key", "qaclick123")
                .body(superHashMapObj)
                .when().post("/maps/api/place/add/json")
                .then().log().all().assertThat()
                .statusCode(200).extract().response().asString();

        System.out.print("Response = " + response);

    }

}
