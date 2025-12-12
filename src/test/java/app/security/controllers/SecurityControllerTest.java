package app.security.controllers;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class SecurityControllerTest {
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

        register("user", "123");
    }

    @BeforeEach
    void beforeEach() {
        login();
    }

    @AfterAll
    static void afterAll() {
        HibernateConfig.setTest(false);
        ApplicationConfig.getInstance()
                .stopServer();
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

    @Test
    void register() {
        String testJson = """
                {
                    "username": "test",
                    "password": "123"
                }
                """;

        given()
                .when()
                .contentType(ContentType.JSON)
                .body(testJson)
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("username", equalTo("test"));
    }


    @Test
    void login() {
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
    void addRole() {
        String roleJson = """
                {
                    "role": "admin"
                }
                """;

        given()
                .when()
                .accept("application/json")
                .header("Authorization", "Bearer " + securityToken)
                .header("Content-type", ContentType.JSON)
                .body(roleJson)
                .post("/auth/user/addrole/")
                .then()
                .statusCode(200)
                .body("msg", equalTo("Role admin added to user"))
                .body("action", equalTo("Login again to get admin access"));
    }
}