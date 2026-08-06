package com.wartime.system.controller;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Rank;
import com.wartime.system.model.SecureMessage;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.service.MessageService;
import com.wartime.system.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReceiverController {

    @FXML
    private ListView<SecureMessage> lstMessages;
    @FXML
    private Label lblStatus;
    @FXML
    private ToggleButton tglPrivate;
    @FXML
    private ToggleButton tglGroup;
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnExit;

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        tglPrivate.setToggleGroup(group);
        tglGroup.setToggleGroup(group);

        btnReturn.setOnAction(e -> SceneNavigator.getInstance().loadScene("option.fxml"));
        btnExit.setOnAction(e -> SceneNavigator.getInstance().loadScene("outro.fxml"));

        tglPrivate.setOnAction(e -> refreshMessages());
        tglGroup.setOnAction(e -> refreshMessages());

        refreshMessages();
        setupContextMenu();

        lstMessages.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SecureMessage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    String prefix = (item.getTargetUser() != null) ? "[PRIVATE] " : "[GROUP] ";
                    String unreadMark = item.isRead() ? "" : " ● ";
                    setText(unreadMark + prefix + "TRANSMISSION ID: "
                            + Integer.toHexString(item.hashCode()).toUpperCase());

                    if (!item.isRead()) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #00ff00;");
                    } else {
                        setStyle("-fx-background-color: transparent;");
                    }
                }
            }
        });

        lstMessages.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // 2 bar click krle message deckha jabe
                SecureMessage selected = lstMessages.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    handleDecrypt(selected);
                }
            }
        });
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete Transmission");
        deleteItem.getStyleClass().add("menu-item");
        deleteItem.setOnAction(e -> {
            SecureMessage selected = lstMessages.getSelectionModel().getSelectedItem();
            if (selected != null) {
                MessageService.getInstance().deleteMessage(selected);
                refreshMessages();
            }
        });
        contextMenu.getItems().add(deleteItem);
        lstMessages.setContextMenu(contextMenu);
    }

    private void refreshMessages() {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        var allVisible = MessageService.getInstance().getVisibleMessages(currentUser);

        List<SecureMessage> filtered;
        if (tglPrivate.isSelected()) {
            filtered = allVisible.stream()
                    .filter(m -> m.getTargetUser() != null || (m.getTargetGroup() == null && m.getTargetUser() == null))
                    .collect(Collectors.toList());
            lblStatus.setText("Private/Rank Transmissions for " + currentUser.getName() + ":");
        } else {
            filtered = allVisible.stream()
                    .filter(m -> m.getTargetGroup() != null)
                    .collect(Collectors.toList());
            lblStatus.setText("Group Operational Transmissions:");
        }

        // Sort: Unread first
        filtered.sort((a, b) -> Boolean.compare(a.isRead(), b.isRead()));

        lstMessages.setItems(FXCollections.observableArrayList(filtered));
    }

    private void handleDecrypt(SecureMessage message) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Decryption Check");
        dialog.setHeaderText("Enter Decryption Key");

        ButtonType loginButtonType = new ButtonType("Decrypt", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        PasswordField password = new PasswordField();
        password.setPromptText("Decryption Key");

        grid.add(new Label("Decryption Key:"), 0, 0);
        grid.add(password, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return (password.getText() != null) ? password.getText().trim() : "";
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(key -> {
            AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
            String statedAppointment = currentUser.getRank().name();

            // Logic 6.e.ii: "if decryption key== encryption key then the text will be
            // visible."
            // 6.e.iii: "but if the given appointment is lower then the senders appointment
            // then it will show a pop-up saying 'access denied'".

            // Step 1: Check Hierarchy (Requirement 5)
            // Wait, Requirement 6.e.iii says "if *given appointment* is lower than
            // senders..."
            // It relies on what the user TYPED in the popup, not what they logged in as?
            // That's insecure but I must follow requirement "enter... user's appointment".

            // Actually, Requirement 5 is the "Logic YOU MUST MAINTAIN".
            // 5.a: Commander sends -> Only Commander sees.

            try {
                // Step 1: Check Access
                boolean accessGranted = checkAccess(message, statedAppointment);

                if (accessGranted) {
                    // Step 2: Decrypt
                    String decrypted = MessageService.getInstance().attemptDecryption(message, key);

                    // Show Content in Custom Dialog to preserve "Normal" look
                    Dialog<Void> dialogContent = new Dialog<>();
                    dialogContent.setTitle("Decrypted Message");
                    dialogContent.setHeaderText("Access Granted (Confidential):");

                    ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
                    dialogContent.getDialogPane().getButtonTypes().add(closeButtonType);

                    TextArea txtContent = new TextArea(decrypted);
                    txtContent.setEditable(false);
                    txtContent.setWrapText(true);
                    txtContent.getStyleClass().add("terminal-input");
                    txtContent.setPrefHeight(200);
                    txtContent.setPrefWidth(400);

                    // Add stylesheet to dialog
                    dialogContent.getDialogPane().getStylesheets()
                            .add(getClass().getResource("/com/wartime/system/view/styles.css").toExternalForm());
                    dialogContent.getDialogPane().getStyleClass().add("standard-root"); // Reuse existing dark styles
                    dialogContent.getDialogPane().setContent(txtContent);

                    // Force dark background for header and content
                    dialogContent.getDialogPane().setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

                    dialogContent.showAndWait();

                    // Mark as read after viewing
                    MessageService.getInstance().markAsRead(message);
                    refreshMessages();
                } else {
                    // Step 3: Access Denied -> Show Encrypted Content
                    Alert error = new Alert(Alert.AlertType.WARNING);
                    error.setTitle("Access Restricted");
                    error.setHeaderText("Insufficient Rank");
                    TextArea txtEncrypted = new TextArea("Result: " + message.getEncryptedContent());
                    txtEncrypted.setEditable(false);
                    txtEncrypted.setWrapText(true);
                    txtEncrypted.getStyleClass().add("terminal-input");

                    // We need to style this alert too if we want consistency, but sticking to
                    // standard Alert for warnings is fine unless requested.
                    // But user said "NORMAL (HOW IT WAS SENT)" for success case.
                    error.getDialogPane().setContent(txtEncrypted);

                    error.showAndWait();
                    // Do NOT delete the message
                }

            } catch (Exception e) {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setContentText(e.getMessage());
                error.showAndWait();
            }
        });
    }

    private boolean checkAccess(SecureMessage msg, String statedAppointment) {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();

        // Commander can always access
        if (currentUser.getRank() == Rank.COMMANDER)
            return true;

        // If it's a group message, check membership
        if (msg.getTargetGroup() != null) {
            return msg.getTargetGroup().isMember(currentUser);
        }

        // If it's a private individual chat
        if (msg.getTargetUser() != null) {
            return msg.getTargetUser().getName().equalsIgnoreCase(currentUser.getName());
        }

        // Rank-based access
        Rank userRank;
        try {
            userRank = Rank.valueOf(statedAppointment.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Appointment Type");
        }

        Rank targetRank = msg.getTargetRank();
        if (targetRank == null) {
            targetRank = msg.getSenderRank();
        }

        return userRank.ordinal() <= targetRank.ordinal();
    }
}
