package com.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ebean.Ebean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;

import static spark.Spark.*;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        ObjectMapper om = new ObjectMapper();

        get("/health", (req, res) -> {
            res.header("Content-Type", "text/plain");
            return "OK";
        });
        get("/users", (req, res) -> listUsers(), om::writeValueAsString);
        post("/users", (req, res) -> createUser(req, res), om::writeValueAsString);

        before((request, response) -> response.type("application/json"));
    }

    private static List<UserRecord> listUsers() {
        return Ebean.find(UserRecord.class).findList();
    }

    private static UserRecord createUser(Request req, Response res) {
        try {
            UserRecord rec = new ObjectMapper().readValue(req.body(), UserRecord.class);
            Ebean.save(rec);
            return rec;
        } catch (Exception e) {
            logger.error("Unable to create new user", e);
            res.status(500);
            return null;
        }
    }

    // https://sparktutorials.github.io/2015/08/24/spark-heroku.html
    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
