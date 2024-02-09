# favorite_games

Description


This repository contains a Spring Boot application written in Java 17 for managing gamers and their gaming activities. The application provides endpoints to register gamers, manage their profiles.

Prerequisites

    JDK 17 installed
    Maven installed
    IDE (optional)

Setup

    1. Clone the repository: https://github.com/danielapetrescu/favorite_games
    2. Navigate to the project directory: cd favorite_games
    3. Build the project using Maven: mvn clean install
    4. Run the application: mvn spring-boot:run 
Or, if you have an IDE, you can run the GamersApplication class directly.

Usage

    Once the application is running, you can access it at http://localhost:4001.
    The following endpoints are available:
        1. "GET /api/gamers": Retrieve all gamers
        2. "GET /api/gamers/{id}": Retrieve a specific gamer by ID
        3. "POST /api/gamers": Register a new gamer (requires JSON payload)
        4. "PUT /api/gamers/{id}": Update an existing gamer (requires JSON payload)
        5. "PUT /api/gamers/{id}/defaultGames": Setup 5 arbitrary games for an existing gamer
        6. "PUT /api/gamers/{id}/gamerGamesAndLevels": Update games and levels for the gamert(requires JSON payload)
        7. "GET /api/match/{id}":Retrieves gamers matching criteria by game and level, game, or geography
        8. "PUT /api/credits/{id}/{gameName}/{credits}":Update or add gamer credits
        9. "GET /api/maxCredit/{game}/{level}":Get gamer with max credit for game and level

Configuration

    The application configuration can be found in the application.properties file, where you can modify properties such as server port, database settings, etc.

Dependencies

    Spring Boot Starter Web: for building web applications with Spring MVC
    Spring Boot Starter Data JPA: for accessing databases using Spring Data JPA
    H2 Database: an in-memory database for development and testing