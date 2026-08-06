package com.wartime.system.controller;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Group;
import com.wartime.system.model.GroupType;
import com.wartime.system.model.Rank;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.service.AuthenticationService;
import com.wartime.system.service.GroupService;
import com.wartime.system.service.MessageService;
import com.wartime.system.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    // Priority toggles
    @FXML
    private ToggleButton tglNormal;
    @FXML
    private ToggleButton tglWarning;
    @FXML
    private ToggleButton tglCritical;

    // Mission Brief
    @FXML
    private ToggleButton tglMissionBrief;
    @FXML
    private VBox messageContentBox;
    @FXML
    private VBox missionBriefBox;
    @FXML
    private TextField txtMission;
    @FXML
    private TextField txtLocation;
    @FXML
    private TextField txtTime;
    @FXML
    private ComboBox<AbstractUser> cmbMissionCommander;

    private ToggleGroup priorityGroup;
    private boolean missionBriefMode = false;

    @FXML
    public void initialize() {

        cmbEncryption.setItems(FXCollections.observableArrayList(
                "Caesar Cipher",
                "Reverse Cipher",
                "XOR Cipher",
                "Base64 Encoding"));

        cmbAppointment.setItems(FXCollections.observableArrayList(
                "COMMANDER",
                "OFFICER",
                "SOLDIER"));

        // Setup Priority Toggle Group
        priorityGroup = new ToggleGroup();
        tglNormal.setToggleGroup(priorityGroup);
        tglWarning.setToggleGroup(priorityGroup);
        tglCritical.setToggleGroup(priorityGroup);
        tglNormal.setSelected(true);

        // Highlight selected priority
        priorityGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                tglNormal.setSelected(true); // Always have one selected
            }
            updatePriorityStyles();
        });
        updatePriorityStyles();

        // Setup Mission Brief toggle
        tglMissionBrief.setOnAction(e -> {
            missionBriefMode = tglMissionBrief.isSelected();
            txtMessage.setVisible(!missionBriefMode);
            txtMessage.setManaged(!missionBriefMode);
            missionBriefBox.setVisible(missionBriefMode);
            missionBriefBox.setManaged(missionBriefMode);

            if (missionBriefMode) {
                tglMissionBrief.setStyle("-fx-font-size: 11px; -fx-background-color: #582F0E; -fx-text-fill: #FFFFFF; "
                        + "-fx-border-color: #7F4F24; -fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12; "
                        + "-fx-cursor: hand; -fx-padding: 4 12;");
            } else {
                tglMissionBrief.setStyle("-fx-font-size: 11px; -fx-background-color: #414833; -fx-text-fill: #E8E8E8; "
                        + "-fx-border-color: #582F0E; -fx-border-width: 1; -fx-border-radius: 12; -fx-background-radius: 12; "
                        + "-fx-cursor: hand; -fx-padding: 4 12;");
            }
        });

        // Populate Mission Commander dropdown with only commander-rank users
        List<AbstractUser> commanders = AuthenticationService.getInstance().getUsers().values().stream()
                .filter(u -> u.getRank() == Rank.COMMANDER)
                .collect(Collectors.toList());
        cmbMissionCommander.setItems(FXCollections.observableArrayList(commanders));
        cmbMissionCommander.setConverter(new StringConverter<AbstractUser>() {
            @Override
            public String toString(AbstractUser object) {
                return object == null ? "" : object.getName();
            }

            @Override
            public AbstractUser fromString(String string) {
                return null;
            }
        });

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
                if (empty || item == null) {
                    setText("None");
                } else {
                    setText(item.getName() + " (" + item.getType() + ")");
                }
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

        // Double-click on cmbGroup opens filter popup
        cmbGroup.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                showFilterPopup();
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

        // MUTUALLY EXCLUSIVE LOGIC
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

    // ================== PRIORITY STYLES ==================

    private void updatePriorityStyles() {
        // Unselected: dim and muted
        String dimBase = "-fx-font-size: 11px; -fx-border-width: 1; -fx-border-radius: 12; "
                + "-fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 4 12; -fx-background-color: #2A3020;";

        // Selected: bright, bold, thick glowing border
        String glowBase = "-fx-font-size: 12px; -fx-font-weight: bold; -fx-border-width: 2; "
                + "-fx-border-radius: 12; -fx-background-radius: 12; -fx-cursor: hand; -fx-padding: 4 12;";

        if (tglNormal.isSelected()) {
            tglNormal.setStyle(glowBase
                    + " -fx-background-color: #006600; -fx-text-fill: #00ff00; -fx-border-color: #00ff00;");
        } else {
            tglNormal.setStyle(dimBase + " -fx-text-fill: #557755; -fx-border-color: #445544;");
        }

        if (tglWarning.isSelected()) {
            tglWarning.setStyle(glowBase
                    + " -fx-background-color: #665500; -fx-text-fill: #ffdd00; -fx-border-color: #ffdd00;");
        } else {
            tglWarning.setStyle(dimBase + " -fx-text-fill: #887744; -fx-border-color: #665533;");
        }

        if (tglCritical.isSelected()) {
            tglCritical.setStyle(glowBase
                    + " -fx-background-color: #880000; -fx-text-fill: #ff4444; -fx-border-color: #ff4444;");
        } else {
            tglCritical.setStyle(dimBase + " -fx-text-fill: #884444; -fx-border-color: #664444;");
        }
    }

    private String getSelectedPriority() {
        if (tglCritical.isSelected()) return "CRITICAL";
        if (tglWarning.isSelected()) return "WARNING";
        return "NORMAL";
    }

    // ================== FILTER POPUP ==================

    private void showFilterPopup() {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        List<Group> allGroups = GroupService.getInstance().getVisibleGroups(currentUser);

        Dialog<List<Group>> dialog = new Dialog<>();
        dialog.setTitle("Filter Groups");
        dialog.setHeaderText(null);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/wartime/system/view/styles.css").toExternalForm());
        dialogPane.setStyle("-fx-background-color: #333D29;");

        Label titleLabel = new Label("FILTER GROUPS");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #E8E8E8;");

        Label hintLabel = new Label("Narrow down target groups by category, year, month, or date.");
        hintLabel.setStyle("-fx-text-fill: #7A8360; -fx-font-size: 11px;");

        String fieldStyle = "-fx-background-color: #2A3020; -fx-text-fill: #E8E8E8; "
                + "-fx-border-color: #582F0E; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-prompt-text-fill: #7A8360;";
        String labelStyle = "-fx-text-fill: #E8E8E8; -fx-font-weight: bold;";

        Label catLabel = new Label("Category");
        catLabel.setStyle(labelStyle);
        ComboBox<GroupType> cmbCat = new ComboBox<>(FXCollections.observableArrayList(GroupType.values()));
        cmbCat.setPromptText("All Categories");
        cmbCat.setPrefWidth(200);
        cmbCat.setStyle(fieldStyle);
        VBox catBox = new VBox(4, catLabel, cmbCat);

        Label yearLabel = new Label("Year");
        yearLabel.setStyle(labelStyle);
        List<Integer> years = new ArrayList<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear; i >= 2020; i--) years.add(i);
        ComboBox<Integer> cmbYear = new ComboBox<>(FXCollections.observableArrayList(years));
        cmbYear.setPromptText("All Years");
        cmbYear.setPrefWidth(200);
        cmbYear.setStyle(fieldStyle);
        VBox yearBox = new VBox(4, yearLabel, cmbYear);

        Label monthLabel = new Label("Month");
        monthLabel.setStyle(labelStyle);
        List<Integer> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) months.add(i);
        ComboBox<Integer> cmbMonth = new ComboBox<>(FXCollections.observableArrayList(months));
        cmbMonth.setPromptText("All Months");
        cmbMonth.setPrefWidth(200);
        cmbMonth.setStyle(fieldStyle);
        VBox monthBox = new VBox(4, monthLabel, cmbMonth);

        Label dateLabel = new Label("Specific Date");
        dateLabel.setStyle(labelStyle);
        DatePicker dpDate = new DatePicker();
        dpDate.setPromptText("Any Date");
        dpDate.setPrefWidth(200);
        dpDate.setStyle(fieldStyle);
        VBox dateBox = new VBox(4, dateLabel, dpDate);

        HBox row1 = new HBox(20, catBox, yearBox);
        row1.setAlignment(Pos.CENTER_LEFT);
        HBox row2 = new HBox(20, monthBox, dateBox);
        row2.setAlignment(Pos.CENTER_LEFT);

        javafx.scene.layout.Region sep = new javafx.scene.layout.Region();
        sep.setPrefHeight(1);
        sep.setStyle("-fx-background-color: #582F0E;");

        Label previewLabel = new Label("Matching groups: " + allGroups.size());
        previewLabel.setStyle("-fx-text-fill: #A4AC86; -fx-font-size: 12px;");

        Runnable updatePreview = () -> {
            List<Group> filtered = filterGroups(allGroups, cmbCat.getValue(), cmbYear.getValue(),
                    cmbMonth.getValue(), dpDate.getValue());
            previewLabel.setText("Matching groups: " + filtered.size());
        };
        cmbCat.setOnAction(e -> updatePreview.run());
        cmbYear.setOnAction(e -> updatePreview.run());
        cmbMonth.setOnAction(e -> updatePreview.run());
        dpDate.setOnAction(e -> updatePreview.run());

        Button btnClear = new Button("CLEAR ALL FILTERS");
        btnClear.setStyle("-fx-background-color: #8B0000; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; "
                + "-fx-border-color: #A52A2A; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand; -fx-padding: 8 20;");
        btnClear.setOnAction(e -> {
            cmbCat.setValue(null);
            cmbYear.setValue(null);
            cmbMonth.setValue(null);
            dpDate.setValue(null);
            updatePreview.run();
        });

        VBox content = new VBox(14, titleLabel, hintLabel, sep, row1, row2, previewLabel, btnClear);
        content.setPadding(new Insets(20, 25, 20, 25));
        content.setStyle("-fx-background-color: #333D29;");
        content.setPrefWidth(480);

        dialogPane.setContent(content);

        ButtonType applyType = new ButtonType("APPLY FILTER", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(applyType, cancelType);

        Button applyBtn = (Button) dialogPane.lookupButton(applyType);
        applyBtn.setStyle("-fx-background-color: #582F0E; -fx-text-fill: #F5F5F5; -fx-font-weight: bold; "
                + "-fx-border-color: #7F4F24; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand; -fx-padding: 8 20;");

        Button cancelBtn = (Button) dialogPane.lookupButton(cancelType);
        cancelBtn.setStyle("-fx-background-color: #414833; -fx-text-fill: #C8C8C8; -fx-font-weight: bold; "
                + "-fx-border-color: #582F0E; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand; -fx-padding: 8 20;");

        dialog.setResultConverter(buttonType -> {
            if (buttonType == applyType) {
                return filterGroups(allGroups, cmbCat.getValue(), cmbYear.getValue(),
                        cmbMonth.getValue(), dpDate.getValue());
            }
            return null;
        });

        Optional<List<Group>> result = dialog.showAndWait();
        result.ifPresent(filtered -> {
            cmbGroup.setItems(FXCollections.observableArrayList(filtered));
        });
    }

    private List<Group> filterGroups(List<Group> allGroups, GroupType cat, Integer year, Integer month,
            LocalDate date) {
        return allGroups.stream().filter(g -> {
            boolean match = true;
            if (cat != null && g.getCategory() != cat)
                match = false;
            if (year != null && g.getDateCreated() != null && g.getDateCreated().getYear() != year)
                match = false;
            if (month != null && g.getDateCreated() != null && g.getDateCreated().getMonthValue() != month)
                match = false;
            if (date != null && g.getDateCreated() != null && !g.getDateCreated().equals(date))
                match = false;
            return match;
        }).collect(Collectors.toList());
    }

    // ================== SEND ==================

    private void handleSend() {
        String msg;

        if (missionBriefMode) {
            // Compose mission brief as structured text
            String mission = txtMission.getText() != null ? txtMission.getText().trim() : "";
            String location = txtLocation.getText() != null ? txtLocation.getText().trim() : "";
            String time = txtTime.getText() != null ? txtTime.getText().trim() : "";
            AbstractUser commander = cmbMissionCommander.getValue();

            if (mission.isEmpty() || location.isEmpty() || time.isEmpty() || commander == null) {
                showAlert("Error", "Please fill all Mission Brief fields.");
                return;
            }

            msg = "[MISSION BRIEF]\n"
                    + "Mission: " + mission + "\n"
                    + "Location: " + location + "\n"
                    + "Time: " + time + "\n"
                    + "Commander: " + commander.getName();
        } else {
            msg = txtMessage.getText() != null ? txtMessage.getText().trim() : "";
        }

        String method = cmbEncryption.getValue();
        String appointment = cmbAppointment.getValue();
        String key = txtKey.getText() != null ? txtKey.getText().trim() : "";
        Group selectedGroup = cmbGroup.getValue();
        AbstractUser selectedRecipient = cmbRecipient.getValue();
        String priority = getSelectedPriority();

        // OOP Concept: Delegation
        // We delegate all validation and try-catch execution logic to our dedicated Exception Handler
        boolean success = com.wartime.system.handler.TransmissionExceptionHandler.attemptSend(
                msg, key, method, appointment, selectedGroup, selectedRecipient, priority);

        if (success) {
            clearMessageFields();
        }
    }

    private void clearMessageFields() {
        txtMessage.clear();
        txtKey.clear();
        if (missionBriefMode) {
            txtMission.clear();
            txtLocation.clear();
            txtTime.clear();
            cmbMissionCommander.setValue(null);
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleEmergency() {
        String msg = txtMessage.getText() != null ? txtMessage.getText().trim() : "";
        
        // OOP Concept: Delegation
        // The try-catch block is isolated in the Exception Handler class cleanly
        boolean success = com.wartime.system.handler.TransmissionExceptionHandler.attemptEmergency(msg);

        if (success) {
            txtMessage.clear();
        }
    }
}
