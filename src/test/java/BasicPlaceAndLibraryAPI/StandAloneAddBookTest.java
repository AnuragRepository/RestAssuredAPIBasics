package BasicPlaceAndLibraryAPI;

import Payloads.Payload;
import UtilityPackage.Utility;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StandAloneAddBookTest {


    @Test(dataProvider = "bookData")
    public static void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "http://216.10.245.166";

        //add book
        String response = given().log().all().header("Content-Type", "application/json").body(Payload.jsonBodyAddBook(isbn, aisle))
                .when().post("/Library/Addbook.php")
                .then().log().all().assertThat().statusCode(200).body("Msg", equalTo("successfully added"))
                .extract().response().asString();

        JsonPath js = Utility.jsonPathUtility(response);
        String bookID = js.getString("ID");
        System.out.println("Book id = " + bookID);

        //delete book
        given().log().all().header("Content-Type", "application/json").body(Payload.jsonBodyDeleteBook(bookID))
                .when().post("/Library/DeleteBook.php").
                then().log().all().assertThat().statusCode(200).body("msg", equalTo("book is successfully deleted"));

    }

    @DataProvider(name = "bookData")
    public Object[][] getData()
    {
        return new Object[][]{{"TestBook","106"},{"TestBook","107"},{"TestBook","108"}};
    }



}
