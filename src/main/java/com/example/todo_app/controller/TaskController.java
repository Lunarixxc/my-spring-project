package com.example.todo_app.controller;

import com.example.todo_app.entity.Task;
import com.example.todo_app.entity.Priority;
import com.example.todo_app.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes; // Если не используется, можно убрать

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @ModelAttribute("allPriorities")
    public List<Priority> getAllPriorities() {
        return Arrays.asList(Priority.values());
    }

    @GetMapping({"", "/", "/active"})
    public String listActiveTasks(Model model,
                                  @RequestParam(name = "sortField", defaultValue = "priority") String sortField,
                                  @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {
        model.addAttribute("tasks", taskService.getAllActiveTasks(sortField, sortDir));
        model.addAttribute("pageView", "active");
        model.addAttribute("listTitle", "Активные задачи");
        // Передаем параметры сортировки в модель для использования в HTML
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "tasks/list-tasks";
    }

    @GetMapping("/completed")
    public String listCompletedTasks(Model model,
                                     @RequestParam(name = "sortField", defaultValue = "priority") String sortField,
                                     @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) {
        model.addAttribute("tasks", taskService.getAllCompletedTasks(sortField, sortDir));
        model.addAttribute("pageView", "completed");
        model.addAttribute("listTitle", "Выполненные задачи");
        // Передаем параметры сортировки в модель
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        return "tasks/list-tasks";
    }

    // ... методы new, edit, save, delete, toggle без изменений в части сортировки ...
    @GetMapping("/new")
    public String showNewTaskForm(Model model) {
        Task task = new Task();
        model.addAttribute("task", task);
        model.addAttribute("pageTitle", "Добавить Новую Задачу");
        return "tasks/task-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Optional<Task> taskOptional = taskService.getTaskById(id);
        if (taskOptional.isPresent()) {
            model.addAttribute("task", taskOptional.get());
            model.addAttribute("pageTitle", "Редактировать Задачу (ID: " + id + ")");
            return "tasks/task-form";
        } else {
            return "redirect:/tasks/active";
        }
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute("task") Task task) { // RedirectAttributes убрал, если не нужен
        taskService.saveTask(task);
        if (task.isCompleted()) {
            return "redirect:/tasks/completed";
        } else {
            return "redirect:/tasks/active";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id, @RequestParam(name = "sourceView", defaultValue = "active") String sourceView) {
        taskService.deleteTaskById(id);
        if ("completed".equals(sourceView)) {
            return "redirect:/tasks/completed";
        }
        return "redirect:/tasks/active";
    }

    @GetMapping("/toggle/{id}")
    public String toggleTaskCompletion(@PathVariable Long id) {
        Task updatedTask = taskService.toggleTaskCompletion(id);
        if (updatedTask.isCompleted()) {
            return "redirect:/tasks/completed";
        } else {
            return "redirect:/tasks/active";
        }
    }
}