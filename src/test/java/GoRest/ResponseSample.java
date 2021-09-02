package GoRest;

import GoRest.Model.User;
import io.restassured.http.ContentType;
import io.restassured.response.*;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ResponseSample {

    @Test()
    public void responseSample() {
        Response response=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()

                       // .log().body()
                        .extract().response()
        ;

        int toTal=response.jsonPath().getInt("meta.pagination.total");
        int limit=response.path("meta.pagination.limit");
        String firstUserName=response.jsonPath().getString("data[0].name");
        String secondUserName=response.path("data[1].name");
        List<String> userNameList=response.jsonPath().getList("data.name"); // isim listesini aldik
        List<String> genderList=response.path("data.gender");
        User firstUser=response.jsonPath().getObject("data[0]",User.class); // object olarak tum bilgilerini aldik
       // User frsUser=response.path("data[0]"); response icinde sipesifik bir objekt i path ile alamiyoruz

        /*
        tek bir değişken almak için
        path veya jsonPath kullanılabilir

        tüm veriye ihtiyacın varsa .as(Genel.class)  response
        kullanılacak

        verinin içinden bir bölümü bir clasa atmak
        istersen jsonPath kullanılacak

         */

        System.out.println("toTal = " + toTal);
        System.out.println("limit = " + limit);
        System.out.println("firstUserName = " + firstUserName);
        System.out.println("secondUserName = " + secondUserName);
        System.out.println("userNameList = " + userNameList);
        System.out.println("genderList = " + genderList);




    }




}
