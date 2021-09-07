package GoRest;
import GoRest.Model.Post;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import static org.hamcrest.Matchers.*;
import static io.restassured.RestAssured.*;

public class GoRestPostTest {

    int postId;
    String url="https://gorest.co.in/public/v1/posts";

    @Test(enabled = false)
    public void getPostList() {

        List<Post> postsList=
                given()
                        .when()
                        .get(url)
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                       // .log().body()
                       .extract().jsonPath().getList("data", Post.class)
                ;
        //   System.out.println("postsList = " + postsList);
        for (Post post:postsList)
        {
            System.out.println("post = " + post);
        }
    }

    @Test()
    public void createPost() {
       Post newPost=new Post();
       newPost.setUser_id(createRandomInt());
       newPost.setTitle(createRandomTitle());
       newPost.setBody(createRandomBody());
        postId=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body(newPost)
                        .when()
                        .post(url)
                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("postId = " + postId);
    }

     @Test(priority = 1)
     public void updatePostByID() {
        Post updatePost=new Post();
        updatePost.setUser_id(createRandomInt());
        updatePost.setTitle(createRandomTitle());
        updatePost.setBody("Merhaba Java");

        String bodyText=
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .contentType(ContentType.JSON)
               // .body(updatePost)
                .body("{\"body\":\"Merhaba Java\"}")
                .pathParam("postID",postId)
                .when()
                .put(url+"/{postID}")
                .then()
                //.log().body()
                .statusCode(200)
                .extract().path("data.body")
        ;

        System.out.println("bodyText = " + bodyText);
        Assert.assertTrue(bodyText.contains("Java"));

    }

    @Test(priority = 2)
    public void getPostByID() {
        given()
                .pathParam("postID",postId)
                .log().uri()
                .when()
                .get(url+"/{postID}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id",equalTo(postId))
        ;
    }

    @Test(priority = 3)
    public void deletePostByID() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("postID",postId)
                .when()
                .delete(url+"/{postID}")
                .then()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods = "deletePostByID")
    public void deletePostByIDNegativ() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("postID",postId)
                .when()
                .delete(url+"/{postID}")
                .then()
                .statusCode(404)
        ;
    }

    public int createRandomInt(){
        Random random=new Random();
        int max=100;
        int randomUser_Id=random.nextInt(max);
        return randomUser_Id;
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
