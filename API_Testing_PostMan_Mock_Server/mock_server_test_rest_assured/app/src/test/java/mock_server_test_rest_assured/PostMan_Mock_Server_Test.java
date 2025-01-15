package mock_server_test_rest_assured;

import static io.restassured.RestAssured.*;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class PostMan_Mock_Server_Test {

    private static final String BASE_URL = ""; //Add mock-server url from postman

    static String name = "Cario Sweet";
    static int age = 25;
    static String gender ="Female";
    static String grade = "First Class";
    static String course = "M.A English Literature";

    HashMap<String,Object> data = new HashMap<String,Object>();

    @Test(priority = 1)
     public void createMockStudent(){
        
        // HashMap "data" initialized as global variable

        data.put("name", name); // "name" declared as global variable
        data.put("age",age); // "age" declared as global variable
        data.put("gender", gender); // "grade" declared as global variable
        data.put("grade", grade); // "grade" declared as global variable
        data.put("course", course); // "course" declared as global variable

       Response response= given()
        .contentType("application/json")
        .body(data)

        .when()
        .post(BASE_URL+"/students");
       
        response.then()
        .statusCode(201)
        .log().status()
        .log().body();

        Assert.assertEquals(response.jsonPath().getString("student.name"), name);
        Assert.assertEquals(response.jsonPath().getInt("student.age"), age);
        Assert.assertEquals(response.jsonPath().getString("student.gender"), gender);
        Assert.assertEquals(response.jsonPath().getString("student.grade"), grade);
        Assert.assertEquals(response.jsonPath().getString("student.course"), course);
    }

    @Test(priority = 2)
    public void getStudents(){

        given()

        .when()
        .get(BASE_URL+"/students")

        .then()
        .statusCode(200)
        .log().status()
        .log().body();
    }    

    @Test(priority = 3)
    public void updateMockStudent(){

        String update_name = "Winnie";
        int update_age = 24;
        String updated_course = "BCA";
        
        data.replace("name", update_name);
        data.replace("age", update_age);
        data.replace("course", updated_course);

        Response response =given()
        .contentType("application/json")
        .body(data)

        .when()
        .put(BASE_URL+"/students/1");

        response.then()
        .statusCode(200)
        .log().body();

        Assert.assertNotEquals(name, response.jsonPath().getString("student.name"));
        Assert.assertNotEquals(age, response.jsonPath().getInt("student.age"));
        Assert.assertNotEquals(course, response.jsonPath().getString("student.course"));  
    }

    @Test(priority = 4)
    public void deleteMockStudent(){

        given()

        .when()
        .delete(BASE_URL+"/students/1")

        .then()
        .statusCode(200)
        .log().all();
    }

    @Test(priority = 5)
    public void getSingleMockStudent(){
        
        Response response =given()

        .when()
        .get(BASE_URL+"/students/1");

        response.then()
        .statusCode(200)
        .log().all();

        Assert.assertEquals(response.jsonPath().getString("name"), name);
        Assert.assertEquals(response.jsonPath().getInt("age"), age);
        Assert.assertEquals(response.jsonPath().getString("gender"), gender);
        Assert.assertEquals(response.jsonPath().getString("grade"), grade);
        Assert.assertEquals(response.jsonPath().getString("course"), course);
    }   
}