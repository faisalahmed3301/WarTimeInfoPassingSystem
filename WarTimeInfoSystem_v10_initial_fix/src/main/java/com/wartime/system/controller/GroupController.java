package com.wartime.system.controller;

import com.wartime.system.model.*;
import com.wartime.system.service.AuthenticationService;
import com.wartime.system.service.GroupService;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GroupController {

    @FXML
    private TextField txtGroupName;
    @FXML
    private ComboBox<GroupType> cmbGroupType;
    @FXML
    private TextField txtCustomType;
    @FXML
    private ListView<AbstractUser> lstUsers;
    @FXML
    private ListView<Group> lstGroups;
    @FXML
    private DatePicker dpDateCreated;
    @FXML
    private Button btnCreateGroup;
    @FXML
    private Button btnDeleteGroup;
    @FXML
    private Button btnBack;

    private AbstractUser currentUser;

    @FXML
    public void initialize() {
        currentUser = SecurityContext.getInstance().getCurrentUser();
        if (currentUser == null)
            return;

        // Hide/Show delete button based on rank
        if (currentUser.getRank() == Rank.COMMANDER) {
            btnDeleteGroup.setVisible(true);
            btnDeleteGroup.setManaged(true);
        }

        // Setup Group Types
        cmbGroupType.setItems(FXCollections.observableArrayList(GroupType.values()));
        cmbGroupType.setOnAction(e -> {
            txtCustomType.setVisible(cmbGroupType.getValue() == GroupType.OTHER);
        });

        // Set default date
        dpDateCreated.setValue(LocalDate.now());

        // Setup User List
        ObservableList<AbstractUser> allUsers = FXCollections.observableArrayList(
                AuthenticationService.getInstance().getUsers().values());
        lstUsers.setItems(allUsers);
        lstUsers.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstUsers.setCellFactory(lv -> new ListCell<AbstractUser>() {
            @Override
            protected void updateItem(AbstractUser user, boolean empty) {
                super.updateItem(user, empty);
                setText(empty ? null : user.getName() + " (" + user.getRank() + ")");
            }
        });

        // Setup Group List
        refreshGroupList();
        lstGroups.setCellFactory(lv -> new ListCell<Group>() {
            @Override
            protected void updateItem(Group group, boolean empty) {
                super.updateItem(group, empty);
                if (empty || group == null) {
                    setText(null);
                } else {
                    setText(group.getName() + " [" + group.getType() + "] ("
                            + group.getMembers().size() + " members) - Created: " + group.getDateCreated());
                }
            }
        });

        // Double-click a group to open the Update popup
        lstGroups.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                Group selected = lstGroups.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    boolean canEdit = currentUser.getRank() == Rank.COMMANDER
                            || selected.getCreator().getName().equals(currentUser.getName())
                            || (currentUser.getRank() == Rank.OFFICER && selected.isMember(currentUser));
                    if (canEdit) {
                        showUpdatePopup(selected);
                    } else {
                        showAlert("Access Denied", "You are not authorized to edit this group.");
                    }
                }
            }
        });

        btnCreateGroup.setOnAction(e -> handleCreateGroup());
        btnDeleteGroup.setOnAction(e -> handleDeleteGroup());
        btnBack.setOnAction(e -> SceneNavigator.getInstance().loadScene("option.fxml"));

        // Disable and hide create button for soldiers
        if (currentUser.getRank() == Rank.SOLDIER) {
            btnCreateGroup.setDisable(true);
            btnCreateGroup.setVisible(false);
            btnCreateGroup.setManaged(false);
        }
    }

    // ================== CREATE GROUP ==================

    private void handleCreateGroup() {
        String name = txtGroupName.getText();
        GroupType category = cmbGroupType.getValue();
        String customType = txtCustomType.getText();
        LocalDate dateCreated = dpDateCreated.getValue();
        List<AbstractUser> selectedMembers = new ArrayList<>(lstUsers.getSelectionModel().getSelectedItems());

        if (name == null || name.isEmpty() || category == null || dateCreated == null) {
            showAlert("Error", "Please provide group name, type, and creation date.");
            return;
        }

        try {
            GroupService.getInstance().createGroup(name, category, customType, currentUser, selectedMembers, dateCreated);
            refreshGroupList();
            clearFields();
            showAlert("Success", "Group created successfully.");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    // ================== UPDATE GROUP POPUP ==================

    private void showUpdatePopup(Group group) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Update Group: " + group.getName());
        dialog.setHeaderText(null);
        dialog.setResizable(true);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/com/wartime/system/view/styles.css").toExternalForm());
        dialogPane.setStyle("-fx-background-color: #333D29;");
        dialogPane.setPrefHeight(520);
        dialogPane.setMinHeight(400);

        String fieldStyle = "-fx-background-color: #2A3020; -fx-text-fill: #E8E8E8; "
                + "-fx-border-color: #582F0E; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6;";
        String labelStyle = "-fx-text-fill: #E8E8E8; -fx-font-weight: bold;";

        // ---- Title ----
        Label titleLabel = new Label("✏ UPDATE GROUP");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #E8E8E8;");

        Label groupNameLabel = new Label("\"" + group.getName() + "\" — " + group.getType());
        groupNameLabel.setStyle("-fx-text-fill: #A4AC86; -fx-font-size: 13px;");

        // ---- Group Name Edit ----
        Label editNameLabel = new Label("Group Name");
        editNameLabel.setStyle(labelStyle);
        TextField editName = new TextField(group.getName());
        editName.setStyle(fieldStyle);
        editName.setPrefWidth(350);

        // ---- Separator ----
        Region sep1 = new Region();
        sep1.setPrefHeight(1);
        sep1.setStyle("-fx-background-color: #582F0E;");

        // ---- Current Members ----
        Label membersTitle = new Label("👥 CURRENT MEMBERS (" + group.getMembers().size() + ")");
        membersTitle.setStyle("-fx-text-fill: #E8E8E8; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label membersHint = new Label("Click a member and press 'Remove' to remove them from the group.");
        membersHint.setStyle("-fx-text-fill: #7A8360; -fx-font-size: 11px;");

        ListView<AbstractUser> lstCurrentMembers = new ListView<>();
        ObservableList<AbstractUser> currentMembers = FXCollections.observableArrayList(
                new ArrayList<>(group.getMembers()));
        lstCurrentMembers.setItems(currentMembers);
        lstCurrentMembers.setPrefHeight(100);
        lstCurrentMembers.setStyle("-fx-background-color: #2A3020; -fx-border-color: #582F0E;");
        lstCurrentMembers.setCellFactory(lv -> new ListCell<AbstractUser>() {
            @Override
            protected void updateItem(AbstractUser user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setStyle("");
                } else {
                    boolean isCreator = user.getName().equals(group.getCreator().getName());
                    setText(user.getName() + " (" + user.getRank() + ")"
                            + (isCreator ? "  👑 Creator" : ""));
                    if (isCreator) {
                        setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #E8E8E8;");
                    }
                }
            }
        });

        Button btnRemoveMember = new Button("✖ REMOVE SELECTED");
        btnRemoveMember.setStyle("-fx-background-color: #8B0000; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; "
                + "-fx-border-color: #A52A2A; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand; -fx-padding: 6 16;");
        btnRemoveMember.setOnAction(e -> {
            AbstractUser selectedUser = lstCurrentMembers.getSelectionModel().getSelectedItem();
            if (selectedUser == null) {
                showAlert("Error", "Select a member to remove.");
                return;
            }
            if (selectedUser.getName().equals(group.getCreator().getName())) {
                showAlert("Error", "Cannot remove the group creator.");
                return;
            }
            currentMembers.remove(selectedUser);
            membersTitle.setText("👥 CURRENT MEMBERS (" + currentMembers.size() + ")");
        });

        // ---- Separator ----
        Region sep2 = new Region();
        sep2.setPrefHeight(1);
        sep2.setStyle("-fx-background-color: #582F0E;");

        // ---- Add Personnel ----
        Label addTitle = new Label("➕ ADD PERSONNEL");
        addTitle.setStyle("-fx-text-fill: #E8E8E8; -fx-font-weight: bold; -fx-font-size: 14px;");
        Label addHint = new Label("Select users below and click 'Add' to include them in the group.");
        addHint.setStyle("-fx-text-fill: #7A8360; -fx-font-size: 11px;");

        // Available users = all users NOT in current members
        ListView<AbstractUser> lstAvailable = new ListView<>();
        lstAvailable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstAvailable.setPrefHeight(90);
        lstAvailable.setStyle("-fx-background-color: #2A3020; -fx-border-color: #582F0E;");
        lstAvailable.setCellFactory(lv -> new ListCell<AbstractUser>() {
            @Override
            protected void updateItem(AbstractUser user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getName() + " (" + user.getRank() + ")");
                    setStyle("-fx-text-fill: #A4AC86;");
                }
            }
        });

        // Refresh available list whenever current members change
        Runnable refreshAvailable = () -> {
            List<AbstractUser> available = AuthenticationService.getInstance().getUsers().values().stream()
                    .filter(u -> !currentMembers.contains(u))
                    .collect(Collectors.toList());
            lstAvailable.setItems(FXCollections.observableArrayList(available));
        };
        refreshAvailable.run();

        Button btnAddMember = new Button("➕ ADD SELECTED");
        btnAddMember.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: #FFFFFF; -fx-font-weight: bold; "
                + "-fx-border-color: #388e3c; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand; -fx-padding: 6 16;");
        btnAddMember.setOnAction(e -> {
            List<AbstractUser> selected = new ArrayList<>(lstAvailable.getSelectionModel().getSelectedItems());
            if (selected.isEmpty()) {
                showAlert("Error", "Select users to add.");
                return;
            }
            for (AbstractUser u : selected) {
                if (!currentMembers.contains(u)) {
                    currentMembers.add(u);
                }
            }
            membersTitle.setText("👥 CURRENT MEMBERS (" + currentMembers.size() + ")");
            refreshAvailable.run();
        });

        // Also refresh available list when a member is removed
        currentMembers.addListener((javafx.collections.ListChangeListener<AbstractUser>) c -> {
            refreshAvailable.run();
        });

        // ---- Layout ----
        ScrollPane scrollPane = new ScrollPane();
        VBox content = new VBox(6,
                titleLabel, groupNameLabel,
                editNameLabel, editName,
                sep1,
                membersTitle, membersHint, lstCurrentMembers, btnRemoveMember,
                sep2,
                addTitle, addHint, lstAvailable, btnAddMember);
        content.setPadding(new Insets(14, 20, 14, 20));
        content.setStyle("-fx-background-color: #333D29;");
        content.setPrefWidth(480);
        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #333D29; -fx-background-color: #333D29;");

        dialogPane.setContent(scrollPane);

        // Buttons
        ButtonType saveType = new ButtonType("SAVE CHANGES", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelType = new ButtonType("CANCEL", ButtonBar.ButtonData.CANCEL_CLOSE);
        dialogPane.getButtonTypes().addAll(saveType, cancelType);

        // Style buttons
        Button saveBtn = (Button) dialogPane.lookupButton(saveType);
        saveBtn.setStyle("-fx-background-color: #582F0E; -fx-text-fill: #F5F5F5; -fx-font-weight: bold; "
                + "-fx-border-color: #7F4F24; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand; -fx-padding: 8 20;");

        Button cancelBtn = (Button) dialogPane.lookupButton(cancelType);
        cancelBtn.setStyle("-fx-background-color: #414833; -fx-text-fill: #C8C8C8; -fx-font-weight: bold; "
                + "-fx-border-color: #582F0E; -fx-border-width: 1; -fx-border-radius: 6; -fx-background-radius: 6; "
                + "-fx-cursor: hand; -fx-padding: 8 20;");

        dialog.setResultConverter(buttonType -> {
            if (buttonType == saveType) {
                String newName = editName.getText() != null ? editName.getText().trim() : "";
                if (newName.isEmpty()) {
                    showAlert("Error", "Group name cannot be empty.");
                    return null;
                }

                try {
                    GroupService.getInstance().updateGroup(
                            group, newName,
                            group.getCategory(), group.getType(),
                            group.getDateCreated(),
                            new ArrayList<>(currentMembers),
                            currentUser);
                    refreshGroupList();
                    showAlert("Success", "Group updated successfully.");
                } catch (Exception ex) {
                    showAlert("Error", ex.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    // ================== DELETE GROUP ==================

    private void handleDeleteGroup() {
        Group selected = lstGroups.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Select a group to delete.");
            return;
        }
        try {
            GroupService.getInstance().deleteGroup(selected, currentUser);
            refreshGroupList();
            showAlert("Success", "Group deleted.");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

    private void refreshGroupList() {
        lstGroups.setItems(FXCollections.observableArrayList(
                GroupService.getInstance().getVisibleGroups(currentUser)));
    }

    private void clearFields() {
        txtGroupName.clear();
        txtCustomType.clear();
        cmbGroupType.getSelectionModel().clearSelection();
        lstUsers.getSelectionModel().clearSelection();
        dpDateCreated.setValue(LocalDate.now());
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
