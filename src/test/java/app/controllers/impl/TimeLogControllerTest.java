package app.controllers.impl;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.routes.Routes;
import app.security.routes.SecurityRoutes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.javalin.http.ContentType;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class TimeLogControllerTest {
    private static final String BASE_URL = "http://localhost:7777/api";
    private static EntityManagerFactory emfTest;
    private static ObjectMapper jsonMapper = new ObjectMapper();

    private static String securityToken;

    @BeforeAll
    static void setUpAll() {
        RestAssured.baseURI = BASE_URL;
        jsonMapper.findAndRegisterModules();

        HibernateConfig.setTest(true);
        Routes routes = new Routes();
        emfTest = HibernateConfig.getEntityManagerFactory();

        ApplicationConfig.getInstance()
                .initiateServer()
                .checkSecurityRoles()
                .setGeneralExceptionHandling()
                .setRoute(routes.getRoutes())
                .setRoute(new SecurityRoutes().getSecurityRoutes())
                .setRoute(SecurityRoutes.getSecuredRoutes())
                .setCORS()
                .startServer(7777);

        populator();
        register("user", "123");
        login();
    }

    @AfterAll
    static void afterAll() {
        HibernateConfig.setTest(false);
        ApplicationConfig.getInstance()
                .stopServer();
    }

    private static void populator() {
        given()
                .when()
                .accept("application/json")
                .header("Authorization", "Bearer " + securityToken)
                .header("Content-type", ContentType.JSON)
                .post("/populate")
                .then()
                .statusCode(201);
    }

    private static void register(String username, String password) {
        ObjectNode objectNode = jsonMapper.createObjectNode()
                .put("username", username)
                .put("password", password);
        String loginInput = objectNode.toString();
        securityToken = given()
                .contentType("application/json")
                .body(loginInput)
                .when().post("/auth/register")
                .then()
                .extract().path("token");
    }

    private static void login() {
        String userJson = """
                {
                    "username": "user",
                    "password": "123"
                }
                """;

        securityToken = given()
                .when()
                .contentType(ContentType.JSON)
                .body(userJson)
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("username", equalTo("user"))
                .body("token", is(notNullValue()))
                .extract().path("token");
    }

    @Test
    void read() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .accept(ContentType.JSON)
                .get("/candidates/1")
                .then()
                .statusCode(200)
                .body("candidate.id", equalTo(1));
    }

    @Test
    void readAll() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .accept(ContentType.JSON)
                .get("/candidates")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    void create() {
        String candidateJson = """
                {
                  "name": "Alice A.",
                  "phone": "87654321",
                  "educationBackground": "Software Engineering"
                }
                """;

        given()
                .when()
                .accept("application/json")
                .header("Authorization", "Bearer " + securityToken)
                .header("Content-type", ContentType.JSON)
                .body(candidateJson)
                .post("/candidates")
                .then()
                .statusCode(201)
                .body("name", equalTo("Alice A."))
                .body("phone", equalTo("87654321"))
                .body("educationBackground", equalTo("Software Engineering"));
    }

    @Test
    void update() {
        String updateJson = """
            {
              "id": 1,
              "name": "Alice A.",
              "phone": "87654321",
              "educationBackground": "Software Engineering"
            }
        """;

        given()
                .header("Authorization", "Bearer " + securityToken)
                .contentType(ContentType.JSON)
                .body(updateJson)
                .put("/candidates")
                .then()
                .statusCode(200)
                .body("name", equalTo("Alice A."))
                .body("phone", equalTo("87654321"))
                .body("educationBackground", equalTo("Software Engineering"));
    }

    @Test
    void delete() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .accept(ContentType.JSON)
                .delete("/candidates/2")
                .then()
                .statusCode(200)
                .body(containsString("Candidate with id 2 deleted"));
    }

    @Test
    void addSkill() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .accept(ContentType.JSON)
                .put("/candidates/1/skill/23")
                .then()
                .statusCode(200)
                .body(containsString("Skill added to candidate id: 1"));
    }

    @Test
    void readFromCategory() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .accept(ContentType.JSON)
                .get("/candidates?category=DB")
                .then()
                .statusCode(404)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    void highestAveragePopularity() {
        given()
                .header("Authorization", "Bearer " + securityToken)
                .accept(ContentType.JSON)
                .get("/reports/candidates/top-by-popularity")
                .then()
                .statusCode(200)
                .body("candidateId", greaterThan(0))
                .body("averagePopularityScore", greaterThanOrEqualTo(0));
    }
}