package com.example.demo.Controller;

import com.example.demo.model.Task;
import com.example.demo.Service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "http://localhost:3000") // CORS configuration for React frontend
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @Autowired
    private TaskService taskService;

    // Create a new task with file uploads
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<Task> createTask(
            @RequestParam("issue") String issue,
            @RequestParam("issueType") String issueType,
            @RequestParam("raisedBy") String raisedBy,
            @RequestParam("raisedStatus") String raisedStatus,
            @RequestParam("raisedOn") String raisedOn,
            @RequestParam(value = "recordingVideo", required = false) MultipartFile recordingVideo,
            @RequestParam(value = "recordingAudio", required = false) MultipartFile recordingAudio) {

        Task task = new Task();
        task.setIssue(issue);
        task.setIssueType(issueType);
        task.setRaisedBy(raisedBy);
        task.setRaisedStatus(raisedStatus);

        try {
            // Parse the raisedOn date string
            ZonedDateTime parsedDate = ZonedDateTime.parse(raisedOn, DATE_FORMATTER);
            task.setRaisedOn(parsedDate);

            // Ensure the upload directory exists
            File dir = new File(UPLOAD_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // Handle video upload
            if (recordingVideo != null && !recordingVideo.isEmpty()) {
                String videoFileName = System.currentTimeMillis() + "_" + recordingVideo.getOriginalFilename();
                Path videoPath = Paths.get(UPLOAD_DIR, videoFileName);
                Files.write(videoPath, recordingVideo.getBytes());
                task.setRecordingVideo("/uploads/" + videoFileName); // Store relative path
            }

            // Handle audio upload
            if (recordingAudio != null && !recordingAudio.isEmpty()) {
                String audioFileName = System.currentTimeMillis() + "_" + recordingAudio.getOriginalFilename();
                Path audioPath = Paths.get(UPLOAD_DIR, audioFileName);
                Files.write(audioPath, recordingAudio.getBytes());
                task.setRecordingAudio("/uploads/" + audioFileName); // Store relative path
            }

        } catch (IOException e) {
            logger.error("Error saving files: {}", e.getMessage());
            return ResponseEntity.status(500).body(null);
        } catch (Exception e) {
            logger.error("Error parsing date or processing task: {}", e.getMessage());
            return ResponseEntity.status(400).body(null);
        }

        Task savedTask = taskService.saveTask(task);
        return ResponseEntity.ok(savedTask);
    }

    // Retrieve all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        tasks.forEach(task -> {
            // Convert file paths to accessible URLs
            if (task.getRecordingVideo() != null && !task.getRecordingVideo().startsWith("http")) {
                task.setRecordingVideo("http://localhost:8080" + task.getRecordingVideo());
            }
            if (task.getRecordingAudio() != null && !task.getRecordingAudio().startsWith("http")) {
                task.setRecordingAudio("http://localhost:8080" + task.getRecordingAudio());
            }
        });
        return tasks;
    }

    // Retrieve a specific task by ID
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable int taskId) {
        Optional<Task> task = taskService.getTaskById(taskId);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a task by ID
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable int taskId) {
        boolean deleted = taskService.deleteTask(taskId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
