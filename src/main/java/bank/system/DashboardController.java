package bank.system;

import java.sql.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DashboardController {

    private int accountNo;

    @FXML
    Label depositmsg;
    @FXML
    Label profiledetail;
    @FXML
    TextField account_no;
    @FXML
    TextField password;
    @FXML
    TextField deposit_amount;

    public void setDetail(int acc) {
        this.accountNo = acc;
        loadAccountDetails();
    }

    public void loadAccountDetails() {
        String sql = "SELECT name,account_no,dob FROM Accounts WHERE account_no =?";
        try (PreparedStatement stmt = Main.connection.prepareStatement(sql)) {
            stmt.setDouble(1, accountNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                profiledetail.setText("Name : "+rs.getString("name") + "\nAccount No : "+ String.valueOf(rs.getInt("account_no")) + "\nDOB : "
                        + rs.getString("dob"));
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void changeToDeposit(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/deposit.fxml"));
            Parent root = loader.load();

            DepositController depositController = loader.getController();
            depositController.setDetail(accountNo);

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }
    public void changeToWithdraw(ActionEvent e) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/withdraw.fxml"));
            Parent root = loader.load();

            WithdrawController withdrawController = loader.getController();
            withdrawController.setDetail(accountNo);

            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void logout(ActionEvent e) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout");
        alert.setContentText("Do you want to save before exit");

        if (alert.showAndWait().get() == ButtonType.OK) {
            System.out.println("You logout Successfully");
            LoginController Login = new LoginController();
            Login.changeToLogin(e);
        }
    }

    public void CheckBalance(ActionEvent e){
        
    }
}
