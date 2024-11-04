import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage {

    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Login Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new GridLayout(3, 2));

        // Create components
        JLabel labelUser = new JLabel("Username:");
        JTextField textUser = new JTextField();
        JLabel labelPass = new JLabel("Password:");
        JPasswordField textPass = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signUpButton = new JButton("Sign Up");
        JButton cancelButton = new JButton("Cancel");

        // Add components to frame
        frame.add(labelUser);
        frame.add(textUser);
        frame.add(labelPass);
        frame.add(textPass);
        frame.add(loginButton);
        frame.add(signUpButton);
        frame.add(cancelButton);

        // Set login button action
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = textUser.getText().trim();
                String password = new String(textPass.getPassword()).trim();

                // Admin login check
                if (username.equals("admin") && password.equals("admin")) {
                    showAdminWindow();
                    JOptionPane.showMessageDialog(frame, "admin login successful!");
                } else if (authenticate(username, password, frame)) {
                    JOptionPane.showMessageDialog(frame, "user login successfull");
                } else {
                    JOptionPane.showMessageDialog(frame, "incorrect password");
                }
            }
        });

        // Set sign-up button action
        signUpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showSignUpDialog(frame);
            }
        });

        // Set cancel button action
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    // SQL connection and authentication method
    public static boolean authenticate(String username, String password, JFrame frame) {
        boolean isValid = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginDB", "root", "toor");

            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                isValid = true;
                String schoolCollegeId = rs.getString("school_college_id");
                String secretNumber = rs.getString("secret_number");
                showUserDetails(username, schoolCollegeId, secretNumber, frame);
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return isValid;
    }

    // Method to show user details
    public static void showUserDetails(String username, String schoolCollegeId, String secretNumber, JFrame frame) {
        JFrame detailsFrame = new JFrame("User Details");
        detailsFrame.setSize(300, 200);
        detailsFrame.setLayout(new GridLayout(3, 2));

        JLabel labelWelcome = new JLabel("Welcome, " + username + "!");
        JLabel labelSchoolCollegeId = new JLabel("School/College ID: ");
        JLabel labelSchoolCollegeIdValue = new JLabel(schoolCollegeId);
        JLabel labelSecretNumber = new JLabel("Secret Number: ");
        JLabel labelSecretNumberValue = new JLabel(secretNumber);

        detailsFrame.add(labelWelcome);
        detailsFrame.add(new JLabel());
        detailsFrame.add(labelSchoolCollegeId);
        detailsFrame.add(labelSchoolCollegeIdValue);
        detailsFrame.add(labelSecretNumber);
        detailsFrame.add(labelSecretNumberValue);

        detailsFrame.setVisible(true);
    }

    // Method to show the sign-up dialog
    public static void showSignUpDialog(JFrame frame) {
        JDialog signUpDialog = new JDialog(frame, "Sign Up", true);
        signUpDialog.setLayout(new GridLayout(5, 2));
        signUpDialog.setSize(300, 250);

        JLabel labelNewUser = new JLabel("New Username:");
        JTextField textNewUser = new JTextField();
        JLabel labelNewPass = new JLabel("New Password:");
        JPasswordField textNewPass = new JPasswordField();
        JLabel labelSecretId = new JLabel("Secret ID:");
        JTextField textSecretId = new JTextField();
        JLabel labelSecretNumber = new JLabel("Secret Number:");
        JPasswordField textSecretNumber = new JPasswordField();
        JButton createAccountButton = new JButton("Create Account");

        signUpDialog.add(labelNewUser);
        signUpDialog.add(textNewUser);
        signUpDialog.add(labelNewPass);
        signUpDialog.add(textNewPass);
        signUpDialog.add(labelSecretId);
        signUpDialog.add(textSecretId);
        signUpDialog.add(labelSecretNumber);
        signUpDialog.add(textSecretNumber);
        signUpDialog.add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newUsername = textNewUser.getText();
                String newPassword = new String(textNewPass.getPassword());
                String secretId = textSecretId.getText();
                String secretNumber = new String(textSecretNumber.getPassword());

                if (createAccount(newUsername, newPassword, secretId, secretNumber)) {
                    JOptionPane.showMessageDialog(signUpDialog, "user added successfully");
                    signUpDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(signUpDialog, "invalid");
                }
            }
        });

        signUpDialog.setVisible(true);
    }

    // Method to create a new user account
    public static boolean createAccount(String username, String password, String secretId, String secretNumber) {
        boolean isCreated = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginDB", "root", "toor");

            String query = "INSERT INTO users (username, password, school_college_id, secret_number) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, secretId);
            pst.setString(4, secretNumber);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                isCreated = true;
            }

            pst.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return isCreated;
    }

    // Method to show the admin window with user data
    public static void showAdminWindow() {
        JFrame adminFrame = new JFrame("Admin Window");
        adminFrame.setSize(500, 400);
        adminFrame.setLayout(new BorderLayout());

        JTextArea userDataArea = new JTextArea();
        userDataArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(userDataArea);

        JButton refreshButton = new JButton("Refresh Data");
        JButton deleteUserButton = new JButton("Delete User");
        JTextField schoolCollegeIdField = new JTextField("Enter school/college ID to delete", 20);

        JPanel controlPanel = new JPanel();
        controlPanel.add(refreshButton);
        controlPanel.add(schoolCollegeIdField);
        controlPanel.add(deleteUserButton);

        adminFrame.add(scrollPane, BorderLayout.CENTER);
        adminFrame.add(controlPanel, BorderLayout.SOUTH);

        refreshButton.addActionListener(e -> userDataArea.setText(getAllUserData()));

        deleteUserButton.addActionListener(e -> {
            String schoolCollegeId = schoolCollegeIdField.getText();
            if (deleteUser(schoolCollegeId)) {
                JOptionPane.showMessageDialog(adminFrame, "deleted successfully");
                userDataArea.setText(getAllUserData());
            } else {
                JOptionPane.showMessageDialog(adminFrame, "unable to delete invalid id");
            }
        });

        adminFrame.setVisible(true);
    }

    // Method to fetch all user data
    public static String getAllUserData() {
        StringBuilder data = new StringBuilder();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginDB", "root", "toor");

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                data.append("Username: ").append(rs.getString("username"))
                        .append(", School ID: ").append(rs.getString("school_college_id"))
                        .append(", Secret Number: ").append(rs.getString("secret_number"))
                        .append("\n");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return data.toString();
    }

    // Method to delete a user based on school/college ID
    public static boolean deleteUser(String schoolCollegeId) {
        boolean isDeleted = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/loginDB", "root", "toor");

            String query = "DELETE FROM users WHERE school_college_id = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, schoolCollegeId);

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                isDeleted = true;
            }

            pst.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return isDeleted;
    }
}
