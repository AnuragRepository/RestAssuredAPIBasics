package eCommerceE2E;

import PojoE2EEcommerceFlow.*;
import UtilityPackage.Utility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class EcommerceE2EFlowTest {

    public  static String authorizationToken;
    public  static String userID;
    public static String productID;
    public static String orderID;

    @Test
    public void loginAPI()
    {
        //Login
        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(200)
                .expectBody("message",equalTo("Login Successfully")).build();


        PojoLoginRequest pojoLoginRequest = new PojoLoginRequest();
        pojoLoginRequest.setUserEmail("test486test486@gmail.com");
        pojoLoginRequest.setUserPassword("Automation@01");

        RequestSpecification loginRequest = given().log().all().spec(requestSpecification).body(pojoLoginRequest);


        PojoLoginResponse pojoLoginResponse = loginRequest.when().post("/api/ecom/auth/login")
                .then().log().all().spec(responseSpecification)
                .extract().response().as(PojoLoginResponse.class);

        userID = pojoLoginResponse.getUserId();
        System.out.println("userID = " +userID);
        authorizationToken = pojoLoginResponse.getToken();
        System.out.println("authorization = " +authorizationToken);
        String message = pojoLoginResponse.getMessage();
        System.out.println("message = " +message);

    }
    @Test(dependsOnMethods = "loginAPI")
    public void createProduct()
    {

       RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",authorizationToken).build();

        RequestSpecification requestCreateProduct = given().log().all().spec(requestSpecification)
                .param("productName","APIAddedProduct")
                .param("productAddedBy",userID)
                .param("productCategory","fashion")
                .param("productSubCategory","shirts")
                .param("productPrice","11500")
                .param("productDescription","Addias Originals")
                .param("productFor","women")
                .multiPart("productImage",new File(System.getProperty("user.dir")+"/src/main/java/UploadFolder/ProductUploadImage.jpg"));

       ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(201)
                        .expectBody("message",equalTo("Product Added Successfully"))
                                .build();

        PojoCreateProductResponse pojoCreateProductResponseObj =requestCreateProduct.when()
                .post("/api/ecom/product/add-product")
                .then().log().all()
                .spec(responseSpecification).extract().response().as(PojoCreateProductResponse.class);

        productID = pojoCreateProductResponseObj.getProductId();
        System.out.println("productID = "+productID);
        String successMessage = pojoCreateProductResponseObj.getMessage();
        System.out.println("Product Success Message = "+successMessage);

    }
    @Test(dependsOnMethods = {"createProduct","loginAPI"})
    public void createOrder()
    {
        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com/")
                .setContentType(ContentType.JSON)
                .addHeader("Authorization",authorizationToken)
                .build();

// VVI
        PojoCreateOrderChildRequest pojoCreateOrderChildRequestObj = new PojoCreateOrderChildRequest();
        pojoCreateOrderChildRequestObj.setCountry("India");
        pojoCreateOrderChildRequestObj.setProductOrderedId(productID);


        List<PojoCreateOrderChildRequest> orders = new ArrayList<PojoCreateOrderChildRequest>();
        orders.add(pojoCreateOrderChildRequestObj);

        //Since parentPojo class accept ProgramList ,
        // we created ProgramList with childPojo class signature and passed childPojo obj into list
        // so that it get to know set value and finally pass that list name into Parent Pojo class Obj

        PojoCreateOrderParentRequest pojoCreateOrderParentRequestObj = new PojoCreateOrderParentRequest();
        pojoCreateOrderParentRequestObj.setOrders(orders);


        RequestSpecification requestCreateOrder = given().log().all().spec(requestSpecification)
                .body(pojoCreateOrderParentRequestObj);

        ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(201)
                .expectBody("message", equalTo("Order Placed Successfully")).build();


        PojoCreateOrderResponse pojoCreateOrderResponseObj = requestCreateOrder.when().post("/api/ecom/order/create-order")
                .then().log().all().spec(responseSpecification).extract().response().as(PojoCreateOrderResponse.class);


        orderID = pojoCreateOrderResponseObj.getOrders().get(0);
        System.out.println("orderID = " +orderID);

        String fetchedProductID = pojoCreateOrderResponseObj.getProductOrderId().get(0);
        Assert.assertEquals(fetchedProductID,productID);

        String createOrderMessage = pojoCreateOrderResponseObj.getMessage();
        System.out.println("createOrderMessage = "+createOrderMessage);

    }

    @Test(dependsOnMethods = {"createOrder","createProduct","loginAPI"})
    public void viewOrderDetails()
    {
       RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addQueryParam("id",orderID)
                .addHeader("Authorization",authorizationToken).build();

        RequestSpecification requestOrderDetails = given().log().all().spec(requestSpecification);

        ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(200).build();


        PojoParentOrderDetails pojoParentOrderDetailsObj = requestOrderDetails.when().get("/api/ecom/order/get-orders-details")
                .then().log().all().spec(responseSpecification).extract().response().as(PojoParentOrderDetails.class);

        String fetchedOrderIDFromOrderDetails = pojoParentOrderDetailsObj.getData().get_id();
        System.out.println("fetchedOrderIDFromOrderDetails ="+fetchedOrderIDFromOrderDetails);
        Assert.assertEquals(fetchedOrderIDFromOrderDetails,orderID);

        String fetchedOrderByIDFromOrderDetails = pojoParentOrderDetailsObj.getData().getOrderById();
        System.out.println("fetchedOrderByIDFromOrderDetails ="+fetchedOrderByIDFromOrderDetails);

        String fetchedOrderByFromOrderDetails =  pojoParentOrderDetailsObj.getData().getOrderBy();
        System.out.println("fetchedOrderByFromOrderDetails ="+fetchedOrderByFromOrderDetails);

        String fetchedProductOrderedIdFromOrderDetails = pojoParentOrderDetailsObj.getData().getProductOrderedId();
        System.out.println("fetchedProductOrderedIdFromOrderDetails ="+fetchedProductOrderedIdFromOrderDetails);
        Assert.assertEquals(fetchedProductOrderedIdFromOrderDetails,productID);

        String fetchedProductNameFromOrderDetails = pojoParentOrderDetailsObj.getData().getProductName();
        System.out.println("fetchedProductNameFromOrderDetails ="+fetchedProductNameFromOrderDetails);

        String fetchedCountryFromOrderDetails = pojoParentOrderDetailsObj.getData().getCountry();
        System.out.println("fetchedCountryFromOrderDetails ="+fetchedCountryFromOrderDetails);

        String fetchedProductDescriptionFromOrderDetails = pojoParentOrderDetailsObj.getData().getProductDescription();
        System.out.println("fetchedProductDescriptionFromOrderDetails ="+fetchedProductDescriptionFromOrderDetails);

        String fetchedProductImageFromOrderDetails = pojoParentOrderDetailsObj.getData().getProductImage();
        System.out.println("fetchedProductImageFromOrderDetails ="+fetchedProductImageFromOrderDetails);

        String fetchedOrderPriceFromOrderDetails = pojoParentOrderDetailsObj.getData().getOrderPrice();
        System.out.println("fetchedOrderPriceFromOrderDetails ="+fetchedOrderPriceFromOrderDetails);

        String fetched__vFromOrderDetails = pojoParentOrderDetailsObj.getData().get__v();
        System.out.println("fetched__vFromOrderDetails ="+fetched__vFromOrderDetails);

       String orderDetailsSuccessMessage =  pojoParentOrderDetailsObj.getMessage();
       System.out.println("orderDetailsSuccessMessage = "+orderDetailsSuccessMessage);
       Assert.assertEquals(orderDetailsSuccessMessage,"Orders fetched for customer Successfully");

    }
    @Test(dependsOnMethods =  {"viewOrderDetails","createOrder","createProduct","loginAPI"})
    public void deleteProduct()
    {
        RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",authorizationToken)
                .addPathParam("key",productID).build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(200).
                expectBody("message",equalTo("Product Deleted Successfully")).build();

        RequestSpecification deleteRequest = given().log().all().spec(requestSpecification);

        PojoDeleteProductResponse pojoDeleteProduct = deleteRequest.when().delete("/api/ecom/product/delete-product/{key}")
                .then().log().all().spec(responseSpecification).extract().response().as(PojoDeleteProductResponse.class);

         String deleteSuccessMessageFromResponse = pojoDeleteProduct.getMessage();
         System.out.println("deleteSuccessMessageFromResponse = "+deleteSuccessMessageFromResponse);
         Assert.assertEquals(deleteSuccessMessageFromResponse,"Product Deleted Successfully");
    }

    @Test(dependsOnMethods = {"deleteProduct","viewOrderDetails","createOrder","createProduct","loginAPI"})
    public void deleteOrder() throws JsonProcessingException {
       RequestSpecification requestSpecification = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",authorizationToken)
                .addPathParam("key",orderID).build();

        RequestSpecification deleteOrderRequest = given().log().all().spec(requestSpecification);

        ResponseSpecification responseSpecification = new ResponseSpecBuilder().expectStatusCode(200)
                .expectBody("message",equalTo("Orders Deleted Successfully")).build();

        String deleteOrderResponse = deleteOrderRequest.when().delete("/api/ecom/order/delete-order/{key}")
                .then().log().all().spec(responseSpecification).extract().response().asString();

       JsonPath jsonPathObj = Utility.jsonPathUtility(deleteOrderResponse);
        String deleteOrderMessageViaJsonPath = jsonPathObj.getString("message");
        System.out.println("deleteOrderMessageViaJsonPath = "+deleteOrderMessageViaJsonPath);

        //Via Pojo Class
        // First convert already found string formatted response into Json using ObjectMapper Class
        ObjectMapper objectMapper = new ObjectMapper();
        PojoDeleteOrderResponse PojoDeleteOrderResponse = objectMapper.readValue(deleteOrderResponse,PojoDeleteOrderResponse.class);

        String deleteOrderMessageViaDeserialization  = PojoDeleteOrderResponse.getMessage();
        System.out.println("deleteOrderMessageViaDeserialization = "+deleteOrderMessageViaDeserialization);
    }

}
