package Basqar;
import Basqar.Model.City;
import Basqar.Model.Country;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class CitiesTest {

    Cookies cookies;

    @BeforeClass
    public void logIn(){

      baseURI="https://demo.mersys.io";

        Map<String,String> credential=new HashMap<>();
        credential.put("username","richfield.edu");
        credential.put("password","Richfield2020!");
        credential.put("rememberMe","true");

      cookies= given()
                .contentType(ContentType.JSON)
                .body(credential)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract().response().getDetailedCookies()
        ;

    }


    String name= RandomStringUtils.randomAlphabetic(5);
    String id;
    @Test
    public void createCities(){
        Country country=new Country();
        country.setId("61476caa26d5662012788bbf");
        City city1=new City();
        city1.setName(name);
        city1.setCountry(country);
       Response response = given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(city1)
                .when()
                .post("/school-service/api/cities")

                .then()
                .log().body()
                .body("name",equalTo(name))
                .statusCode(201)
                .extract().response()
        ;
       city1.getCountry().id=response.jsonPath().getString("country.id");
       id=response.path("id");
       System.out.println("countryId = " + city1.getCountry().id);
       System.out.println("id = " + id);
    }

    @Test(dependsOnMethods = "createCities",priority = 1)
    public void createCitiesNegativ(){
        Country country=new Country();
        country.setId("61476caa26d5662012788bbf");
        City city1=new City();
        city1.setName(name);
        city1.setCountry(country);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(city1)
                .when()
                .post("/school-service/api/cities")

                .then()
                .body("message",equalTo("The City with Name \""+name+"\" already exists."))
                .log().body()
                .statusCode(400)
        ;

    }

    @Test(dependsOnMethods = "createCities",priority = 2)
    public void updateCityById(){
       String cityName=RandomStringUtils.randomAlphabetic(6);
        Country country=new Country();
        country.setId("61476caa26d5662012788bbf");
        City city1=new City();
        city1.setName(cityName);
        city1.setId(id);
        city1.setCountry(country);

        given()
                .cookies(cookies)
                .contentType(ContentType.JSON)
                .body(city1)
                .when()
                .put("/school-service/api/cities")
                .then()
                .log().body()
                .body("name",equalTo(cityName))
                //.body("countryId",equalTo(countryId))
                .body("id",equalTo(id))

                .statusCode(200)
        ;

    }

    @Test(dependsOnMethods = "createCities",priority = 3)
    public void deleteCityById(){
        given()
                .cookies(cookies)
                .pathParams("id",id)
                .log().uri()
                .when()
                .delete("/school-service/api/cities/{id}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }



    @Test(dependsOnMethods = "deleteCityById")
    public void deleteCityByIdNegativ(){
        given()
                .cookies(cookies)
                .pathParams("id",id)
                .log().all()
                .log().uri()
                .when()
                .delete("/school-service/api/cities/{id}")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }


}
