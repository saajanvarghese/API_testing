package rest_assured_authorization_vs_authentication;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AppTest {
    private final String BASE_URL = "https://gorest.co.in/public/v2/users";
    private final String TOKEN = "Bearer ddb9580192c5dcad153b88308dcc72b4b7960116d4cc47bc5cec2d9f583ff4a9";

    static String name = "Pam Beesely";
    static String email = "pam_beesely." + System.currentTimeMillis() + "@sc.dms"; 
    static String gender = "female";
    static String status = "active";

    HashMap<String, Object> data = new HashMap<>();

    @Test(priority = 1)
    public void createUser(ITestContext context) {
        data.put("name", name);
        data.put("email", email);
        data.put("gender", gender);
        data.put("status", status);

        Response response = given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post(BASE_URL);

        response.then().log().status().log().body();
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 201, "User creation failed!");

        if (statusCode == 201) {
            String userId = response.jsonPath().getString("id");
            context.setAttribute("id", userId);
        }
    }

    @Test(priority = 2)
    public void getUserDetails(ITestContext context) {
        String id = (String) context.getAttribute("id");

        Response response = given()
                .header("Authorization", TOKEN)
                .pathParam("id", id)
                .when()
                .get(BASE_URL + "/{id}");

        response.then().statusCode(200).log().status().log().body();

        Assert.assertEquals(response.jsonPath().getString("id"), id);
        Assert.assertEquals(response.jsonPath().getString("name"), name);
        Assert.assertEquals(response.jsonPath().getString("email"), email);
        Assert.assertEquals(response.jsonPath().getString("gender"), gender);
        Assert.assertEquals(response.jsonPath().getString("status"), status);
    }

    @Test(priority = 3)
    public void updateUserDetails(ITestContext context) {
        String id = (String) context.getAttribute("id");

        // New updated values
        String updated_name = "Jim Harplet";
        String updated_email = "jim_harplet." + System.currentTimeMillis() + "@sc.dms";
        String updated_gender = "male";

        data.replace("name", updated_name);
        data.replace("email", updated_email);
        data.replace("gender", updated_gender);

        Response response = given()
                .header("Authorization", TOKEN)
                .pathParam("id", id)
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .put(BASE_URL + "/{id}");

        response.then().statusCode(200).log().status().log().body();

        Assert.assertEquals(response.jsonPath().getString("id"), id);
        Assert.assertEquals(response.jsonPath().getString("name"), updated_name);
        Assert.assertEquals(response.jsonPath().getString("email"), updated_email);
        Assert.assertEquals(response.jsonPath().getString("gender"), updated_gender);
    }

    @Test(priority = 4)
    public void deleteUser(ITestContext context) {
        String id = (String) context.getAttribute("id");

        given()
                .header("Authorization", TOKEN)
                .pathParam("id", id)
                .when()
                .delete(BASE_URL + "/{id}")
                .then()
                .statusCode(204)
                .log().status();
    }

    @Test(priority = 5)
    public void verifyUserDeleted(ITestContext context) {
        String id = (String) context.getAttribute("id");

        given()
                .header("Authorization", TOKEN)
                .pathParam("id", id)
                .when()
                .get(BASE_URL + "/{id}")
                .then()
                .statusCode(404)
                .log().status();
    }
}