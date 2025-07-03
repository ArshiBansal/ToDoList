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

        String[] columns = {"Category", "Description", "Due Date", "Priority", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        // Status dropdown
        String[] statusOptions = {"Pending", "Completed"};
        JComboBox<String> statusComboBox = new JComboBox<>(statusOptions);
        TableColumn statusColumn = taskTable.getColumnModel().getColumn(4);
        statusColumn.setCellEditor(new DefaultCellEditor(statusComboBox));

        // Row coloring
        taskTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String priority = (String) table.getValueAt(row, 3);
                if (!isSelected) {
                    switch (priority) {
                        case "High": c.setBackground(new Color(66, 165, 245)); break;
                        case "Medium": c.setBackground(new Color(144, 202, 249)); break;
                        case "Low": c.setBackground(new Color(227, 242, 253)); break;
                        default: c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(table.getSelectionBackground());
                }
                ((JLabel) c).setOpaque(true);
                return c;
            }
        });

        // Table model listener
        tableModel.addTableModelListener(e -> {
            int row = e.getFirstRow();
            int column = e.getColumn();
            if (column == 4 && row >= 0 && row < tasks.size()) {
                String status = (String) tableModel.getValueAt(row, 4);
                tasks.get(row).setCompleted("Completed".equals(status));
                taskTable.repaint();
            }
        });

        // Buttons and filter
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
                toEdit.setCategory(newTask.getCategory());
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
        if (row != -1 && row < tasks.size()) {
            openTaskForm(tasks.get(row));
        }
    }

    private void deleteSelectedTask() {
        int row = taskTable.getSelectedRow();
        if (row != -1 && row < tasks.size()) {
            tasks.remove(row);
            updateTable();
        }
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
                    t.getCategory(),
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
