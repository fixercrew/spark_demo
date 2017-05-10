package com.fixer;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ebean.Ebean;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.List;

import static spark.Spark.*;

public class UserService {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());

        ObjectMapper om = new ObjectMapper();
        get("/hello", (req, res) -> "Hello World");
        get("/users", (req, res) -> listUsers(), om::writeValueAsString);
        post("/users", (req, res) -> createUser(req, res), om::writeValueAsString);

        before((request, response) -> response.type("application/json"));

        System.out.println("Server running at " + port());
    }

    private static List<UserRecord> listUsers() {
        return Ebean.find(UserRecord.class).findList();
    }

    private static UserRecord createUser(Request req, Response res) {
        try {
            UserRecord rec = new ObjectMapper().readValue(req.body(), UserRecord.class);
            Ebean.save(rec);
            return rec;
        } catch (IOException e) {
            e.printStackTrace();
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

    private static String word() {
        int len = 3 + (int)(Math.random() * 4);
        String name = "";
        for (int ii=0; ii<len; ii++) {
            name += (char)('a' + (int)(Math.random()*26));
            if (name.length() == 1) {
                name = name.toUpperCase();
            }
        }
        return name;
    }
}
