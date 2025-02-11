import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Main extends JFrame {
    static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    static final String DB_USER = "root";
    static final String DB_PASSWORD = "";

    private JTextField idField, nameField, ageField, emailField, phoneField;
    private JTable table;
    private DefaultTableModel model;

    public Main() {
        setTitle("Student Management System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ==== Input Panel ====
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        inputPanel.add(new JLabel("ID:"));
        idField = new JTextField();
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Age:"));
        ageField = new JTextField();
        inputPanel.add(ageField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        inputPanel.add(phoneField);

        add(inputPanel, BorderLayout.NORTH);

        // ==== Button Panel ====
        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Add Student");
        JButton viewButton = new JButton("View Students");
        JButton updateButton = new JButton("Update Student");
        JButton deleteButton = new JButton("Delete Student");

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.CENTER);

        // ==== Table Panel ====
        model = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Email", "Phone"}, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.SOUTH);

        // ==== Button Actions ====
        addButton.addActionListener(e -> addStudent());
        viewButton.addActionListener(e -> viewStudents());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
    }

    // ==== Add Student Method ====
    private void addStudent() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (id, name, age, email, phone) VALUES (?, ?, ?, ?, ?)")) {

            stmt.setInt(1, Integer.parseInt(idField.getText()));
            stmt.setString(2, nameField.getText());
            stmt.setInt(3, Integer.parseInt(ageField.getText()));
            stmt.setString(4, emailField.getText());
            stmt.setString(5, phoneField.getText());

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student Added Successfully!");
            viewStudents();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // ==== View Students Method ====
    private void viewStudents() {
        model.setRowCount(0); // Clear table
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students")) {

            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("id"), rs.getString("name"), rs.getInt("age"), rs.getString("email"), rs.getString("phone")});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // ==== Update Student Method ====
    private void updateStudent() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("UPDATE students SET name=?, age=?, email=?, phone=? WHERE id=?")) {

            stmt.setString(1, nameField.getText());
            stmt.setInt(2, Integer.parseInt(ageField.getText()));
            stmt.setString(3, emailField.getText());
            stmt.setString(4, phoneField.getText());
            stmt.setInt(5, Integer.parseInt(idField.getText()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student Updated Successfully!");
                viewStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Student Not Found!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    // ==== Delete Student Method ====
    private void deleteStudent() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id=?")) {

            stmt.setInt(1, Integer.parseInt(idField.getText()));

            int rows = stmt.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Student Deleted Successfully!");
                viewStudents();
            } else {
                JOptionPane.showMessageDialog(this, "Student Not Found!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}

// for Project run ==== java -cp ".;../lib/mysql-connector-java-9.1.0.jar" Main
// xaamp using for server
