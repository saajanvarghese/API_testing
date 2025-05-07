package rest_assured.Book_API;

import static io.restassured.RestAssured.*;

import io.restassured.response.*;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class BookAPI {

    static String author = "J.K. Rowling";
    static String category = "Fantasy";
    static double price = 19.99;
    static String title = "Harry Potter and the Chamber of Secrets";

    HashMap<String,Object> data = new HashMap<String,Object>();

    @Test(priority = 1)
     public void createBook(ITestContext context){
        
        // HashMap "data" initialized as global variable

        data.put("author", author); // "author" declared as global variable
        data.put("category",category); // "category" declared as global variable
        data.put("price", price); // "price" declared as global variable
        data.put("title", title); // "title" declared as global variable

        Response postResponse = given()
        .contentType("application/json")
        .body(data)
        .when()
        .post("http://localhost:3000/books");  // This is the Running JSON-Server URL
    
    postResponse.then().log().all();
    
    String id = postResponse.jsonPath().getString("id");
System.out.println("Generated id is " + id);
context.setAttribute("id", id); // store for later use
    }

    @Test(priority = 2)
    public void getBook(ITestContext context){

        String id = (String) context.getAttribute("id");

        Response response = given()
        .pathParam("id", id)

        .when()
        .get("http://localhost:3000/books/{id}");


        response.then()
        .statusCode(200)
        .log().all();

        Assert.assertEquals(response.jsonPath().getString("id"), id);
        Assert.assertEquals(response.jsonPath().getString("author"), author);
        Assert.assertEquals(response.jsonPath().getString("category"), category);
        Assert.assertEquals(response.jsonPath().getDouble("price"), price);
        Assert.assertEquals(response.jsonPath().getString("title"), title);
    }    

    @Test(priority = 3)
    public void updateBook(ITestContext context){
        String id = (String) context.getAttribute("id");

        String update_title = "Harry Potter and the Prisioner of Azkaban";
        double update_price = 22.00;
        
        data.replace("title", update_title);
        data.replace("price", update_price);

        Response response =given()
        .contentType("application/json")
        .pathParam("id", id)
        .body(data)

        .when()
        .put("http://localhost:3000/books/{id}");

        response.then()
        .statusCode(200)
        .log().body();

        Assert.assertEquals(response.jsonPath().getString("id"), id);
        Assert.assertNotEquals(response.jsonPath().getDouble("price"), price);
        Assert.assertNotEquals(response.jsonPath().getString("title"), price);
    }

    @Test(priority = 4)
    public void deleteBook(ITestContext context){
        String id = (String) context.getAttribute("id");

        given()
        .pathParam("id", id)

        .when()
        .delete("http://localhost:3000/books/{id}")

        .then()
        .statusCode(200);
    }

    @Test(priority = 5)
    public void getStoredBook(ITestContext context){
        String id = (String) context.getAttribute("id");
        
        given()
        .pathParam("id", id)

        .when()
        .get("http://localhost:3000/books/{id}")

        .then()
        .statusCode(404)
        .log().all();
    }
}