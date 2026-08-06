package com.wartime.system.controller;

import com.wartime.system.exception.UnauthorizedAccessException;
import com.wartime.system.service.AuthenticationService;
import com.wartime.system.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.collections.FXCollections;

public class LoginController {

    @FXML
    private TextField txtName;
    @FXML
    private ComboBox<String> cmbAppointment;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtPasswordVisible;
    @FXML
    private Button btnLogin;
    @FXML
    private Button btnExit;
    @FXML
    private Label lblEye;

    private boolean passwordVisible = false;

    private Image eyeOpenImg;
    private Image eyeClosedImg;

    @FXML
    public void initialize() {
        // Load eye icons from resources
        try {
            eyeOpenImg = new Image(getClass().getResourceAsStream("/com/wartime/system/view/images/opened-eye.png"));
            eyeClosedImg = new Image(getClass().getResourceAsStream("/com/wartime/system/view/images/close-eye.png"));
        } catch (Exception e) {
            System.err.println("Could not load eye images: " + e.getMessage());
        }

        cmbAppointment.setItems(FXCollections.observableArrayList(
                "COMMANDER",
                "OFFICER",
                "SOLDIER"));
        btnLogin.setOnAction(e -> handleLogin());
        btnExit.setOnAction(e -> SceneNavigator.getInstance().loadScene("outro.fxml"));

        // Set initial eye icon (closed eye because password is hidden by default)
        setEyeIcon(false);

        // Sync text between password and visible text fields
        txtPassword.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!passwordVisible) {
                txtPasswordVisible.setText(newVal);
            }
        });
        txtPasswordVisible.textProperty().addListener((obs, oldVal, newVal) -> {
            if (passwordVisible) {
                txtPassword.setText(newVal);
            }
        });

        // Eye toggle — click the label overlay
        lblEye.setOnMouseClicked(e -> togglePasswordVisibility());
    }

    private void setEyeIcon(boolean open) {
        Image img = open ? eyeOpenImg : eyeClosedImg;
        if (img != null) {
            ImageView iv = new ImageView(img);
            iv.setFitWidth(24);
            iv.setFitHeight(24);
            iv.setPreserveRatio(true);
            iv.setSmooth(true);
            lblEye.setGraphic(iv);
        } else {
            // Fallback to text if image loading fails
            lblEye.setText(open ? "👁" : "🙈");
        }
    }

    private void togglePasswordVisibility() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            // Show password: Sync visible field, swap visibility
            txtPasswordVisible.setText(txtPassword.getText());
            txtPassword.setVisible(false);
            txtPassword.setManaged(false);
//            text field expose korbo
            txtPasswordVisible.setVisible(true);
            txtPasswordVisible.setManaged(true);
            setEyeIcon(true);
        } else {
            // Hide password: Sync hidden field, swap visibility
            txtPassword.setText(txtPasswordVisible.getText());
            txtPasswordVisible.setVisible(false);
            txtPasswordVisible.setManaged(false);
            txtPassword.setVisible(true);
//            text field hide korbo
            txtPassword.setManaged(true);
            setEyeIcon(false);
        }
    }

    private void handleLogin() {
        String name = txtName.getText() != null ? txtName.getText().trim() : "";
        String appointment = cmbAppointment.getValue();
        String password = passwordVisible
                ? (txtPasswordVisible.getText() != null ? txtPasswordVisible.getText().trim() : "")
                : (txtPassword.getText() != null ? txtPassword.getText().trim() : "");

        if (name.isEmpty() || appointment == null || password.isEmpty()) {
            showAlert("Error", "Please fill all fields.");
            return;
        }

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
        txtPasswordVisible.clear();
    }
}
