//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.sql.*;
import java.util.Scanner;

public class Main{

    private static final String URL = "jdbc:mysql://localhost:3306/tododb";
    private static final String USER = "root";
    private static final String PASSWORD = "Abishek@2004";

    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Scanner sc = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n--- TO-DO LIST MENU ---");
                System.out.println("1. Add Task");
                System.out.println("2. View Tasks");
                System.out.println("3. Update Task");
                System.out.println("4. Delete Task");
                System.out.println("5. Exit");
                System.out.print("Choose option: ");
                int choice = sc.nextInt();
                sc.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> addTask(conn, sc);
                    case 2 -> viewTasks(conn);
                    case 3 -> updateTask(conn, sc);
                    case 4 -> deleteTask(conn, sc);
                    case 5 -> {
                        System.out.println("Goodbye!");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 1. Add Task
    private static void addTask(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter task name: ");
        String taskName = sc.nextLine();

        String sql = "INSERT INTO tasks (task_name) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, taskName);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Task added successfully!" : "Failed to add task.");
        }
    }

    // 2. View Tasks
    private static void viewTasks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM tasks";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\n--- Task List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Task: %s | Status: %s%n",
                        rs.getInt("id"), rs.getString("task_name"), rs.getString("status"));
            }
        }
    }

    // 3. Update Task
    private static void updateTask(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter task ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Enter new task name: ");
        String taskName = sc.nextLine();

        System.out.print("Enter new status (Pending/Done): ");
        String status = sc.nextLine();

        String sql = "UPDATE tasks SET task_name = ?, status = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, taskName);
            ps.setString(2, status);
            ps.setInt(3, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Task updated successfully!" : "No task found with given ID.");
        }
    }

    // 4. Delete Task
    private static void deleteTask(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter task ID to delete: ");
        int id = sc.nextInt();

        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Task deleted successfully!" : "No task found with given ID.");
        }
    }
}
