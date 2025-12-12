package app.security.controllers;
import io.javalin.http.Handler;

public interface ISecurityController {
    Handler login();
    Handler register();
    Handler authenticate();
    Handler authorize();
}
