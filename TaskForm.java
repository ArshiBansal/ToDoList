import java.awt.*;
import javax.swing.*;

public class TaskForm extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JTextField dueDateField;
    private JComboBox<String> priorityBox;
    private JCheckBox completedCheck;
    private boolean submitted = false;
    private Task task;

    public TaskForm(JFrame parent, Task existingTask) {
        super(parent, existingTask == null ? "Add Task" : "Edit Task", true);
        setSize(300, 400);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0,1,5,5));

        nameField = new JTextField();
        descriptionArea = new JTextArea(3, 20);
        dueDateField = new JTextField();
        priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        completedCheck = new JCheckBox("Completed");

        if (existingTask != null) {
            nameField.setText(existingTask.getName());
            descriptionArea.setText(existingTask.getDescription());
            dueDateField.setText(existingTask.getDueDate());
            priorityBox.setSelectedItem(existingTask.getPriority());
            completedCheck.setSelected(existingTask.isCompleted());
        }

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        panel.add(new JLabel("Due Date:"));
        panel.add(dueDateField);
        panel.add(new JLabel("Priority:"));
        panel.add(priorityBox);
        panel.add(new JLabel("Completed:"));
        panel.add(completedCheck);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            submitted = true;
            task = new Task(
                nameField.getText(),
                descriptionArea.getText(),
                dueDateField.getText(),
                (String) priorityBox.getSelectedItem(),
                completedCheck.isSelected()
            );
            setVisible(false);
        });

        cancelButton.addActionListener(e -> {
            submitted = false;
            setVisible(false);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public Task getTask() {
        return task;
    }
}
