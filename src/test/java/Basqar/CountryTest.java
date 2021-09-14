package Basqar;

import Basqar.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Cookies;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class CountryTest {

   /*
   url : https://demo.mersys.io/auth/login
giden body :
{
    "username": "richfield.edu",
    "password": "Richfield2020!",
    "rememberMe": true
}



    */
   Cookies cookies;
    @BeforeClass
    public void loginBasqar(){
        baseURI="https://demo.mersys.io";

        Map<String,String> credential=new HashMap<>();  // Class yada MAP ile sent body yapilabillir
        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");

       cookies= given()
                .body(credential)
                .contentType(ContentType.JSON)
                .when()
                .post("/auth/login") // POST : token oluşturulduğu için

                .then()
                .statusCode(200)
              //  .log().body()
                .extract().response().getDetailedCookies()
        ;

    }

    String rangenName=RandomStringUtils.randomAlphabetic(5);
    String rangenCode=RandomStringUtils.randomAlphabetic(2);
    String countryId;

    @Test
    public void createCountry(){

        Country country=new Country();
        country.setName(rangenName);
        country.setCode(rangenCode);

     countryId=   given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")

                .then()
                .statusCode(201)
                .body("name",equalTo(rangenName))
                 .body("code",equalTo(rangenCode))
                .log().body()
                .extract().path("id")
      ;
    }

    @Test(dependsOnMethods = "createCountry",priority = 1)
    public void createCountryNegativ(){
        Country country=new Country();
        country.setName(rangenName);
        country.setCode(rangenCode);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .post("/school-service/api/countries")
                .then()
               .statusCode(400)
                .body("message",equalTo("The Country with Name \""+rangenName+"\" already exists."))
                .log().body()
        ;
    }

    @Test(dependsOnMethods = "createCountry",priority = 2)
    public void updateCountry() {
        String upgenName=RandomStringUtils.randomAlphabetic(5);
        String upgenCode=RandomStringUtils.randomAlphabetic(2);
        Country country=new Country();
        country.setName(upgenName);
        country.setCode(upgenCode);
        country.setId(countryId);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(country)
                .when()
                .put("/school-service/api/countries")
                .then()
                .statusCode(200)
                .log().body()
                .body("name",equalTo(upgenName))
                .body("code",equalTo(upgenCode))
                .body("id",equalTo(countryId))
        ;
    }

    @Test(dependsOnMethods = "createCountry",priority = 3)
    public void deleteCountrybyID() {
        given()
                .cookies(cookies)
                .pathParam("countryId",countryId)
                .when()
                .delete("/school-service/api/countries/{countryId}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }

    @Test(dependsOnMethods = "deleteCountrybyID")
    public void deleteCountrybyIDNegativ() {
        given()
                .cookies(cookies)
                .pathParam("countryId",countryId)
                .when()
                .delete("/school-service/api/countries/{countryId}")
                .then()
               // .log().body()
                .statusCode(404)
        ;
    }






}
