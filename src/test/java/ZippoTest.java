import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ZippoTest {

    @Test
    public void Test() {
        given()
                // hazirlik islemlerini yapacagiz
                .when()
                // link ve aksiyon islemleri
                .then()
        // test extract islemleri
        ;
    }
    @Test
    public void statusCodeTest()
    {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .statusCode(200)
        ;
    }
    @Test
    public void contentTypeTest()
    {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .contentType(ContentType.JSON)
        ;
    }

    @Test
    public void logTest()
    {
        given()
                .log().all()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()

        ;
    }

    @Test
    public void checkStateInResponseBody()
    {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("country",equalTo("United States"))
                .statusCode(200)
        ;
    }

    @Test
    public void checkStateInResponseBody1()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places[0].state",equalTo("California"))
                .statusCode(200)
        ;
    }

    @Test
    public void checkStateInResponseBodyHasItem()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .body("places.state",hasItem("California"))
                .statusCode(200)

        // places[0].state -> listin 0 indexli elemanının state değerini verir, 1 değer
        //places.state ->    Bütün listteki state leri bir list olarak verir : California,California2   hasItem
        //             List<String> list= {'California','California2'}

        ;
    }

    @Test
    public void checkStateInResponseBodyHasItem1()
    {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()

                .body("places.'place name'",hasItem("Beverly Hills"))  // ayrim varsa tek tirnak aliyoruz
                .statusCode(200)
        ;
    }

    @Test
    public void checkStateInResponseBodyHasSize()
    {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places",hasSize(1))
                .statusCode(200)
        ;
    }

    @Test
    public void CombiningTest()
    {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .log().body()
                .body("places[0].state",equalTo("California"))
                .body("places.'place name'",hasItem("Beverly Hills"))
                .body("places",hasSize(1))
                .statusCode(200)
        ;
    }
    /*
    API ye parametre gönderme
    1.Yöntem : parametreler / ayıracı    ile metoda gönderiliyor
    http://api.zippopotam.us/us/90210    linki inceleyelim
    http://api.zippopotam.us->  API nin adresi
               /us/90210->  us ülke değişkenin değeri
                            90210 zipkodu
    2.Yöntem
    https://gorest.co.in/public/v1/users  API adresi
    https://gorest.co.in/public/v1/users?page=1&count=3
                                     page değişken adı  = sonrası değeri
                                     count değişken adı = sonrası değeri
    ? işareti sonrası gönderilecek değerler paramatere adı=değeri
                  yine ek parametre için & işareti ve yine adı ve değeri şeklinde gönderilir
    */

    @Test
    public void pathParamTest()  // 1 yöntem   Request URI:	http://api.zippopotam.us/us/90210
    {
        given()
                .pathParam("country","us")
                .pathParam("zipKod","90210")
                .log().uri()
                .when()
                .get("http://api.zippopotam.us/{country}/{zipKod}")
                .then()
                .log().body()
                .body("places",hasSize(1))
                .statusCode(200)
        ;
    }
    @Test
    public void pathParamTest1() {
        String country = "us";   // bu sekilde parametreleri tanimlayabiliriz
        int zipKod = 90210;

        given()
                .pathParam("country", country)
                .pathParam("zipKod", zipKod)
                .log().uri() //request linki

                .when()
                .get("http://api.zippopotam.us/{country}/{zipKod}")

                .then()
                .log().body()
                .body("places", hasSize(1))
        ;
    }

    @Test
    public void pathPramTestDongu() {
        String country = "us";

        for (int i = 90210; i < 90214; i++) {

            given()
                    .pathParam("country", country)
                    .pathParam("zipKod", i)
                    .log().uri()
                    .when()
                    .get("http://api.zippopotam.us/{country}/{zipKod}")
                    .then()
                    .log().body()
                    .body("places", hasSize(1))
                    .statusCode(200)
            ;
        }
    }
    @Test
    public void queryPramTest()      // 2 yöntem   //Request URI:	https://gorest.co.in/public/v1/users?page=1
    {
        given()
                .param("page",1)
                .log().uri()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .log().body()
                .body("meta.pagination.page",equalTo(1))
                .statusCode(200)
        ;
    }

    @Test
    public void queryParamTestCoklu()
    {
        for(int page=1;page<=10;page++) {
            given()
                    .param("page", page)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .log().body()
                    .body("meta.pagination.page", equalTo(page));
        }
        ;
    }


}