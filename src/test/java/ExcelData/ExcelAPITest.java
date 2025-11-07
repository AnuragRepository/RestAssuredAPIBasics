package ExcelData;

import UtilityPackage.Utility;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static UtilityPackage.Utility.jsonPathUtility;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class ExcelAPITest {

    @Test
    public void addPlace() throws FilloException {
        //Create jsonBody from HashMap and pass map object in body
        HashMap<String,Object> jsonMapObj = new HashMap<String,Object>();

        Recordset recordsetObj = Utility.excelReadUtilityFillo("Select * from PageB");

        ArrayList<String> columnValueList = new ArrayList<String>();

        while(recordsetObj.next())
        {
            columnValueList.add(recordsetObj.getField("Value"));
        }

        jsonMapObj.put("name",columnValueList.get(0));
        jsonMapObj.put("isbn",columnValueList.get(1));
        jsonMapObj.put("aisle",columnValueList.get(2));
        jsonMapObj.put("author",columnValueList.get(3));

        RestAssured.baseURI = "http://216.10.245.166";
       String addPlaceResponse = given().log().all()
                .header("Content-Type","application/json")
                .body(jsonMapObj)
                .when().post("/Library/Addbook.php")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

       JsonPath js = jsonPathUtility(addPlaceResponse);
       String idFromAddPlaceResponse = js.get("ID");


       //delete added Place so that test not fail with duplicate value
       HashMap<String,Object> jsonMapObj1 = new HashMap<String,Object>();
       Recordset recordsetObj1 =  Utility.excelReadUtilityFillo("Select * from PageC");
        recordsetObj1.next();
        String IDFromExcel = recordsetObj1.getField("Key");
        jsonMapObj1.put(IDFromExcel,idFromAddPlaceResponse);

        given().log().all()
                .header("Content-Type","application/json")
                .body(jsonMapObj1)
                .when().delete("/Library/DeleteBook.php")
                .then().log().all()
                .assertThat().statusCode(200)
                .body("msg",equalTo("book is successfully deleted"))
                .extract().response().asString();

    }

}
