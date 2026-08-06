package com.wartime.system.controller;

import com.wartime.system.model.*;
import com.wartime.system.service.AuthenticationService;
import com.wartime.system.service.GroupService;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.util.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

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
    private Button btnCreateGroup;
    @FXML
    private Button btnDeleteGroup;
    @FXML
    private Button btnBack;

    private AbstractUser currentUser;

    @FXML
    public void initialize() {
        currentUser = SecurityContext.getInstance().getCurrentUser();

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
                    setText(group.getName() + " [" + group.getType() + "] (" + group.getMembers().size() + " members)");
                }
            }
        });

        btnCreateGroup.setOnAction(e -> handleCreateGroup());
        btnDeleteGroup.setOnAction(e -> handleDeleteGroup());
        btnBack.setOnAction(e -> SceneNavigator.getInstance().loadScene("option.fxml"));

        // Disable and hide create button for soldiers if they somehow get here
        if (currentUser.getRank() == Rank.SOLDIER) {
            btnCreateGroup.setDisable(true);
            btnCreateGroup.setVisible(false);
            btnCreateGroup.setManaged(false);
        }
    }

    private void handleCreateGroup() {
        String name = txtGroupName.getText();
        GroupType category = cmbGroupType.getValue();
        String customType = txtCustomType.getText();
        List<AbstractUser> selectedMembers = lstUsers.getSelectionModel().getSelectedItems();

        if (name == null || name.isEmpty() || category == null) {
            showAlert("Error", "Please provide group name and type.");
            return;
        }

        try {
            GroupService.getInstance().createGroup(name, category, customType, currentUser, selectedMembers);
            refreshGroupList();
            clearFields();
            showAlert("Success", "Group created successfully.");
        } catch (Exception e) {
            showAlert("Error", e.getMessage());
        }
    }

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
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
