package GoRest;
import GoRest.Model.Post;
import GoRest.Model.PostsBody;
import GoRest.Model.Todos;
import GoRest.Model.TodosBody;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class GorestToDosTest {

    @BeforeClass
    public void starUp()
    {
        baseURI="https://gorest.co.in/public/v1";
    }

    // Task 1: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
    //         en büyük id sini bulunuz
    @Test
    public void getBiggestId() {
        List<Integer> idList=
                given()
                        .when()
                        .get("/todos")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                       // .log().body()
                        .extract().jsonPath().getList("data.id")
                ;
    // 1 yol
            int max=0;
        for (int a:idList){
            if(a>max)
                max=a;
        }
        System.out.println("max="+max);
    // 2 yol
        Collections.sort(idList);
        System.out.println("userIdList = " + idList.get(idList.size()-1));
           
    }
    // 2 yol

    @Test
    public void findBigIdOfTodos()
    {
        List<Todos> todoList=
                given()
                        .when()
                        .get("/todos")
                        .then()
                        //.log().body()
                        .extract().jsonPath().getList("data", Todos.class)
                ;
        int maxId=0;
        for(int i=0;i<todoList.size();i++)
        {
            if (todoList.get(i).getId() > maxId)
            {
                maxId=todoList.get(i).getId();
            }
        }
        System.out.println("maxId = " + maxId);
    }

    // Task 2: https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
    //         en büyük id ye sahip todo nun id sini BÜTÜN PAGE leri dikkate alarak bulunuz.


   // pages sayisi alttaki test ile aliniyor
    int pages;
    @Test
    public void getPages() {
        Response response=
                given()
                        .when()
                        .get("/todos")
                        .then()
                        // .log().body()
                        .extract().response()
                ;
        pages=response.path("meta.pagination.pages");
        System.out.println("pages = " + pages);
    }

    // Listelerin Listesi olusturulup her sayfadaki liste ona atiliyor
    List<List<Integer> >allPagesIdList=new ArrayList<>();
    @Test(dependsOnMethods = "getPages")
    public void biggestIdAllPages()
    {
        for(int page=1;page<=pages;page++) {
          // her sayfadaki id ler liste aliniyor
            List<Integer>  pageIdList=
            given()
                    .param("page", page)
                    //.log().uri()
                    .when()
                    .get("/todos")

                    .then()
                    //.log().body()
                    .extract().jsonPath().getList("data.id");

           // her sayfadaki id listler listelerin listesine atiliyor
            allPagesIdList.add(pageIdList);
        }
        System.out.println("AllIdList = " + allPagesIdList);

        // tüm id leri kapsayan listelerin listesinden max id bulunuyor
        int maxId=0;
        for(int i=0;i<allPagesIdList.size();i++)
        {
            for (int j = 0; j <allPagesIdList.get(i).size() ; j++) {
                if (allPagesIdList.get(i).get(j) > maxId)
                {
                    maxId=allPagesIdList.get(i).get(j);
                }
            }
        }
        System.out.println("maxId = " + maxId);
    }


    // Ayın sorusu : https://gorest.co.in/public/v1/todos  Api sinden dönen verilerdeki
    // zaman olarak ilk todo nun hangi userId ye ait olduğunu bulunuz

    @Test
    public void getfirstTodosUser_Ids() {
        List<Todos> todosList=
                given()
                        .when()
                        .get("/todos")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        // .log().body()
                        .extract().jsonPath().getList("data", Todos.class)
                ;

        List<String> dates=new ArrayList<>();
        //   Tarihlerin String list olarak alinmasi
        for (Todos todo:todosList)
        {
          String date=todo.getDue_on().substring(0,10).replace("-"," ");
          dates.add(date);
        }
        System.out.println("dates = " + dates);

        // Tarihlerin LocalDate e cevirilmesi
        DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy MM dd");
        List<LocalDate> dueDates=new ArrayList<>();
        for (int i = 0; i < dates.size(); i++) {
            LocalDate dueDate=LocalDate.parse(dates.get(i),format);
            dueDates.add(dueDate);
        }
        System.out.println("dueDates = " + dueDates);

        // Ilk todo nun bulunmasi ve user_id lerin alinmasi
        LocalDate firstDate=null;
        for (int i = 0; i < dueDates.size()-1; i++) {

            if (dueDates.get(i).isBefore(dueDates.get(i+1)))
              firstDate=dueDates.get(i);
        }
        System.out.println("firstDate = " + firstDate);
         // LocalDate in String e cevrilmesi
         String stringFirsDate=String.valueOf(firstDate);

        for (int i = 0; i < todosList.size(); i++) {
            if (todosList.get(i).getDue_on().contains(stringFirsDate))
                System.out.println("First Todo user_id = " + todosList.get(i).getUser_id()+" due_on ="+todosList.get(i).getDue_on());
        }
    }






    //Task 1
    @Test
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

    // Task 7 : Create edilen Todo yu siliniz. Status kodu kontorl ediniz 204

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
    // Task 8 : Silinen Todo ın negatif testini tekrar silmeye çalışarak yapınız.

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
