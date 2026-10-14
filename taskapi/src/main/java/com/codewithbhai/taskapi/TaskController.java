
package com.codewithbhai.taskapi;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
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

    // 2. GET ALL TASKS
    @GetMapping
    public List<Map<String, Object>> getAllTasks() {
        return tasks;
    }

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
