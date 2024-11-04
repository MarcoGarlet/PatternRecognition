# PatternRecognition

This project provides a solution to the Pattern Recognition challenge.
The solution leverages PostgreSQL as a database and Docker for containerization.

## Table of Contents

- [Usage](#usage)
- [Architecture](#architecture)
- [Endpoints](#endpoints)
- [Run Tests Locally](#run-tests-locally)
- [Generate Documentation](#generate-documentation)
- [Solution Details](#solution-details)
- [Future Improvements](#future-improvements)

## Usage

To run the application, clone the repository and use Docker Compose:

```bash
docker compose up -d
```

This command will spin up two containers:

- **API container**: Hosts the Spring Boot RESTful API.
- **Database container**: Hosts a PostgreSQL instance.

The API is exposed on port `8080` (or your specified configuration), and the database is exposed on port `5432`.

## Architecture

The application is built using:

- **Spring Boot** for the RESTful API.
- **PostgreSQL** for data storage.
- **Docker Compose** for managing containerized deployment.

### Project Structure

The main packages include:

- `controller`: Contains the REST controllers that define the API endpoints.
- `entity`: Defines the `Point` entity which represents a point in 2D space.
- `repository`: Interfaces with the database to perform CRUD operations on points.
- `utils`: Contains helper classes like `MathUtils` to handle floating-point comparisons.

## Endpoints

### `POST /points`

Creates a new point in the database. Accepts a JSON payload with `x` and `y` coordinates.

**Request Body Example**:

```json
{
  "x": 12.34,
  "y": 56.78
}
```

### `GET /space`

Retrieves all points in the database.

### `DELETE /space`

Deletes all points in the database.

### `GET /lines/{n}`

Finds line segments that pass through at least `n` points. Returns a list of unique lines, where each line is represented by the points that lie on it.

## Run Tests Locally

To run the tests locally, use the following command:

```bash
docker exec patternrecognition-app-1 sh -c "./gradlew test"
```

This command will execute the test suite inside the API container.

## Generate Documentation

To generate Javadoc for the project, use:

```bash
docker exec patternrecognition-app-1 sh -c "./gradlew javadoc"
```

The documentation will be generated in the `build/docs/javadoc` directory within the container.

## Solution Details

The core challenge was implementing the `/lines/{n}` endpoint, which identifies line segments passing through at least `n` points. Here is an overview of the approach:

- **Identify a Line**: Two points uniquely define a line, but two different points can represent the same line if they share the same slope and intercept.
- **Line Equality Implementation**: I created a custom `Line` class, which defines a line segment using its slope and intercept. A tolerance-based equality check was implemented to manage floating-point precision issues (following IEEE 754 standards). This check is consistently applied in all equality comparisons involving double values within the tests.
- **Point Grouping by Line**: To group points that lie on the same line, a hashmap was used with `Line` objects as keys. The `Line` class was designed to represent each unique line segment by its slope and intercept. By overriding the equals and hashCode methods in `Line`, line segments with the same slope and intercept are considered equal, allowing the hashmap to correctly group points that belong to the same line segment. This structure makes it easy to identify and retrieve all points lying on each unique line segment.
- **Time Complexity**: For $n$ points the time complexity will be $O(n^2)$ because the algorithm needs to compute the line for each pair of points (instances of the Point class).
