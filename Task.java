public class Task {
    private String category;
    private String description;
    private String dueDate;
    private String priority;
    private boolean completed;

    public Task(String category, String description, String dueDate, String priority, boolean completed) {
        this.category = category;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Category: " + category
            + " | Desc: " + description
            + " | Due: " + dueDate
            + " | Priority: " + priority
            + " | " + (completed ? "Completed" : "Not Completed");
    }

    public String getCategory() { return category; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setCategory(String category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
