import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Employee implements Serializable {
    private String name;
    private Map<String, Boolean> attendance;

    public Employee(String name) {
        this.name = name;
        this.attendance = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public Map<String, Boolean> getAttendance() {
        return attendance;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(name);
        for (Map.Entry<String, Boolean> entry : attendance.entrySet()) {
            String date = entry.getKey();
            Boolean isPresent = entry.getValue();
            stringBuilder.append("\n").append(date).append(": ").append(isPresent ? "Present" : "Absent");
        }
        return stringBuilder.toString();
    }
}

public class AttendanceTracker {
    private static ArrayList<Employee> employeeList = new ArrayList<>();
    private static final String DATA_FILE = "attendance_data.txt";
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadAttendanceData(); // Load data from file

        boolean isAdmin = login();

        if (isAdmin) {
            showAdminMenu();
        } else {
            showUserMenu();
        }

        saveAttendanceData(); // Save data to file
    }

    private static void loadAttendanceData() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            employeeList = (ArrayList<Employee>) inputStream.readObject();
            System.out.println("Attendance data loaded successfully!");
        } catch (FileNotFoundException e) {
            System.out.println("Attendance data file not found. Starting with an empty employee list.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading attendance data: " + e.getMessage());
        }
    }

    private static void saveAttendanceData() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            outputStream.writeObject(employeeList);
            System.out.println("Attendance data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving attendance data: " + e.getMessage());
        }
    }

    private static boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        // Simulating admin access (You can modify this logic for real authentication)
        if (username.equals("admin") && password.equals("password")) {
            System.out.println("Login successful!");
            return true;
        } else {
            System.out.println("Login failed! Regular user mode.");
            return false;
        }
    }

    private static void showAdminMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View Attendance");
            System.out.println("2. Update Attendance");
            System.out.println("3. Add Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    viewAttendance();
                    break;
                case 2:
                    updateAttendance();
                    break;
                case 3:
                    addEmployee();
                    break;
                case 4:
                    deleteEmployee();
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void showUserMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1. View Attendance");
            System.out.println("2. Logout");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    viewAttendance();
                    break;
                case 2:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private static void viewAttendance() {
        System.out.println("\n--- Attendance List ---");
        for (Employee employee : employeeList) {
            System.out.println(employee);
        }
    }

    private static void updateAttendance() {
        System.out.print("Enter the name of the employee to update attendance: ");
        String name = scanner.nextLine();

        boolean found = false;
        for (Employee employee : employeeList) {
            if (employee.getName().equalsIgnoreCase(name)) {
                Map<String, Boolean> attendance = employee.getAttendance();
                System.out.print("Enter the date (dd-mm-yyyy): ");
                String date = scanner.nextLine();
                System.out.print("Is the employee present on this date? (yes/no): ");
                String choice = scanner.nextLine().toLowerCase();
                boolean isPresent = choice.equals("yes");
                attendance.put(date, isPresent);
                System.out.println("Attendance updated successfully!");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Employee not found in the employee list!");
        }
    }

    private static void addEmployee() {
        System.out.print("Enter the name of the employee to add: ");
        String name = scanner.nextLine();

        Employee newEmployee = new Employee(name);
        employeeList.add(newEmployee);
        System.out.println("Employee added successfully!");
    }

    private static void deleteEmployee() {
        System.out.print("Enter the name of the employee to delete: ");
        String name = scanner.nextLine();

        boolean found = false;
        for (Employee employee : employeeList) {
            if (employee.getName().equalsIgnoreCase(name)) {
                employeeList.remove(employee);
                System.out.println("Employee deleted successfully!");
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Employee not found in the employee list!");
        }
    }
}
