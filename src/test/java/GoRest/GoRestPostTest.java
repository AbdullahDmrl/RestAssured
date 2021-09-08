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
   // String url="https://gorest.co.in/public/v1/posts";
   // String url1="https://gorest.co.in/public/v1/users/123/posts";

    @Test(enabled = false)
    public void getPostList() {

        List<Post> postsList=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/posts")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
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
        newPost.setTitle("IT Testing");
        newPost.setBody("Prepare for the Restassured");
        postId=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body(newPost)
                        .when()
                        .post("https://gorest.co.in/public/v1/users/68/posts")
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("postId = " + postId);
    }

    @Test(dependsOnMethods = "createPost",priority = 1)
    public void getPostByID() {
        given()
                .pathParam("postID",postId)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/posts/{postID}")
                .then()
                .log().body()
                .statusCode(200)
                .body("data.id",equalTo(postId))
        ;
    }


    @Test(dependsOnMethods = "createPost",priority = 2)
    public void updatePostByID() {

                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body("{\"body\":\"Merhaba Java\"}")
                        .pathParam("postID",postId)
                        .when()
                        .put("https://gorest.co.in/public/v1/posts/{postID}")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .body("data.body",equalTo("Merhaba Java"))
                ;
    }

    @Test(dependsOnMethods = "createPost",priority = 3)
    public void deletePostByID() {
        given()
                .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                .pathParam("postID",postId)
                .when()
                .delete("https://gorest.co.in/public/v1/posts/{postID}")
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
                .delete("https://gorest.co.in/public/v1/posts/{postID}")
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
