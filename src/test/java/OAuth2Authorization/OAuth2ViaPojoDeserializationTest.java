package OAuth2Authorization;

import PojoPackageDeserializationOne.PojoParent;
import UtilityPackage.Utility;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OAuth2ViaPojoDeserializationTest {

    @Test
    public void connectAuthorisationServerAndFetchAPIDetailsViaPojoDeserialization() {
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

        //We have stored the response in Pojo object via below

        PojoParent pojoParentObj = given().log().all()
                .queryParam("access_token",accessToken)
                .when().get("/oauthapi/getCourseDetails")
                .then().log().all()
                .assertThat().statusCode(401)
                .extract().response().as(PojoParent.class);

        System.out.println("DESERIALISATION OUTPUTS: Now extract details from Json response");

        //1. Find the value of non-array items from response using Pojo class object

        System.out.println("Instructor name = "+pojoParentObj.getInstructor());
        Assert.assertEquals(pojoParentObj.getInstructor(),"RahulShetty");

        System.out.println("URL = "+pojoParentObj.getUrl());
        Assert.assertEquals(pojoParentObj.getUrl(),"rahulshettycademy.com");

        System.out.println("Services = "+pojoParentObj.getServices());
        Assert.assertEquals(pojoParentObj.getServices(),"projectSupport");

        System.out.println("Expertise = "+pojoParentObj.getExpertise());
        Assert.assertEquals(pojoParentObj.getExpertise(),"Automation");

        System.out.println("linkedIn = "+pojoParentObj.getLinkedIn());
        Assert.assertEquals(pojoParentObj.getLinkedIn(),"https://www.linkedin.com/in/rahul-shetty-trainer/");

       // b. Find all course title under WebAutomation ProgramList
        System.out.println("All Courses Titles under Web Automation ProgramList are :-");
      for(int i=0;i<pojoParentObj.getCourses().getWebAutomation().size();i++)
      {
         System.out.println(pojoParentObj.getCourses().getWebAutomation().get(i).getCourseTitle());
      }

        // c. Find all course price under WebAutomation ProgramList
        System.out.println("All Course Prices under Web Automation ProgramList are :-");
        for(int i=0;i<pojoParentObj.getCourses().getWebAutomation().size();i++)
        {
            System.out.println(pojoParentObj.getCourses().getWebAutomation().get(i).getPrice());
        }

        // d. Find all course Title under API ProgramList
        System.out.println("All Course Title under API ProgramList are :-");
        for(int i=0;i<pojoParentObj.getCourses().getApi().size();i++)
        {
            System.out.println(pojoParentObj.getCourses().getApi().get(i).getCourseTitle());
        }

        // e. Find all Course Price under API ProgramList
        System.out.println("All Course Price under API ProgramList are :-");
        for(int i=0;i<pojoParentObj.getCourses().getApi().size();i++)
        {
            System.out.println(pojoParentObj.getCourses().getApi().get(i).getPrice());
        }

        // f. Find all Course Title under mobile ProgramList
        System.out.println("All Course Title under Mobile ProgramList are :-");
        for(int i=0;i<pojoParentObj.getCourses().getMobile().size();i++)
        {
            System.out.println(pojoParentObj.getCourses().getMobile().get(i).getCourseTitle());
        }

        // g. Find all Course Price under mobile ProgramList
        System.out.println("All Course Price under Mobile ProgramList are :-");
        for(int i=0;i<pojoParentObj.getCourses().getMobile().size();i++)
        {
            System.out.println(pojoParentObj.getCourses().getMobile().get(i).getPrice());
        }

       // h. Find price of specific Course with tile Soap UI WebServiceTesting

        for(int i=0;i<pojoParentObj.getCourses().getApi().size();i++)
        {
            if(pojoParentObj.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
            {
                System.out.println("Course Title named "+pojoParentObj.getCourses().getApi().get(i).getCourseTitle()+" has price = " +pojoParentObj.getCourses().getApi().get(i).getPrice());
            }
        }

       // i. Print all course title of web automation array and validate

        System.out.println("Print all course title of web automation array and validate");

        String [] expectedArrayItems = {"Selenium Webdriver Java","Cypress","Protractor"};
        ArrayList<String> actualList = new ArrayList<String>();

        for(int i=0;i<pojoParentObj.getCourses().getWebAutomation().size();i++)
        {
            actualList.add (pojoParentObj.getCourses().getWebAutomation().get(i).getCourseTitle());
        }
        Assert.assertEquals(actualList, Arrays.asList(expectedArrayItems));

        //j. verify if course title -'Appium-Mobile Automation using Java' present in entire response
        // Doubt - tomorrow if another course add , so , since we are counting known coursesList -limitation of this Json response)
        System.out.println("verify if course title -Appium-Mobile Automation using Java present in entire response");
        for (int i=0;i<pojoParentObj.getCourses().getWebAutomation().size();i++)
        {
            if(pojoParentObj.getCourses().getWebAutomation().get(i).getCourseTitle().equalsIgnoreCase("Appium-Mobile Automation using Java"))
            {
               System.out.println(pojoParentObj.getCourses().getWebAutomation().get(i).getCourseTitle()+" " +
                       "is present in Automation Course ");
               break;
            }
        }
        for (int i=0;i<pojoParentObj.getCourses().getApi().size();i++)
        {
            if(pojoParentObj.getCourses().getApi().get(i).getCourseTitle().equalsIgnoreCase("Appium-Mobile Automation using Java"))
            {
                System.out.println(pojoParentObj.getCourses().getApi().get(i).getCourseTitle()+" is present in API Course ");
                break;
            }
        }
        for (int i=0;i<pojoParentObj.getCourses().getMobile().size();i++)
        {
            if(pojoParentObj.getCourses().getMobile().get(i).getCourseTitle().equalsIgnoreCase("Appium-Mobile Automation using Java"))
            {
                System.out.println(pojoParentObj.getCourses().getMobile().get(i).getCourseTitle()+" is present in Mobile Course ");
                break;
            }
        }
        //k. Print sum of web automation list course

        System.out.println("Print sum of web automation list course");
        int sumOfCoursePriceWebAutomation = 0;
        for(int i=0; i<pojoParentObj.getCourses().getWebAutomation().size();i++)
        {
            sumOfCoursePriceWebAutomation = sumOfCoursePriceWebAutomation + Integer.parseInt(pojoParentObj.getCourses().getWebAutomation().get(i).getPrice());
        }
        System.out.println("SumOfCoursePriceWebAutomation = "+sumOfCoursePriceWebAutomation);

        //l. Print sum of  API list course

        System.out.println("Print sum of API list course");
        int sumOfCoursePriceAPI = 0;
        for(int i=0; i<pojoParentObj.getCourses().getApi().size();i++)
        {
            sumOfCoursePriceAPI = sumOfCoursePriceAPI + Integer.parseInt(pojoParentObj.getCourses().getApi().get(i).getPrice());
        }
        System.out.println("SumOfCoursePriceAPI = "+sumOfCoursePriceAPI);

        //m. Print sum of mobile list course

        System.out.println("Print sum of mobile list course");
        int sumOfCoursePriceMobile = 0;
        for(int i=0; i<pojoParentObj.getCourses().getMobile().size();i++)
        {
            sumOfCoursePriceMobile = sumOfCoursePriceMobile + Integer.parseInt(pojoParentObj.getCourses().getMobile().get(i).getPrice());
        }
        System.out.println("SumOfCoursePriceMobile = "+sumOfCoursePriceMobile);

        //n. Print total course price in entire response
        System.out.println("SumOfAllCoursePrice = "+(sumOfCoursePriceWebAutomation+sumOfCoursePriceAPI+sumOfCoursePriceMobile));

        //o. Find total no. of course present in entire response
        // Doubt - tomorrow if another course add , so , since we are counting known coursesList(limitation of this Json response)
        int countWebAutomationCourse = 0;
        for(int i=0;i<pojoParentObj.getCourses().getWebAutomation().size();i++)
        {
            if(pojoParentObj.getCourses().getWebAutomation().get(i).getCourseTitle()!=null)
            {
                countWebAutomationCourse++;
            }
        }
        int countAPICourse = 0;
        for(int i=0;i<pojoParentObj.getCourses().getApi().size();i++)
        {
            if(pojoParentObj.getCourses().getApi().get(i).getCourseTitle()!=null)
            {
                countAPICourse++;
            }
        }
        int countMobileCourse = 0;
        for(int i=0;i<pojoParentObj.getCourses().getMobile().size();i++)
        {
            if(pojoParentObj.getCourses().getMobile().get(i).getCourseTitle()!=null)
            {
                countMobileCourse++;
            }
        }
        System.out.println("Total no. of course present in entire response = "+(countWebAutomationCourse+countAPICourse+countMobileCourse));

    }

}
