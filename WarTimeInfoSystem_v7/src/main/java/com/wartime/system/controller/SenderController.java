package com.wartime.system.controller;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Group;
import com.wartime.system.model.Rank;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.service.AuthenticationService;
import com.wartime.system.service.GroupService;
import com.wartime.system.service.MessageService;
import com.wartime.system.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

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
    private ComboBox<Group> cmbGroup;
    @FXML
    private ComboBox<AbstractUser> cmbRecipient;
    @FXML
    private Button btnSend;
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnEmergency;

    @FXML
    public void initialize() {
//combo box er options
        cmbEncryption.setItems(FXCollections.observableArrayList(
                "Caesar Cipher",
                "Reverse Cipher",
                "XOR Cipher",
                "Base64 Encoding"));

        // commander--> officer--> soldier"
        cmbAppointment.setItems(FXCollections.observableArrayList(
                "COMMANDER",
                "OFFICER",
                "SOLDIER"));

        // Setup Groups
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        if (currentUser == null)
            return;

        cmbGroup.setItems(FXCollections.observableArrayList(
                GroupService.getInstance().getVisibleGroups(currentUser)));
        cmbGroup.setCellFactory(lv -> new ListCell<Group>() {
            @Override
            protected void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "None" : item.getName());
            }
        });
        cmbGroup.setConverter(new StringConverter<Group>() {
            @Override
            public String toString(Group object) {
                return object == null ? "None" : object.getName();
            }

            @Override
            public Group fromString(String string) {
                return null;
            }
        });

        // Setup Recipients
        cmbRecipient.setItems(FXCollections.observableArrayList(
                AuthenticationService.getInstance().getUsers().values()));
        cmbRecipient.setCellFactory(lv -> new ListCell<AbstractUser>() {
            @Override
            protected void updateItem(AbstractUser item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "None" : item.getName() + " (" + item.getRank() + ")");
            }
        });
        cmbRecipient.setConverter(new StringConverter<AbstractUser>() {
            @Override
            public String toString(AbstractUser object) {
                return object == null ? "None" : object.getName();
            }

            @Override
            public AbstractUser fromString(String string) {
                return null;
            }
        });

        // MUTUALLY EXCLUSIVE LOGIC -> yt 
        cmbGroup.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbAppointment.setValue(null);
                cmbAppointment.setDisable(true);
                cmbRecipient.setValue(null);
                cmbRecipient.setDisable(true);
            } else {
                cmbAppointment.setDisable(false);
                cmbRecipient.setDisable(false);
            }
        });

        cmbRecipient.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cmbAppointment.setValue(null);
                cmbAppointment.setDisable(true);
                cmbGroup.setValue(null);
                cmbGroup.setDisable(true);
            } else {
                if (cmbGroup.getValue() == null) {
                    cmbAppointment.setDisable(false);
                    cmbGroup.setDisable(false);
                }
            }
        });

        btnSend.setOnAction(e -> handleSend());
        btnReturn.setOnAction(e -> SceneNavigator.getInstance().loadScene("option.fxml"));

        // Show emergency button only for Commanders
        if (currentUser.getRank() == Rank.COMMANDER) {
            btnEmergency.setVisible(true);
            btnEmergency.setManaged(true);
            btnEmergency.setOnAction(e -> handleEmergency());
        }
    }

    private void handleSend() {
        String msg = txtMessage.getText() != null ? txtMessage.getText().trim() : "";
        String method = cmbEncryption.getValue();
        String appointment = cmbAppointment.getValue();
        String key = txtKey.getText() != null ? txtKey.getText().trim() : "";
        Group selectedGroup = cmbGroup.getValue();
        AbstractUser selectedRecipient = cmbRecipient.getValue();

        // VALIDATION: If no group and no recipient, appointment is mandatory.
        if (msg == null || msg.isEmpty() || method == null || key == null || key.isEmpty() ||
                (selectedGroup == null && selectedRecipient == null && appointment == null)) {
            showAlert("Error", "Please fill all fields. Select a Group, Recipient, or Appointment.");
            return;
        }
        // sob thik thakle

        try {
            MessageService.getInstance().sendMessage(msg, key, method, appointment, selectedGroup, selectedRecipient);

            // status deckhanor jonno
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Message Sent!");
            alert.showAndWait();

            // ekbar seen korar por inbox+key clear hoye jabe
            // version 6.1 e egula stay korbe cz ami backend integrate korechi

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

    private void handleEmergency() {
        String msg = txtMessage.getText() != null ? txtMessage.getText().trim() : "";
        if (msg.isEmpty()) {
            showAlert("Error", "Please enter a message for the emergency alert.");
            return;
        }
        try {
            MessageService.getInstance().sendEmergencyMessage(msg);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Emergency Alert Sent");
            alert.setHeaderText(null);
            alert.setContentText("Emergency alert has been broadcast to all personnel!");
            alert.showAndWait();
            txtMessage.clear();
        } catch (Exception e) {
            showAlert("Error", "Failed to send emergency alert: " + e.getMessage());
        }
    }
}
