import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;

public class TaskForm extends JDialog {
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JComboBox<Integer> dayBox;
    private JComboBox<String> monthBox;
    private JComboBox<Integer> yearBox;
    private JComboBox<String> priorityBox;
    private boolean submitted = false;
    private Task task;

    public TaskForm(JFrame parent, Task existingTask) {
        super(parent, existingTask == null ? "Add Task" : "Edit Task", true);
        setSize(350, 400);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(0,1,5,5));

        nameField = new JTextField();
        descriptionArea = new JTextArea(3, 20);

        // Day combo box
        dayBox = new JComboBox<>();
        for (int d = 1; d <= 31; d++) {
            dayBox.addItem(d);
        }

        // Month combo box
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                           "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        monthBox = new JComboBox<>(months);

        // Year combo box
        yearBox = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int y = currentYear - 5; y <= currentYear + 5; y++) {
            yearBox.addItem(y);
        }

        priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});

        if (existingTask != null) {
            nameField.setText(existingTask.getName());
            descriptionArea.setText(existingTask.getDescription());
            priorityBox.setSelectedItem(existingTask.getPriority());

            // Parse due date
            String[] parts = existingTask.getDueDate().split("-");
            if (parts.length == 3) {
                dayBox.setSelectedItem(Integer.parseInt(parts[0]));
                monthBox.setSelectedItem(parts[1]);
                yearBox.setSelectedItem(Integer.parseInt(parts[2]));
            }
        }

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(new JScrollPane(descriptionArea));
        panel.add(new JLabel("Due Date:"));

        JPanel datePanel = new JPanel();
        datePanel.add(dayBox);
        datePanel.add(monthBox);
        datePanel.add(yearBox);
        panel.add(datePanel);

        panel.add(new JLabel("Priority:"));
        panel.add(priorityBox);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            submitted = true;
            String dueDate = dayBox.getSelectedItem() + "-" +
                             monthBox.getSelectedItem() + "-" +
                             yearBox.getSelectedItem();
            task = new Task(
                nameField.getText(),
                descriptionArea.getText(),
                dueDate,
                (String) priorityBox.getSelectedItem(),
                existingTask != null && existingTask.isCompleted()
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
