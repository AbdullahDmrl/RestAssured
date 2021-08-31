
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class Task {
    /** Task 1
     * create a request to https://httpstat.us/203
     * expect status 203
     * expect content type TEXT
     */
    @Test
    public void Task1() {
        given()
                .when()
                .get("https://httpstat.us/203")
                .then()
                .statusCode(203)
                .contentType(ContentType.TEXT)
        ;
    }
        /** Task 2
         * create a request to https://httpstat.us/203
         * expect status 203
         * expect content type TEXT
         * expect BODY to be equal to "203 Non-Authoritative Information"
         */
        @Test
        public void Task2() {
    String text=
    given()
            .when()
            .get("https://httpstat.us/203")
            .then()
            .statusCode(203)
            .contentType(ContentType.TEXT)
           // .body(equalTo("203 Non-Authoritative Information"))  // aslinda böyle yapmaliyiz
            .extract().body().asString()
         ;
            Assert.assertEquals(text,"203 Non-Authoritative Information");
     // 2 yol
            given()
                    .when()
                    .get("https://httpstat.us/203")
                    .then()
                    .statusCode(203)
                    .contentType(ContentType.TEXT)
                 .body(equalTo("203 Non-Authoritative Information"))  // aslinda böyle yapmaliyiz
            ;
    }
        /** Task 3
         *  create a request to https://jsonplaceholder.typicode.com/todos/2
         *  expect status 200
         *  expect content type JSON
         *  expect title in response body to be "quis ut nam facilis et officia qui"
         */
        @Test
        public void Task3() {
            given()
                    .when()
                    .get("https://jsonplaceholder.typicode.com/todos/2")
                    .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("title",equalTo("quis ut nam facilis et officia qui"))
            ;
        }

    /** Task 4
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     *  expect status 200
     *  expect content type JSON
     *  expect response completed status to be false
     */
    @Test
    public void Task4() {

        given()

                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
               // .log().body()
               .body("completed",equalTo(false))
        ;
        // 2 yol
        boolean completed=
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        // .log().body()
                        //.body("completed",equalTo(false))
                        .extract().path("completed")
                ;
                Assert.assertFalse(completed);
    }

    /** Task 5
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * expect content type JSON
     * expect third item have:
     *      title = "fugiat veniam minus"
     *      userId = 1
     */

    @Test
    public void Task5() {
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                // .log().body()
                .body("userId[2]", equalTo(1))
                .body("title[2]", equalTo("fugiat veniam minus"))
        ;
    }

    /** Task 6
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * Converting Into POJO
     */

    @Test
    public void Task6() {
       Body body= given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .statusCode(200)
               .log().body()
                .extract().as(Body.class)
              

        ;
        System.out.println("body = " + body);

    }



    /** Task 7
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * Converting Array Into Array of POJOs
     */

    @Test
    public void Task7() {
        Body[] body=
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos")
                .then()
                .extract().as(Body[].class)
        ;
        System.out.println("body"+" "+ Arrays.toString(body));
        // diger bir yolu
        List<Body> bodies= Arrays.asList(body);
        System.out.println("bodies = " + bodies);
        }


    /** Task 8 - Ödev 2
     * create a request to https://jsonplaceholder.typicode.com/todos
     * expect status 200
     * Converting Array Into List of POJOs
     */
    @Test
    public void Task8() {
        Body[] body=
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos")
                        .then()
                        .statusCode(200)
                        .extract().as(Body[].class)
                ;
        System.out.println("body"+" "+ Arrays.toString(body));
        // diger bir yolu
        List<Body> bodies= Arrays.asList(body);
        System.out.println("bodies = " + bodies);
    }



}
