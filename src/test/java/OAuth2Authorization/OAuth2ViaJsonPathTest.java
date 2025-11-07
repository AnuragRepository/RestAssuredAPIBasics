package OAuth2Authorization;

import UtilityPackage.Utility;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class OAuth2ViaJsonPathTest {

    @Test
    public void connectAuthorisationServerAndFetchAPIDetails() {
        RestAssured.baseURI = "https://rahulshettyacademy.com";

        //Generate Access Token
        String authorisationServerResp = given().log().all()
                .formParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .formParam("grant_type", "client_credentials")
                .formParam("scope", "trust").
                when().post("/oauthapi/oauth2/resourceOwner/token")
                .then().log().all().
                assertThat().statusCode(200)
                .extract().response().asString();

        JsonPath js = Utility.jsonPathUtility(authorisationServerResp);
        String accessToken = js.getString("access_token");
        System.out.println("Generated Access Token = " + accessToken);

        //Connect API without AccessToken
        String responseWithoutAccessToken = given().log().all()
                .when().get("/oauthapi/getCourseDetails")
                .then().log().all()
                .assertThat().statusCode(401)
                .body("msg",equalTo("Invalid or Expired token"))
                .extract().response().asString();

        System.out.println("Response Without AccessToken = "+responseWithoutAccessToken);

        //Connect API with AccessToken
        String responseWithAccessToken = given().log().all()
                .queryParam("access_token",accessToken)
                .when().get("/oauthapi/getCourseDetails")
                .then().log().all()
                .assertThat().statusCode(401)
                .extract().response().asString();

        System.out.println("Response With AccessToken = "+responseWithAccessToken);



    }

}
