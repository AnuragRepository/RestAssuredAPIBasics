package JiraFlowAndBasicAuthorization;

import Payloads.Payload;
import UtilityPackage.Utility;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.*;

public class JiraBasicAuthTest {

    @Test
    public void createBugAddAttachmentAndVerifyDetails()
    {
        String basicEncodedAuth = "Basic YW51cmFnMzA1NkBnbWFpbC5jb206QVRBVFQzeEZmR0YwUmVodVE0ODN0V1pDNzJZa0lMOEQySWIwQ1FBUmhqQXV2TUZxT0VKb2VFU2lPb2w1SkM2UmJCRVpJN0ZVMGxRNm5Ocm1iMHBIWGFrT3lKNFJPSlJmZkNvb2h3T1ZzckJNbUk4am00WERwcmhxdWRaSEtUV3I2aDZOb1djMlR6RFgtUEJJbzE3bk1zREQyLVhHYnVNOUJjTlA5T2prMmxXdkRuWHdxRjJna3RFPThDNjlFMTAz";
        RestAssured.baseURI="https://anurag3056.atlassian.net";

        String response = given().log().all()
                .header("Content-Type","application/json")
                .header("Authorization",basicEncodedAuth)
                .body(Payload.createBugPayload())
                .when().post("/rest/api/2/issue")
                .then().log().all()
                .assertThat().statusCode(201)
                .extract().response().asString();

       JsonPath js=  Utility.jsonPathUtility(response);
       String bugID= js.getString("id");

       System.out.println("bugID = "+bugID);

       //attach attachment
        String attachmentToAddName= "UploadFolder/Library+API.postman_collection.json";
       String attachmentResponse = given().log().all()
                .pathParams("Key",bugID)
                .header("X-Atlassian-Token","no-check")
                .header("Authorization",basicEncodedAuth)
                .multiPart("file",new File(System.getProperty("user.dir")+"/src/main/java/UploadFolder/Library+API.postman_collection.json"))
                .when().post("/rest/api/3/issue/{Key}/attachments")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response().asString();

       // .body("filename",equalTo(attachmentToAddName))


      JsonPath js1 = Utility.jsonPathUtility(attachmentResponse);
      String addedAttachment = js1.getString("filename");
      System.out.println("Added Attachment in "+bugID+" is " +addedAttachment);

      // issue details

        String issueDetailsResponse= given().log().all()
                .pathParams("key1",bugID)
                .header("Accept","application/json")
                .header("Authorization",basicEncodedAuth)
                .when().get("/rest/api/2/issue/{key1}")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        //.body("filename",equalTo(attachmentToAddName))

        System.out.println("issueDetailsResponse = "+issueDetailsResponse);



    }

}
