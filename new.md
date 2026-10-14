# Spring Boot Task Management CRUD REST API in IntelliJ IDEA

Bhai, let's build a complete **Spring Boot Task Management CRUD REST API in IntelliJ IDEA** from scratch. We will use only a Java controller, collections, and Spring Boot annotations.

The important requirement is that **we will NOT create a Model/Entity class or connect any database**. All tasks will be stored temporarily in an `ArrayList` using `Map`, just like a simple in-memory application.

# 1. What are we going to build?

Our application will allow users to create, view, update, and delete tasks.

Each task will contain:

| Field | Example |
|---|---|
| `id` | `1` |
| `title` | Learn Spring Boot |
| `description` | Practice REST APIs |
| `status` | Pending |
| `priority` | High |
| `dueDate` | 2026-10-20 |

We will implement these five endpoints:

| HTTP method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/tasks` | Create a task |
| GET | `/api/tasks` | Fetch all tasks |
| GET | `/api/tasks/1` | Fetch a specific task |
| PUT | `/api/tasks/1` | Update a task |
| DELETE | `/api/tasks/1` | Delete a task |

# 2. Create the Spring Boot project in IntelliJ

*Illustration: IntelliJ IDEA's New Project / Spring Boot wizard.*

## Step 1 — Project setup

**Step 1. Open IntelliJ IDEA**

Click **File → New → Project**. Select **Spring Boot** if it is available in the project wizard. IntelliJ also supports creating projects through Spring Initializr. (References in the original response: `turn452665search1`, `turn452665search2`.)

*Illustration: Spring Initializr project setup with Java, Maven, and Spring Web.*

**Step 2. Configure the project**

Use these settings:

| Setting | Value |
|---|---|
| Name | `task-crud-api` |
| Language | Java |
| Build system | Maven |
| Group | `com.example` |
| Artifact | `taskapi` |
| Package | `com.example.taskapi` |

Select a JDK and Spring Boot version that are compatible with each other.

*Illustration: Spring Initializr dependency selection for Spring Web.*

**Step 3. Add the dependency**

Select **Spring Web** and click Create or Generate.

We only need Spring Web for this application. Do not add Spring Data JPA, MySQL, PostgreSQL, or any database dependency. The official Spring guide also uses Spring Initializr with Spring Web to create a REST service. (References in the original response: `turn452665search0`, `turn452665search3`.)

If the Spring Boot wizard is unavailable in your IntelliJ edition, visit [Spring Initializr](https://start.spring.io), select the same settings, click **Generate**, extract the ZIP, and open that project in IntelliJ.

# 3. Understand the project structure

After creating the project, you should see a structure similar to this:

```text
task-crud-api
│
├── src
│   └── main
│       ├── java
│       │   └── com.example.taskapi
│       │       ├── TaskApiApplication.java
│       │       └── TaskController.java
│       │
│       └── resources
│           └── application.properties
│
└── pom.xml
```

You do not need to create a separate model, service, repository, or database configuration.

The generated `TaskApiApplication.java` is your main application class. We will create only one new file: `TaskController.java`.

# 4. Create the TaskController class

In IntelliJ:

1. Navigate to `src/main/java/com.example.taskapi`.
2. Right-click the package.
3. Select **New → Java Class**.
4. Enter `TaskController`.
5. Paste the following complete code.

## Complete CRUD code

```java
package com.example.taskapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // Temporary storage: no database and no Model class.
    private final List<Map<String, Object>> tasks = new ArrayList<>();
    private int nextId = 1;

    // 1. CREATE TASK
    @PostMapping
    public ResponseEntity<Map<String, Object>> createTask(
            @RequestBody Map<String, Object> request) {

        Map<String, Object> task = new LinkedHashMap<>();
        task.put("id", nextId++);
        task.put("title", request.get("title"));
        task.put("description", request.getOrDefault("description", ""));
        task.put("status", request.getOrDefault("status", "Pending"));
        task.put("priority", request.getOrDefault("priority", "Medium"));
        task.put("dueDate", request.getOrDefault("dueDate", ""));

        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

        ### Create Task
        POST http://localhost:8080/api/tasks
        Content-Type: application/json

        {
        "title": "Learn Spring Boot",
        "description": "Practice CRUD operations",
        "status": "Pending",
        "priority": "High",
        "dueDate": "2026-10-20"
        }


    // 2. GET ALL TASKS
    @GetMapping
    public List<Map<String, Object>> getAllTasks() {
        return tasks;
    }

    ### Get All Tasks
    GET http://localhost:8080/api/tasks 
    
    
    // 3. GET TASK BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable int id) {

        for (Map<String, Object> task : tasks) {
            if ((Integer) task.get("id") == id) {
                return ResponseEntity.ok(task);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // 4. UPDATE TASK
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(
            @PathVariable int id,
            @RequestBody Map<String, Object> request) {

        for (int i = 0; i < tasks.size(); i++) {
            Map<String, Object> existingTask = tasks.get(i);

            if ((Integer) existingTask.get("id") == id) {
                Map<String, Object> updatedTask = new LinkedHashMap<>();
                updatedTask.put("id", id);
                updatedTask.put("title", request.get("title"));
                updatedTask.put("description", request.getOrDefault("description", ""));
                updatedTask.put("status", request.getOrDefault("status", "Pending"));
                updatedTask.put("priority", request.getOrDefault("priority", "Medium"));
                updatedTask.put("dueDate", request.getOrDefault("dueDate", ""));

                tasks.set(i, updatedTask);
                return ResponseEntity.ok(updatedTask);
            }
        }

        return ResponseEntity.notFound().build();
    }

    // 5. DELETE TASK
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {

        for (int i = 0; i < tasks.size(); i++) {
            if ((Integer) tasks.get(i).get("id") == id) {
                tasks.remove(i);
                return ResponseEntity.noContent().build();
            }
        }

        return ResponseEntity.notFound().build();
    }
}
```

**Important:** If IntelliJ generated a different package, such as `com.example.demo`, change the package declaration at the top of this file to match your generated application package.

# 5. Understand the code step by step

## A. Why are we using `List<Map<String, Object>>`?

Normally, a developer might create a separate `Task` model class with fields such as `title`, `description`, `status`, and `priority`. You specifically asked not to create a model, so we use a Java `Map` instead.

```java
private final List<Map<String, Object>> tasks =
        new ArrayList<>();
```

Think of this as a temporary list of task records.

- `List` holds multiple tasks.
- `Map` stores each task's fields as key-value pairs.
- `String` represents field names such as `"title"`.
- `Object` allows different field values, such as integers and strings.
- `ArrayList` stores these task maps in memory.

A single task looks like this internally:

```java
Map<String, Object> task = new LinkedHashMap<>();

task.put("id", 1);
task.put("title", "Learn Spring Boot");
task.put("description", "Practice REST APIs");
task.put("status", "Pending");
task.put("priority", "High");
task.put("dueDate", "2026-10-20");
```

Spring Boot's JSON handling converts these map entries into a JSON response.

## B. What does `@RestController` do?

```java
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
```

`@RestController` tells Spring that this class handles HTTP requests and its method results should normally be sent back as response data, such as JSON.

`@RequestMapping("/api/tasks")` defines the common URL prefix for every endpoint inside this controller.

For example, a method using `@GetMapping("/{id}")` will handle:

```text
GET http://localhost:8080/api/tasks/1
```

## C. How does `@RequestBody` work without a model?

```java
public ResponseEntity<?> createTask(
        @RequestBody Map<String, Object> request)
```

Suppose a client sends this JSON:

```json
{
  "title": "Learn Spring Boot",
  "description": "Practice REST APIs",
  "status": "Pending",
  "priority": "High",
  "dueDate": "2026-10-20"
}
```

Spring reads the incoming JSON and makes its fields available through the `request` map.

You access them using:

```java
request.get("title");
request.get("description");
request.get("priority");
```

There is no need to define a separate Java class to receive the JSON.

## D. How does `@PathVariable` work?

```java
@GetMapping("/{id}")
public ResponseEntity<?> getTaskById(
        @PathVariable int id)
```

Here, `{id}` is a variable part of the URL.

When the client sends:

```text
GET /api/tasks/5
```

Spring passes the number `5` into the `id` parameter.

The method checks each task in the list directly. If no task has the requested ID, it returns HTTP `404 Not Found`.

## E. How does the application generate an ID?

```java
private int nextId = 1;
```

When creating a task, the controller assigns its ID directly:

```java
task.put("id", nextId++);
```

The first task gets ID `1`, the second gets ID `2`, and so on. Deleting a task does not reset the ID counter.

## F. Why use `ResponseEntity`?

`ResponseEntity` lets us return both the response data and an HTTP status code.

| Situation | Response code |
|---|---:|
| Task created | `201 Created` |
| Task fetched successfully | `200 OK` |
| Task updated successfully | `200 OK` |
| Task deleted successfully | `204 No Content` |
| Task does not exist | `404 Not Found` |

To keep this beginner-friendly version short, it does not include input validation. Send the correct status (`Pending`, `In Progress`, or `Completed`), priority (`Low`, `Medium`, or `High`), and due date (`YYYY-MM-DD`) values in your requests.

# 6. Run the application

Open the generated `TaskApiApplication.java` file. It should look similar to this:

```java
package com.example.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TaskApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                TaskApiApplication.class, args);
    }
}
```

This file is generated by Spring Initializr, so you do not need to create it again.

Now:

1. Open `TaskApiApplication.java`.
2. Click the green Run icon beside the `main()` method.
3. Wait until the console reports that the application has started.
4. The default server port is `8080`, unless you have configured a different port.

Your API is now available at `http://localhost:8080/api/tasks`.

# 7. Test all five CRUD operations

The easiest way to test the application directly from IntelliJ is to create an HTTP request file.

Right-click the project root, select **New → File**, and name it:

`tasks.http`

Paste the requests below into that file. IntelliJ provides a Run icon above each request to execute it.

## Operation 1: Create a task — POST

```http
### Create Task
POST http://localhost:8080/api/tasks
Content-Type: application/json

{
  "title": "Learn Spring Boot",
  "description": "Practice CRUD operations",
  "status": "Pending",
  "priority": "High",
  "dueDate": "2026-10-20"
}
```

Click the Run icon above this request.

Expected response: `201 Created`.

```json
{
  "id": 1,
  "title": "Learn Spring Boot",
  "description": "Practice CRUD operations",
  "status": "Pending",
  "priority": "High",
  "dueDate": "2026-10-20"
}
```

The task is now stored in the `tasks` list.

## Operation 2: Fetch all tasks — GET

```http
### Get All Tasks
GET http://localhost:8080/api/tasks
```

Expected response: `200 OK`.

```json
[
  {
    "id": 1,
    "title": "Learn Spring Boot",
    "description": "Practice CRUD operations",
    "status": "Pending",
    "priority": "High",
    "dueDate": "2026-10-20"
  }
]
```

The response is an array because the endpoint returns a list of tasks.

## Operation 3: Fetch one task — GET by ID

```http
### Get Task By ID
GET http://localhost:8080/api/tasks/1
```

Expected response: `200 OK`, containing only the task with ID `1`.

If you request an ID that doesn't exist, such as `999`, the response will be `404 Not Found`.

## Operation 4: Update a task — PUT

```http
### Update Task
PUT http://localhost:8080/api/tasks/1
Content-Type: application/json

{
  "title": "Master Spring Boot",
  "description": "Complete the REST API project",
  "status": "In Progress",
  "priority": "Medium",
  "dueDate": "2026-10-25"
}
```

Expected response: `200 OK`.

```json
{
  "id": 1,
  "title": "Master Spring Boot",
  "description": "Complete the REST API project",
  "status": "In Progress",
  "priority": "Medium",
  "dueDate": "2026-10-25"
}
```

We keep the original ID but replace the task's other fields with the submitted values. For this implementation, send the complete task details in the PUT body.

## Operation 5: Delete a task — DELETE

```http
### Delete Task
DELETE http://localhost:8080/api/tasks/1
```

Expected response: `204 No Content`.

There will be no response body. The task has been removed from the list.

Now run the following request again:

```http
### Verify Task Deleted
GET http://localhost:8080/api/tasks/1
```

Expected response:

```json
{
  "error": "Task with ID 1 not found."
}
```

The HTTP status will be `404 Not Found`.

# 8. Complete endpoint reference

## POST — Create a task

`/api/tasks`

Sends task details in the JSON body and returns the created task with an automatically assigned ID.

## GET — Fetch all tasks

`/api/tasks`

Returns every task currently stored in memory.

## GET — Fetch one task

`/api/tasks/{id}`

Finds one task using its ID, or returns `404` if it does not exist.

## PUT — Update a task

`/api/tasks/{id}`

Replaces the task details while preserving its ID.

## DELETE — Delete a task

`/api/tasks/{id}`

Removes the matching task from the list.

# 9. Important limitations of this project

Because you requested no database and no model class, this is a learning/demo implementation.

- **Data is temporary:** restarting Spring Boot empties the list and resets the ID counter.
- **No separate model:** the `Map` holds the task fields directly.
- **No database connection:** everything exists in application memory.
- **One controller handles everything:** it manages the five endpoints and in-memory storage, keeping the project simple.
- **No authentication:** anyone who can reach the API can access its endpoints unless additional security is added.

For practice, this is a good way to understand `@RestController`, `@RequestMapping`, `@PostMapping`, `@GetMapping`, `@PutMapping`, `@DeleteMapping`, `@RequestBody`, and `@PathVariable` without first learning databases or model classes.

**Your next step:** Create the project, paste `TaskController.java`, run the main class, and execute the five requests in `tasks.http` in order.
