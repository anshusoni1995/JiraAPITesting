import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

public class JiraAPI {

    public static void main(String[] args){

        Login.Jiralogin();
        RestAssured.baseURI = "http://localhost:8080";
        given().log().all().header("Content-Type","application/json").header("Cookie","JSESSIONID=049D41B5ADF24CEEAB0111788F575ECA")
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "        \"project\": {\n" +
                        "            \"id\": \"10001\",\n" +
                        "            \"Key\": \"RESAUTO\"\n" +
                        "        },\n" +
                        "        \"summary\": \"Second Bug\",\n" +
                        "        \"description\" : \" this is our second bug created using api\",\n" +
                        "        \"issuetype\": {\n" +
                        "            \"name\": \"Bug\"\n" +
                        "        }\n" +
                        "    }\n" +
                        "}")
                .when().post("/rest/api/2/issue")
        .then().log().all().statusCode(201);
    }
}
