import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

public class ToDoListApp extends JFrame {
    private DefaultTableModel tableModel;
    private JTable taskTable;
    private ArrayList<Task> tasks;
    private JComboBox<String> filterBox;

    public ToDoListApp() {
        setTitle("To-Do List");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tasks = new ArrayList<>();

        // Table setup with editable "Completed" dropdown
        String[] columns = {"Name", "Description", "Due Date", "Priority", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Only "Status" column is editable
                return column == 4;
            }
        };
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Make "Status" a dropdown in the table
        String[] statusOptions = {"Pending", "Completed"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        TableColumn statusColumn = taskTable.getColumnModel().getColumn(4);
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));

        // Renderer to change row color
        taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String status = (String) table.getValueAt(row, 4);
                if ("Completed".equals(status)) {
                    c.setBackground(new Color(200, 255, 200)); // light green
                } else {
                    c.setBackground(new Color(255, 230, 180)); // light orange
                }
                return c;
            }
        });

        // Listen for changes in "Completed" dropdown to update data
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 4 && row >= 0) {
                String name = (String) tableModel.getValueAt(row, 0);
                String status = (String) tableModel.getValueAt(row, 4);
                for (Task t : tasks) {
                    if (t.getName().equals(name)) {
                        t.setCompleted("Completed".equals(status));
                        break;
                    }
                }
                taskTable.repaint();
            }
        });

        // Buttons + filter at the TOP
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Add");
        JButton editButton = new JButton("Edit");
        JButton deleteButton = new JButton("Delete");
        filterBox = new JComboBox<>(new String[]{"All", "Completed", "Pending"});

        addButton.addActionListener(e -> openTaskForm(null));
        editButton.addActionListener(e -> editSelectedTask());
        deleteButton.addActionListener(e -> deleteSelectedTask());
        filterBox.addActionListener(e -> updateTable());

        topPanel.add(addButton);
        topPanel.add(editButton);
        topPanel.add(deleteButton);
        topPanel.add(new JLabel("Filter:"));
        topPanel.add(filterBox);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
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
            } else {
                tasks.add(newTask);
            }
            updateTable();
        }
    }

    private void editSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row != -1) {
            Task actualTask = getTaskAtRow(row);
            openTaskForm(actualTask);
        }
    }

    private void deleteSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row != -1) {
            Task selected = getTaskAtRow(row);
            tasks.remove(selected);
            updateTable();
        }
    }

    private Task getTaskAtRow(int row) {
        String name = (String) tableModel.getValueAt(row, 0);
        for (Task t : tasks) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        String filter = (String) filterBox.getSelectedItem();

        for (Task t : tasks) {
            boolean show = filter.equals("All") ||
                (filter.equals("Completed") && t.isCompleted()) ||
                (filter.equals("Pending") && !t.isCompleted());
            if (show) {
                tableModel.addRow(new Object[]{
                    t.getName(),
                    t.getDescription(),
                    t.getDueDate(),
                    t.getPriority(),
                    t.isCompleted() ? "Completed" : "Pending"
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDoListApp().setVisible(true));
    }
}
