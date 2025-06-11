package com.example.todo_app.entity; // Убедитесь, что имя пакета совпадает с вашим

public enum Priority {
    LOW("Низкий"),      // Низкий приоритет
    MEDIUM("Средний"),  // Средний приоритет
    HIGH("Высокий");    // Высокий приоритет

    private final String displayName;

    Priority(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}