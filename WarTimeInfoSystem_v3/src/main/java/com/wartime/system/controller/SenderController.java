package com.wartime.system.controller;

import com.wartime.system.service.MessageService;
import com.wartime.system.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class SenderController {

    @FXML
    private TextArea txtMessage;
    @FXML
    private ComboBox<String> cmbEncryption;
    @FXML
    private ComboBox<String> cmbAppointment;
    @FXML
    private TextField txtKey;
    @FXML
    private Button btnSend;
    @FXML
    private Button btnReturn;

    @FXML
    public void initialize() {
        
        cmbEncryption.setItems(FXCollections.observableArrayList(
                "Caesar Cipher",
                "Reverse Cipher",
                "XOR Cipher",
                "Base64 Encoding"));

        //  commander--> officer--> soldier"
        cmbAppointment.setItems(FXCollections.observableArrayList(
                "COMMANDER",
                "OFFICER",
                "SOLDIER"));

        btnSend.setOnAction(e -> handleSend());
        btnReturn.setOnAction(e -> SceneNavigator.getInstance().loadScene("option.fxml"));
    }

    private void handleSend() {
        String msg = txtMessage.getText();
        String method = cmbEncryption.getValue();
        String appointment = cmbAppointment.getValue();
        String key = txtKey.getText();

        if (msg == null || msg.isEmpty() || method == null || appointment == null || key == null || key.isEmpty()) {  // jodi kichu miss thake
            showAlert("Error", "Please fill all fields including Key.");
            return;
        }
// sob thik thkle 

        try {
            MessageService.getInstance().sendMessage(msg, key, method, appointment);
            
            // status deckhanor jonno
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Message Sent!");
            alert.showAndWait();
            
            // ekbar seen korar por inbox+key clear hoye jabe
        
            txtMessage.clear();
            txtKey.clear();
        } catch (Exception e) { // if get any error 
            showAlert("Error", "Failed to send: " + e.getMessage());
        }
    }

    // ki deckhabo alert box e
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
