import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class ToDoListApp extends JFrame {
    private DefaultListModel<Task> listModel;
    private JList<Task> taskList;
    private ArrayList<Task> tasks;
    private JComboBox<String> filterBox;

    public ToDoListApp() {
        setTitle("To-Do List");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        tasks = new ArrayList<>();

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        JButton completeButton = new JButton("Mark Completed");
        filterBox = new JComboBox<>(new String[]{"All", "Completed", "Incomplete"});

        addButton.addActionListener(e -> openTaskForm(null));
        editButton.addActionListener(e -> editSelectedTask());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        completeButton.addActionListener(e -> markSelectedCompleted());
        filterBox.addActionListener(e -> updateList());

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(completeButton);
        buttonPanel.add(new JLabel("Filter:"));
        buttonPanel.add(filterBox);

        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openTaskForm(Task toEdit) {
        TaskForm form = new TaskForm(this, toEdit);
        form.setVisible(true);

        if (form.isSubmitted()) {
            Task newTask = form.getTask();
            if (toEdit != null) {
                toEdit.setName(newTask.getName());
                toEdit.setDescription(newTask.getDescription());
                toEdit.setDueDate(newTask.getDueDate());
                toEdit.setPriority(newTask.getPriority());
                toEdit.setCompleted(newTask.isCompleted());
            } else {
                tasks.add(newTask);
            }
            updateList();
        }
    }

    private void editSelectedTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            Task actualTask = listModel.get(index);
            openTaskForm(actualTask);
        }
    }

    private void deleteSelectedTask() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            Task selected = listModel.get(index);
            tasks.remove(selected);
            updateList();
        }
    }

    private void markSelectedCompleted() {
        int index = taskList.getSelectedIndex();
        if (index != -1) {
            Task selected = listModel.get(index);
            selected.setCompleted(true);
            updateList();
        }
    }

    private void updateList() {
        listModel.clear();
        String filter = (String) filterBox.getSelectedItem();

        for (Task t : tasks) {
            if (filter.equals("All") ||
                (filter.equals("Completed") && t.isCompleted()) ||
                (filter.equals("Incomplete") && !t.isCompleted())) {
                listModel.addElement(t);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListApp().setVisible(true));
    }
}
