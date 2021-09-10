package GoRest;
import GoRest.Model.*;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class GoRestPostTest {
    // Task 1 : https://gorest.co.in/public/v1/posts  API sinden dönen data bilgisini bir class yardımıyla
    // List ini alınız.

    // String url="https://gorest.co.in/public/v1/posts";
    // String url1="https://gorest.co.in/public/v1/users/123/posts";

    int postId;

    @Test(enabled = false)
    public void getPostList() {
        List<Post> postsList=
                given()
                        .when()
                        .get("/posts")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                       .extract().jsonPath().getList("data", Post.class)
                ;
        for (Post post:postsList)
        {
            System.out.println("post = " + post);
        }
    }
    // Task 2 : https://gorest.co.in/public/v1/posts  API sinden sadece 1 kişiye ait postları listeletiniz.
    //  https://gorest.co.in/public/v1/users/87/posts
    // Bir user in post larini alma

    @BeforeClass
     public void starUp()
    {
        baseURI="https://gorest.co.in/public/v1";
    }


    @Test(enabled = false)
    public void getoneUserPosts() {
        List<Post> userPostsList=
                given()
                        .when()
                        .get("/users/87/posts")
                        .then()
                       // .log().body()
                        .statusCode(200)
                        .extract().jsonPath().getList("data", Post.class)
                ;
        for (Post post:userPostsList)
        {
            System.out.println("User 87 = " + post);
        }
    }

    // Task 3 : https://gorest.co.in/public/v1/posts  API sinden dönen bütün bilgileri tek bir nesneye atınız

    @Test(enabled = false)
    public void postAllPOJO()
    {
        PostsBody body =
                given()
                        .when()
                        .get("/posts")
                        .then()
                      //  .log().body()
                        .extract().as(PostsBody.class)
                ;
        System.out.println("body.getMeta() = " + body.getMeta());

        List<Post> dataList= body.getData();
        for (Post p:dataList)
        {
            System.out.println("dataListElemet = " + p);
        }
    }

    // Task 4 : https://gorest.co.in/public/v1/posts  API sine 87 nolu usera ait bir post create ediniz.
    @Test()
    public void createPost() {
        Post newPost=new Post();
        newPost.setTitle("IT Testing");
        newPost.setBody("Prepare for the Restassured");
        postId=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body(newPost)
                        .when()
                        .post("/users/87/posts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .body("data.user_id",equalTo(87))
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("postId = " + postId);
    }

    @Test(enabled = false)
    public void createPost2() {
        Post newPost=new Post();
        newPost.setTitle("IT");
        newPost.setBody("Restassured");
        newPost.setUser_id(1235);
        postId=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body(newPost)
                        .when()
                        .post("/posts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .body("data.user_id",equalTo(1235))
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("postId = " + postId);
    }

    // Task 5 : Create edilen Post ı get yaparak id sini kontrol ediniz.

    @Test(dependsOnMethods = "createPost",priority = 1)
    public void getPostByID() {
        given()
                .pathParam("postID",postId)
               // .log().uri()
                .when()
                .get("/posts/{postID}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id",equalTo(postId))
        ;
    }

    // Task 6 : Create edilen Post un body sini güncelleyerek, bilgiyi kontrol ediniz
    @Test(dependsOnMethods = "createPost",priority = 2)
    public void updatePostByID() {
                 given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body("{\"body\":\"Merhaba Java\"}")
                        .pathParam("postID",postId)
                        .when()
                        .put("/posts/{postID}")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("data.body",equalTo("Merhaba Java"))
                ;
    }

    // Task 7 : Create edilen Post u delete ediniz, status kontrol ediniz

    @Test(dependsOnMethods = "createPost",priority = 3)
    public void deletePostByID() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("postID",postId)
                .when()
                .delete("/posts/{postID}")
                .then()
                .statusCode(204)
        ;
    }
    // Task 8 : Silinen Post ın negatif testini tekrar silmeye çalışarak yapınız.

    @Test(dependsOnMethods = "deletePostByID")
    public void deletePostByIDNegativ() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("postID",postId)
                .when()
                .delete("/posts/{postID}")
                .then()
                .statusCode(404)
        ;
    }


    public String createRandomTitle(){
        String randomTitle=RandomStringUtils.randomAlphabetic(4);
        return randomTitle;
    }

    public String createRandomBody(){
        String randomBody=RandomStringUtils.randomAlphabetic(6);
        return randomBody;
    }

}
