package GoRest;

import GoRest.Model.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GorestUsersURL {


    // burda toplu cal icin priority kullandin delete negativ haric
    // metot lari tek tek calistirmak icin depends lere ihtiyac var


    String url="https://gorest.co.in/public/v1/users";
    int userId;

    @Test(enabled = false)
    public void getUserListURL() {
        List<User> userList=
                given()
                        .when()
                        .get(url)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        //.log().body()
                        .extract().jsonPath().getList("data",User.class)
                ;
        System.out.println("userList = " + userList);
        // daha düzenli Istersek
        for (User u:userList){
            System.out.println("u = " + u);
        }
    }


    @Test()
    public void createUserURL() {
        userId=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\""+getRandomName()+"\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                        .when()
                        .post(url)
                        .then()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        //.log().body()
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("userId = " + userId);
    }

    @Test(priority = 1)
    public void updateUserByIDURL() {
        String name=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body("{\"name\":\""+getRandomName()+"\"}")    // ++ lari koymaliyiz
                        .pathParam("userID",userId)
                        .when()
                        .put(url+"/{userID}")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().jsonPath().getString("data.name")
                ;
        System.out.println("name = " + name);
    }

    @Test(priority = 2)
    public void getUserByIDURL() {
        given()
                .pathParam("userID",userId)
                .log().uri()
                .when()
                .get(url+"/{userID}")  //
                .then()
                //.log().body()
                .statusCode(200)
                .body("data.id",equalTo(userId))
        ;
        System.out.println("userId = " + userId);
    }

    @Test(priority = 3)
    public void deleteUserByIDURL() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("userID",userId)
                .when()
                .delete(url+"/{userID}")
                .then()
                .statusCode(204)
        ;

    }

    @Test(dependsOnMethods = "deleteUserByIDURL")
    public void deleteUserByIDURLNegativ() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("userID",userId)
                .when()
                .delete(url+"/{userID}")
                .then()
                .statusCode(404)
        ;

    }

    public String getRandomName(){
        String randomName= RandomStringUtils.randomAlphabetic(4);
        return randomName;
    }
    public String getRandomEmail(){
        String randomString= RandomStringUtils.randomAlphabetic(8).toLowerCase();
        return randomString+"@gmail.com";
    }





}
