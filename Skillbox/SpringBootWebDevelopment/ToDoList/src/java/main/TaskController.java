package main;

import main.model.Task;

import main.model.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @PostMapping(value = "/tasks", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity add(@RequestBody Task task) {

        task.setIsDone(false);
        taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity getTask(@PathVariable int id) {

        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return new ResponseEntity(task, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public List<Task> getTasks() {

        Iterable<Task> tasksList = taskRepository.findAll();
        ArrayList<Task> tasks = new ArrayList<>();
        tasksList.forEach(task -> tasks.add(task));
        return tasks;
    }

    @PatchMapping(value = "/tasks/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity replacementTack(@PathVariable int id, @RequestBody Task newTask) {

        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (newTask.getIsDone() != null) {
            task.get().setIsDone(newTask.getIsDone());
        }

        if (newTask.getTitle() != null) {
            task.get().setTitle(newTask.getTitle());
        }

        if (newTask.getDescription() != null) {
            task.get().setDescription(newTask.getDescription());
        }
        taskRepository.save(task.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity deleteTask(@PathVariable int id) {

        Optional<Task> task = taskRepository.findById(id);
        if (!task.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        taskRepository.delete(task.get());
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
