package com.example.todo_app.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime; // <--- ДОБАВИТЬ ИМПОРТ

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // ID оставляем, он нужен для операций, просто не будем его выводить

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false)
    private boolean completed = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Priority priority = Priority.MEDIUM;

    @Column(nullable = false, updatable = false) // Не обновляется после создания
    private LocalDateTime createdAt; // <--- НОВОЕ ПОЛЕ

    // Конструкторы
    public Task() {
        // this.createdAt = LocalDateTime.now(); // Можно и здесь, но @PrePersist лучше
    }

    public Task(String description) {
        this.description = description;
        this.priority = Priority.MEDIUM;
        // this.createdAt = LocalDateTime.now();
    }

    public Task(String description, Priority priority) {
        this.description = description;
        this.priority = priority;
        // this.createdAt = LocalDateTime.now();
    }

    @PrePersist // Метод будет вызван перед сохранением новой сущности в БД
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() { // <--- ГЕТТЕР ДЛЯ createdAt
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) { // <--- СЕТТЕР ДЛЯ createdAt (может не понадобиться, если только при создании)
        this.createdAt = createdAt;
    }


    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", priority=" + priority +
                ", createdAt=" + createdAt + // Добавили createdAt
                '}';
    }
}