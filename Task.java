public class Task {
    private String name;
    private String description;
    private String dueDate;
    private String priority;
    private boolean completed;

    public Task(String name, String description, String dueDate, String priority, boolean completed) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "Name: " + name 
            + " | Desc: " + description
            + " | Due: " + dueDate 
            + " | Priority: " + priority 
            + " | " + (completed ? "Completed" : "Not Completed");
    }

    // Getters and setters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getDueDate() { return dueDate; }
    public String getPriority() { return priority; }
    public boolean isCompleted() { return completed; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
