package com.example.todo_app.service;

import com.example.todo_app.entity.Task;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getAllActiveTasks(String sortField, String sortDir);   // <--- ИЗМЕНЕНО

    List<Task> getAllCompletedTasks(String sortField, String sortDir); // <--- ИЗМЕНЕНО

    Optional<Task> getTaskById(Long id);
    Task saveTask(Task task);
    void deleteTaskById(Long id);
    Task toggleTaskCompletion(Long id);
}