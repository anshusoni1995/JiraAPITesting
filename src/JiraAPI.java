import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JiraAPI {

    public static void main(String[] args) {

        SessionFilter sessionFilter = Login.Jiralogin();
        Login.Jiralogin();
        RestAssured.baseURI = "http://localhost:8080";
        //given().log().all().header("Content-Type","application/json").header("Cookie","JSESSIONID="+value)
        String response = given().log().all().header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"fields\": {\n" +
                        "        \"project\": {\n" +
                        "            \"id\": \"10001\",\n" +
                        "            \"Key\": \"RESAUTO\"\n" +
                        "        },\n" +
                        "        \"summary\": \"Bug created from rest assured\",\n" +
                        "        \"description\" : \" this is our bug created using api\",\n" +
                        "        \"issuetype\": {\n" +
                        "            \"name\": \"Bug\"\n" +
                        "        }\n" +
                        "    }\n" +
                        "}")
                .filter(sessionFilter).when().post("/rest/api/2/issue")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();


        JsonPath js = new JsonPath(response);
        String id = js.get("id");

        //Add Comment to existing Jira
        given().log().all().pathParam("key",id).header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"body\": \"This is our comment from rest assured\"\n" +
                        "\n" +
                        "}")
                .filter(sessionFilter).post("/rest/api/2/issue/{key}/comment")
                .then().log().all().assertThat().statusCode(201);

        //Add Attachment
        given().log().all().pathParam("key",id).header("Content-Type","multipart/form-data")
                .header("X-Atlassian-Token","no-check")
                .multiPart("file",new File("src/JiraAttachment")).filter(sessionFilter)
                .when().post("/rest/api/2/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200);

        //Get Issue details
       String issuedetails = given().log().all().pathParam("key",id).filter(sessionFilter).queryParam("fields","comment")
                .when().get("/rest/api/2/issue/{key}")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();
       JsonPath js2 = new JsonPath(issuedetails);
       String commentid = js2.get("fields.comment.comments[0].id");
       System.out.println(commentid);
       System.out.println((String) js2.get("fields.comment.comments[0].body"));
    }
}
