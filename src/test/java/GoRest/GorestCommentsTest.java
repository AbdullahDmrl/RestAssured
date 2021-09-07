package GoRest;

import GoRest.Model.Comments;
import GoRest.Model.CommentsBody;
import GoRest.Model.Post;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class GorestCommentsTest {


    @Test
    public void getCommentsList() {

        List<Comments> commentList=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .log().body()
                        .extract().jsonPath().getList("data", Comments.class)
                ;
        for (Comments c:commentList)
        {
            System.out.println("commentList = " + c);
        }
    }

    // Task 1: https://gorest.co.in/public/v1/comments  Api sinden dönen verilerdeki data yı bir nesne yardımıyla
    //         List olarak alınız.
    @Test
    public void getComments() {
        Response response =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        //.log().body()
                        .extract().response();

        List<Comments> listComments1 = response.jsonPath().getList("data");
        List<Comments> listComments2 = response.jsonPath().getList("data", Comments.class);

        System.out.println("listComments1 = " + listComments1); // itemleri nesne olarak görmüyor
        System.out.println("listComments2 = " + listComments2);

//        for (Comment c:  listComments1) { // ClassCastException
//            System.out.println("l1 c = " + c); //c.toString()
//        }

        for (Comments c:  listComments2) { // ClassCastException
            System.out.println("l2 c = " + c);
        }
    }

    // Task 2 Bütün Comment lardaki emailleri bir list olarak alınız ve
    // içinde "acharya_rajinder@ankunding.biz" olduğunu doğrulayınız.

    @Test
    public void getEmailList()
    {  // data[0].email  -> 1. email  , bütüm emailler için ise -> data.email
        List<String> emailList=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        //.log().body()
                        .extract().path("data.email");  // Stringlerden oluşan bir List<String>
        ;

        System.out.println("emailList = " + emailList);
        for(String s: emailList)
        {
            System.out.println("s = " + s);
        }
        Assert.assertTrue(emailList.contains("acharya_rajinder@ankunding.biz"));
    }

    // Ayni soruyu response ile yaparsak

    @Test
    public void getEmailListResponse()
    {
        Response response=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        //.log().body()
                        .extract().response();
        ;
        List<String> emailList=response.path("data.email");
        List<String> emailList2=response.jsonPath().getList("data.email", String.class); // String yazmak zorunda degiliz.

        System.out.println("emailList = " + emailList2);
        for(String s: emailList2)
        {
            System.out.println("s = " + s);
        }
        Assert.assertTrue(emailList2.contains("acharya_rajinder@ankunding.biz"));
    }

    // Task 3 : https://gorest.co.in/public/v1/comments  Api sinden
    // dönen bütün verileri tek bir nesneye dönüştürünüz

    @Test
    public void responseAllPOJO()

    {
        CommentsBody body =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/comments")

                        .then()
                        .log().body()
                        .extract().as(CommentsBody.class)
        ;
        System.out.println("body.getMeta().getPagination().getLinks() = " + body.getMeta().getPagination().getLinks());// burada sadece links leri aldim
        System.out.println("body.getMeta().getPagination() = " + body.getMeta().getPagination());// burada sadece Pagination leri aldim
        System.out.println("body.getMeta() = " + body.getMeta()); // meta olarak hepsini aldim

        System.out.println("body.getData() = " + body.getData());   // sadece data yi aldim
       
        // daha düzgün almak istersem

        List<Comments> dataList= body.getData();
       for (Comments c:dataList)
       {
           System.out.println("dataListElemet = " + c);
       }
        

    }






    int commentsId;
    String url="https://gorest.co.in/public/v1/comments";

    @Test()
    public void createComments() {
        Comments newComment=new Comments();
        newComment.setPost_id(createRandomInt());
        newComment.setName(createRandomName());
        newComment.setEmail(createRandomEmail());
        newComment.setBody(createRandomBody());

        commentsId=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body(newComment)
                        .when()
                        .post(url)
                        .then()
                        .statusCode(201)
                        .log().body()
                        .extract().jsonPath().getInt("data.id")
        ;
        System.out.println("commentsId = " + commentsId);
    }

    public int createRandomInt(){
        Random random=new Random();
        int max=100;
        int randomPost_Id=random.nextInt(max);
        return randomPost_Id;
    }

    public String createRandomName(){
        String randomName= RandomStringUtils.randomAlphabetic(4);
        return randomName;
    }

    public String createRandomBody(){
        String randomBody=RandomStringUtils.randomAlphabetic(6);
        return randomBody;
    }

    public String createRandomEmail(){
        String randomString= RandomStringUtils.randomAlphabetic(8).toLowerCase();
        return randomString+"@gmail.com";
    }


    @Test(dependsOnMethods = "createComments")
    public void updateCommentsByID() {

        String bodyText=
                given()
                        .header("Authorization","Bearer e4b725104d61c0ebaffa9eccfe772b0c0cba54dff360d019e2ceeeac90e63eea")
                        .contentType(ContentType.JSON)
                        .body("{\"body\":\"Merhaba Java\"}")
                        .pathParam("commentsID",commentsId)
                        .when()
                        .put(url+"/{commentsID}")
                        .then()
                        .log().body()
                        .statusCode(200)
                        .extract().path("data.body")
                ;

        System.out.println("bodyText = " + bodyText);
        Assert.assertTrue(bodyText.contains("Java"));

    }


}
