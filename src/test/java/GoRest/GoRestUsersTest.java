package GoRest;

import GoRest.Model.User;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

    public class GoRestUsersTest {

        // siralamayi priority veya depends ile veriyoruz Burada toplu calistrsaydik;
        // priority ile siralama yapip depedns leri kaldirabilirdik


        int userId;

        @Test(enabled = false)
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

    @Test()
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

    @Test(dependsOnMethods = "createUser",priority = 1)
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

    @Test(dependsOnMethods = "createUser",priority = 2)
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

   @Test(dependsOnMethods = "createUser",priority = 3)
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

        @Test(dependsOnMethods = "deleteUserByID") // buna yazmiyorum cunku delete bagli.
                                                    // Ayni degere depends olanlari önceliklendiriyorum
        public void deleteUserByIDNegativ() {
            given()
                    .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                    .pathParam("userID",userId)
                    .when()
                    .delete("https://gorest.co.in/public/v1/users/{userID}")
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

     // Simdi body kismini daha basitlestirecek 2 yöntem anlatilacak
    //1.
        @Test()
        public void createNewUserBodyMap() {
          Map<String,String> newUser=new HashMap<>(); // verileri istersek Map olak gönderebiliriz
          newUser.put("name",getRandomName());        // Content.JSON  onlari JSON a cevirir
          newUser.put("gender","male");
          newUser.put("email",getRandomEmail());
          newUser.put("status","active");

           given()
                   .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                   .contentType(ContentType.JSON)
                   .body(newUser)  // bodye newUser olarak yaziyoruz
                   .when()
                   .post("https://gorest.co.in/public/v1/users")
                   .then()
                   .statusCode(201)
                   .contentType(ContentType.JSON)
                   .log().body()
            ;
        }
        //2.
        @Test()
        public void createNewUserBodyObjekt() {
            User newUser=new User();                // verileri istersek User bir nesne olak gönderebiliriz
            newUser.setName(getRandomName());     // Content.JSON  onlari JSON a cevirir
            newUser.setGender("male");
            newUser.setEmail(getRandomEmail());
            newUser.setStatus("active");

            given()
                    .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                    .contentType(ContentType.JSON)
                    .body(newUser)  // bodye newUser olarak yaziyoruz
                    .when()
                    .post("https://gorest.co.in/public/v1/users")
                    .then()
                    .statusCode(201)
                    .contentType(ContentType.JSON)
                    .log().body()
            ;
        }


}




