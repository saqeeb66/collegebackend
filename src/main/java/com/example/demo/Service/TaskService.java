package com.example.demo.Service;

import com.example.demo.model.Task;
import com.example.demo.Repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Value("${upload.dir}")
    private String UPLOAD_DIR;

    // Save a new Task
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Get a specific Task by ID
    public Optional<Task> getTaskById(int taskId) {
        return taskRepository.findById(taskId);
    }

    // Update an existing Task
    public Optional<Task> updateTask(int taskId, Task task) {
        if (taskRepository.existsById(taskId)) {
            task.setTaskId(taskId);  // Ensure the ID stays the same
            return Optional.of(taskRepository.save(task));
        }
        return Optional.empty();
    }

    // Delete a Task by ID (includes file cleanup)
    public boolean deleteTask(int taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);

        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();

            try {
                // Delete associated video file if exists
                if (task.getRecordingVideo() != null) {
                    Path videoPath = Paths.get(UPLOAD_DIR, task.getRecordingVideo().replace("/uploads/", ""));
                    Files.deleteIfExists(videoPath);
                }

                // Delete associated audio file if exists
                if (task.getRecordingAudio() != null) {
                    Path audioPath = Paths.get(UPLOAD_DIR, task.getRecordingAudio().replace("/uploads/", ""));
                    Files.deleteIfExists(audioPath);
                }
            } catch (IOException e) {
                // Log error but proceed with task deletion
                System.err.println("Error deleting files for task ID " + taskId + ": " + e.getMessage());
            }

            // Delete task from the database
            taskRepository.deleteById(taskId);
            return true;
        }

        return false; // Task not found
    }
}
