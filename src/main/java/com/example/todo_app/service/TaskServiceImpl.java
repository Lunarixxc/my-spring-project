package com.example.todo_app.service;

import com.example.todo_app.entity.Task;
import com.example.todo_app.entity.Priority; // Убедитесь, что импорт есть
import com.example.todo_app.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort; // <--- ИМПОРТ
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    private Sort createSort(String sortField, String sortDir) {
        // Сортировка по умолчанию, если sortField невалидный или не указан
        // (хотя контроллер задаст значения по умолчанию)
        if (sortField == null || sortField.isEmpty()) {
            sortField = "priority"; // Поле по умолчанию
        }
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "desc"; // Направление по умолчанию
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        // Добавим вторичную сортировку по createdAt для консистентности,
        // если основное поле сортировки имеет одинаковые значения
        if ("priority".equals(sortField)) {
            // Для приоритета сначала по самому приоритету, потом по дате для одинаковых приоритетов
            return Sort.by(direction, sortField).and(Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if ("createdAt".equals(sortField) || "description".equals(sortField)) {
            return Sort.by(direction, sortField);
        } else {
            // Если пришел неизвестный sortField, вернемся к дефолтной сортировке
            return Sort.by(Sort.Direction.DESC, "priority").and(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllActiveTasks(String sortField, String sortDir) {
        Sort sort = createSort(sortField, sortDir);
        return taskRepository.findByCompletedFalse(sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllCompletedTasks(String sortField, String sortDir) {
        Sort sort = createSort(sortField, sortDir);
        return taskRepository.findByCompletedTrue(sort);
    }

    // ... остальные методы без изменений ...
    @Override
    @Transactional(readOnly = true)
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    @Transactional
    public Task saveTask(Task task) {
        if (task.getPriority() == null) {
            task.setPriority(Priority.MEDIUM);
        }
        // createdAt устанавливается через @PrePersist
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTaskById(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Task toggleTaskCompletion(Long id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setCompleted(!task.isCompleted());
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Task not found with id: " + id);
        }
    }
}