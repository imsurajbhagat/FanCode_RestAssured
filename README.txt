FanCode ----> Todo Automation Project

Project Overview
This project is written in Java using TestNG and RestAssured to interact with the JSONPlaceholder API to:

Retrieve users from the city of FanCode and Verify that each user has more than 50% of their todo tasks completed.

It includes:
A TestNG test case for verifying the scenario.
Hamcrest matchers for assertions.
Response pretty-printing for easy debugging.

Technologies Used
Java 8+: Programming language.
Maven: Dependency management and build automation.
RestAssured: Java library for simplifying testing of REST services.
TestNG: Testing framework.
Jackson (ObjectMapper): For JSON pretty-printing.
Hamcrest: for assertions.

FanCode.java: Contains the TestNG test that fetches users, their todos,
and checks if more than half of their todos are completed.

pom.xml: Maven configuration file for managing dependencies