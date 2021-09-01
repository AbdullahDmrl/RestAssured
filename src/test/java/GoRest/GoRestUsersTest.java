package GoRest;

import GoRest.Model.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.List;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

    public class GoRestUsersTest {

        String url="https://gorest.co.in/public/v1/users";
        int userId;

        @Test(priority = 1)
        public void getUserList() {

            List<User> userList=
            given()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")
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

    @Test(priority = 2)
    public void createUser() {
        userId=
            given()
                    .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                    .contentType(ContentType.JSON)
                    .body("{\"name\":\"abdullah\", \"gender\":\"male\", \"email\":\""+getRandomEmail()+"\", \"status\":\"active\"}")
                    .when()
                    .post("https://gorest.co.in/public/v1/users")
                    .then()
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    //   .log().body()
                    .extract().jsonPath().getInt("data.id")
            ;
        }

    @Test(priority = 3,dependsOnMethods = "createUser")
    public void updateUserByID() {

            given()
                    .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                    .contentType(ContentType.JSON)
                    .body("{\"name\":\"Kaan Demirel\"}")
                    .pathParam("userID",userId)

                    .when()
                    .put("https://gorest.co.in/public/v1/users/{userID}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("data.name",equalTo("Kaan Demirel"))
            ;
        }

    @Test(priority = 4, dependsOnMethods = "createUser")
    public void getUserByID() {
            given()
                    .pathParam("userID",userId)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users/{userID}")
                    .then()
                    .log().body()
                    .statusCode(200)
                    .body("data.id",equalTo(userId))
            ;
        }

   @Test(priority = 5, dependsOnMethods = "createUser")
   public void deleteUserByID() {
            given()
                    .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                    .pathParam("userID",userId)
                    .when()
                    .delete("https://gorest.co.in/public/v1/users/{userID}")
                    .then()
                    .statusCode(204)

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

        @Test(priority = 6)
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


   @Test(priority = 7)
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

   @Test(priority = 8,dependsOnMethods = "createUserURL")
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

   @Test(priority = 9,dependsOnMethods = "createUserURL")
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

   @Test(priority = 10,dependsOnMethods = "createUserURL")
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








}




