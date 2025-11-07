package SampleJsonExtract;

import Payloads.Payload;
import UtilityPackage.Utility;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class JsonExtractTest {

   @Test
           public void fetchOutputFromResponse()
   {
       /*a. Print No of courses returned by API
        b. Print Purchase Amount
        c. Print Title of the first course
        d. Print All course titles and their respective Prices
        e. Print no of copies sold by RPA Course
        f. Verify if Sum of all Course prices matches with Purchase Amount*/

       String mockResponse = Payload.jsonBody();
       JsonPath js = Utility.jsonPathUtility(mockResponse);

       //Print No of courses returned by API
       int totalCourses = js.getInt("courses.size()");
       System.out.println("Total Courses = "+totalCourses);

       //Print Purchase Amount
       int totalpurchaseAmount = js.getInt("dashboard.purchaseAmount");
       System.out.println("Total Purchase Amount = "+totalpurchaseAmount);

       //Print Title of the first course
       String firstCourseTitle = js.getString("courses[0].title");
       System.out.println("First Course Title = "+firstCourseTitle);

       //Print All course titles and their respective Prices
       for(int i=0; i<totalCourses; i++)
       {
           String title = js.getString("courses["+i+"].title");
           int price = js.getInt("courses["+i+"].price");
           System.out.println("Course Title is "+title +" with price = "+price);
       }

       //Print no of copies sold by RPA Course
       for(int i=0;i<totalCourses;i++)
       {
           String courseTitle = js.getString("courses["+i+"].title");
           if(courseTitle.equalsIgnoreCase("RPA"))
           {
               int numberOfCopies = js.getInt("courses["+i+"].copies");
               System.out.println("Number Of Copies of "+courseTitle +" is "+numberOfCopies);
               break;
           }
       }
       //Verify if Sum of all Course prices matches with Purchase Amount
       int totalCoursePrice = 0;
       for(int i=0;i<totalCourses;i++)
       {
           int coursePrices = js.getInt("courses["+i+"].price");
           int courseCopies = js.getInt("courses["+i+"].copies");
           totalCoursePrice = totalCoursePrice + coursePrices * courseCopies;
       }
       System.out.println("Total Course Price = "+totalCoursePrice);
       Assert.assertEquals(totalCoursePrice,totalpurchaseAmount);

   }


}

