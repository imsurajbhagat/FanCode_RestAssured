package com.rest;



import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class FanCode {

    // Base URL
    private final String BASE_URL = "http://jsonplaceholder.typicode.com";

    // Variables for FanCode City
    private  double LAT_MIN = -40;
    private  double LAT_MAX = 5;
    private  double LONG_MIN = 5;
    private  double LONG_MAX = 100;

    private final ObjectMapper objectMapper = new ObjectMapper();

    //  Users in FanCode City
    public List<Map<String, Object>> getUsersFromFanCodeCity() {
        Response response = RestAssured
                .get(BASE_URL + "/users")
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Map<String, Object>> users = response.jsonPath().getList("$");

        // Filter users by latitude and longitude for FanCode city
        List<Map<String, Object>> fanCodeUsers = new ArrayList<>();
        for (Map<String, Object> user : users) {
            Map<String, Object> address = (Map<String, Object>) user.get("address");
            if (address == null) continue;

            Map<String, String> geo = (Map<String, String>) address.get("geo");
            if (geo == null) continue;

            try {
                double lat = Double.parseDouble(geo.get("lat"));
                double lng = Double.parseDouble(geo.get("lng"));


                if (lat >= LAT_MIN && lat <= LAT_MAX && lng >= LONG_MIN && LONG_MAX >= lng) {
                    fanCodeUsers.add(user);
                }
            } catch (NumberFormatException e) {

            }
        }

        //  at least one user in the city of FanCode
        assertThat("Users in FanCode City should not be empty", fanCodeUsers, is(not(empty())));

        return fanCodeUsers;
    }

    // finding specific user by their user ID's
    public List<Map<String, Object>> getTodosForUser(int userId) {
        Response response = RestAssured
                .get(BASE_URL + "/todos?userId=" + userId)
                .then()
                .statusCode(200)
                .extract()
                .response();

        List<Map<String, Object>> todos = response.jsonPath().getList("$");

        // Validating that the user has todos
        assertThat("Todos list should not be empty", todos, is(not(empty())));

        return todos;
    }

    // user has completed more than 50% of todos
    public boolean isMoreThanHalfTodosCompleted(List<Map<String, Object>> todos) {
        int totalTasks = todos.size();
        int completedTasks = 0;


        for (Map<String, Object> todo : todos) {
            boolean completed = (boolean) todo.get("completed");
            if (completed) {
                completedTasks++;
            }
        }


        assertThat("Number of completed tasks should be greater than 50%", completedTasks, greaterThan(totalTasks / 2));

        return completedTasks > (totalTasks / 2);
    }


    private void prettyPrint(Object obj) {
        try {
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            System.out.println(prettyJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Verify only users in FanCode City with more than half todos completed
    @Test
    public void verifyUsersWithMoreThanHalfTodosCompletedInFanCodeCity() {
        List<Map<String, Object>> usersInFanCodeCity = getUsersFromFanCodeCity();

        System.out.println("Checking users in FanCode City who completed the half of their todos");

        for (Map<String, Object> user : usersInFanCodeCity) {
            int userId = (int) user.get("id");
            String userName = (String) user.get("name");

            List<Map<String, Object>> todos = getTodosForUser(userId);

            if (isMoreThanHalfTodosCompleted(todos)) {
                System.out.println("User " + userName + " has completed more than 50% of their todos----->");
                prettyPrint(user);
            } else {
                System.out.println("User " + userName + " has not completed more than 50% of their todos----->");
            }


            assertThat("User should have a valid ID", userId, greaterThan(0));
        }
    }
}
