package bank.system;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import java.sql.PreparedStatement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    TextField username;

    @FXML
    TextField password;

    @FXML
    Button forgetpassword;

    @FXML
    Button loginbtn;

    @FXML
    TextField name;
    @FXML
    TextField passwd;
    @FXML
    TextField confirmpasswd;
    @FXML
    TextField date;
    @FXML
    TextField month;
    @FXML
    TextField year;
    @FXML
    Label msg;

    public void login(ActionEvent e) {
        System.out.println("Login");

    }

    public void forgetPassword(ActionEvent e) {
        System.out.println("Forget Password");
    }

    public void changeToLogin(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void changeToCreateAcc(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/createAcc.fxml"));
            Stage stage = (Stage) ((javafx.scene.Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public boolean isDob(String date, String month, String year) {
        if (date == null || date.isEmpty())
            return false;
        if (month == null || month.isEmpty())
            return false;
        if (year == null || year.isEmpty())
            return false;

        try {
            int d = Integer.parseInt(date);
            int m = Integer.parseInt(month);
            int y = Integer.parseInt(year);

            LocalDate.of(y, m, d);
            return true;
        } catch (NumberFormatException | DateTimeException e) {
            return false;
        }
    }

    public void createAccount(ActionEvent e) {
        String Acc_name = name.getText();
        String Passwd = passwd.getText();
        String ConfirmPasswd = confirmpasswd.getText();

        String dob = date.getText() + "-" + month.getText() + "-" + year.getText();
        String sql = "INSERT INTO Accounts(name, dob) VALUES(?,?)";
        if (Acc_name.isEmpty()) {
            msg.setText("Name Should not be empty");
            msg.setTextFill(Color.RED);
            return;
        }
        if (Passwd.isEmpty()) {
            msg.setText("Password Should not be empty");
            msg.setTextFill(Color.RED);
            return;
        }
        if (!Passwd.equals(ConfirmPasswd)) {
            msg.setText("Password and Confirm Password not match");
            msg.setTextFill(Color.RED);
            return;
        }
        if (Passwd.length() < 6) {
            msg.setText("Password need to be atleast 6 characters long");
            msg.setTextFill(Color.RED);
            return;
        }
        if (!isDob(date.getText(), month.getText(), year.getText())) {
            msg.setText("Incorrect Date of Birth");
            msg.setTextFill(Color.RED);
            return;
        }
        int inputYear = Integer.parseInt(year.getText());
        int currentYear = java.time.LocalDate.now().getYear();
        int minYear = 1950;
        int maxYear = currentYear - 18;

        if (inputYear < minYear || inputYear > maxYear) {
            msg.setText("Year must be between " + minYear + " and " + maxYear);
            msg.setTextFill(Color.RED);
            return;
        }
        try (PreparedStatement stmt = Main.connection.prepareStatement(sql)) {
            stmt.setString(1, Acc_name);
            stmt.setString(2, dob);
            stmt.executeUpdate();
            System.out.println("Account created for: " + name);

            msg.setText("Successfully created Account");
            msg.setTextFill(Color.GREEN);

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Account Detail ");
            alert.setHeaderText("Remember Your Account Detail unless You may loss! your account");
            alert.setContentText("Name : " + Acc_name + "\n DOB : " + dob + "\nPassword : " + Passwd);

            alert.getButtonTypes().setAll(ButtonType.OK);

            Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
            alertStage.setOnCloseRequest(event -> {
                event.consume();
            });

            if (alert.showAndWait().get() == ButtonType.OK) {
                changeToLogin(e);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
