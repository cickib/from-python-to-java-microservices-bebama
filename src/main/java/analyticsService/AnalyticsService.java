package analyticsService;

import analyticsService.controller.DataController;
import analyticsService.controller.RenderController;
import analyticsService.controller.TrackingController;
import analyticsService.dao.JDBC.AbstractDaoJDBC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.resourceresolver.ClassLoaderResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;

import static spark.Spark.*;
import static spark.debug.DebugScreen.enableDebugScreen;

public class AnalyticsService {
    private static final Logger logger = LoggerFactory.getLogger(AnalyticsService.class);
    private static final int PORT = 60000;

    public static void main(String[] args) throws IOException {

        logger.debug("Starting server...");

        // --- SERVER SETUP ---
        staticFileLocation("/public");
        port(PORT);

        // --- DB CONNECTION SETUP ---
        PropertiesConfig.config();
        AbstractDaoJDBC.setConnection("connection.properties");

        // --- EXCEPTION HANDLING ---
        exception(URISyntaxException.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("URI building error, maybe wrong format? : %s", exception.getMessage()));
            logger.error("Error while processing request", exception);
        });

        exception(SQLException.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("SQL error, maybe DB server disconnected? : %s", exception.getMessage()));
            logger.error("Error while processing request", exception);
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(500);
            response.body(String.format("Unexpected error occurred: %s", exception.getMessage()));
            logger.error("Error while processing request", exception);
        });

        // --- TEMPLATE ENGINE ---
        TemplateResolver templateResolver = new TemplateResolver();
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(3600000L);
        templateResolver.setResourceResolver(new ClassLoaderResourceResolver());

        // --- ROUTES ---
        get("/api", new DataController()::api);
        get("/api/visitors", new DataController()::visitorCount);
        get("/api/times", new DataController()::visitTimeCount);
        get("/api/locations", new DataController()::locationVisits);
        get("/startTime", new TrackingController()::startSession);
        get("/stopTime", new TrackingController()::stopSession);
        post("/get_location", new TrackingController()::getData);
        get("/webshop", new RenderController()::renderWebshop, new ThymeleafTemplateEngine(templateResolver));
        get("/register", new RenderController()::renderRegister, new ThymeleafTemplateEngine(templateResolver));
        post("/register", new TrackingController()::registerWebshop);
        get("registered", new RenderController()::renderRegistered, new ThymeleafTemplateEngine(templateResolver));
        get("/", new RenderController()::renderMain, new ThymeleafTemplateEngine(templateResolver));


        enableDebugScreen();

        logger.info("AnalyticsService started on port " + PORT);

    }
}
