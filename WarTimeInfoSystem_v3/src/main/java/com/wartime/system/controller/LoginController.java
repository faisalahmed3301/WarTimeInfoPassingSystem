package com.wartime.system.controller;

import com.wartime.system.exception.UnauthorizedAccessException;
import com.wartime.system.service.AuthenticationService;
import com.wartime.system.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;

public class LoginController {

    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<String> cmbAppointment;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;

    @FXML
    public void initialize() {
        
        cmbAppointment.setItems(FXCollections.observableArrayList(
                "COMMANDER",
                "OFFICER",
                "SOLDIER"));
        btnLogin.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String name = txtName.getText();
        String appointment = cmbAppointment.getValue();
        String password = txtPassword.getText();

        try {
            AuthenticationService.getInstance().login(name, appointment, password);
          
            SceneNavigator.getInstance().loadScene("option.fxml");
        } catch (UnauthorizedAccessException e) {
            showAlert("Access Unauthorised", "Credentials do not match.");
        } catch (IllegalArgumentException e) {          
            showAlert("Error", "Invalid Appointment Type. Use Commander, Officer, or Soldier.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
     
        txtPassword.clear();
    }
}
