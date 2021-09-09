package GoRest;
import GoRest.Model.Post;
import GoRest.Model.PostsBody;
import GoRest.Model.Todos;
import GoRest.Model.TodosBody;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class GorestToDosTest {

    @BeforeClass
    public void starUp()
    {
        baseURI="https://gorest.co.in/public/v1";
    }

    //Task 1
    @Test(enabled = false)
    public void getTodosList() {

        List<Todos> todosList=
                given()
                        .when()
                        .get("/todos")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", Todos.class)
                ;
        //   System.out.println("postsList = " + postsList);
       for (Todos todo:todosList)
       {
           System.out.println("Todo = " + todo);
       }
    }

    // Task2 Bir user in todo larini alma
    @Test(enabled = false)
    public void getoneUserTodos() {
        List<Todos> usertodosList=
                given()
                        .when()
                        .get("/users/11/todos")
                        .then()
                        //.log().body()
                        .statusCode(200)
                        .extract().jsonPath().getList("data", Todos.class)
                ;
        for (Todos todo:usertodosList)
        {
            System.out.println("Todo user11 = " + todo);
        }
    }

    // Task 3 : https://gorest.co.in/public/v1/todos  API sinden dönen bütün bilgileri tek bir nesneye atınız

    @Test(enabled = false)
    public void todosAllPOJO()
    {
        TodosBody body =
                given()
                        .when()
                        .get("/todos")
                        .then()
                        //  .log().body()
                        .extract().as(TodosBody.class)
                ;
        System.out.println("body.getMeta() = " + body.getMeta());

        List<Todos> dataList= body.getData();
        for (Todos todo:dataList)
        {
            System.out.println("dataListElemet = " + todo);
        }
    }

    // Task 4 : https://gorest.co.in/public/v1/todos  API sine 90 nolu usera ait bir post create ediniz.

    int todosId;
    @Test()
    public void createTodos() {
       Todos  newTodo=new Todos();
       newTodo.setTitle("ISTQB Certified Tester Seminar");
       newTodo.setDue_on("2021-09-13T00:00:00.000+05:30");
       newTodo.setStatus("pending");

        todosId=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body(newTodo)
                        .when()
                        .post("/users/90/todos")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("todosId = " + todosId);
    }

    // Task 5 : Create edilen Todo ı get yaparak id sini kontrol ediniz.

    @Test(dependsOnMethods = "createTodos",priority = 1)
    public void getTodoByID() {

        given()
                .pathParam("todosID",todosId)
                .log().uri()
                .when()
                .get("/todos/{todosID}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id",equalTo(todosId))
               // .extract().path("data.user_id")
        ;

    }
    // Task 6 : Create edilen Todo nun title ini güncelleyerek, bilgiyi kontrol ediniz

    @Test(dependsOnMethods = "createTodos",priority = 2)
    public void updateTodoByID() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .contentType(ContentType.JSON)
                .body("{\"title\":\"ISTQB Prüfung\"}")
                .pathParam("todosID",todosId)
                .when()
                .put("/todos/{todosID}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.title",equalTo("ISTQB Prüfung"))
        ;
    }

    // Task 7 : Create edilen Comment ı siliniz. Status kodu kontorl ediniz 204

    @Test(dependsOnMethods = "createTodos",priority = 3)
    public void deleteTodoByID() {

        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("todosID",todosId)
                .when()
                .delete("/todos/{todosID}")
                .then()
                .statusCode(204)
        ;

    }
    // Task 8 : Silinen Comment ın negatif testini tekrar silmeye çalışarak yapınız.

    @Test(dependsOnMethods = "deleteTodoByID")
    public void deleteTodoByIDNegativ() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("todosID",todosId)
                .when()
                .delete("/todos/{todosID}")
                .then()
                .statusCode(404)
        ;
    }
}
