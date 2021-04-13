import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.given;

public class Login {

    public static SessionFilter Jiralogin() {
        RestAssured.baseURI = "http://localhost:8080";
        SessionFilter session = new SessionFilter();
       String response =  given().log().all().header("Content-Type","application/json").body("{\n" +
                "    \"username\": \"anshusoni1995\",\n" +
                "    \"password\": \"Anshu@123\"}")
                .filter(session).when().post("/rest/auth/1/session")
                .then().log().all().statusCode(200).extract().response().asString();

        JsonPath js = new JsonPath(response);
      String name =  js.get("session.name");
        String value = js.get("session.value");
        System.out.println(name+":"+value);
        return session;
    }
}
