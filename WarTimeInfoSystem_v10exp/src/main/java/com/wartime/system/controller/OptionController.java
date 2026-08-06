package com.wartime.system.controller;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Rank;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OptionController {

    @FXML
    private Button btnSend;
    @FXML
    private Button btnDecrypt;
    @FXML
    private Button btnCreateGroup;
    @FXML
    private Button btnLogout;

    @FXML
    public void initialize() {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        if (currentUser == null) {
            SceneNavigator.getInstance().loadScene("login.fxml");
            return;
        }
        Rank rank = currentUser.getRank();

        btnSend.setOnAction(e -> SceneNavigator.getInstance().loadScene("sender.fxml"));
        btnDecrypt.setOnAction(e -> SceneNavigator.getInstance().loadScene("receiver.fxml"));

        // GROUP MANAGEMENT visible only for COMMANDER and OFFICER
        if (rank == Rank.COMMANDER || rank == Rank.OFFICER) {
            btnCreateGroup.setVisible(true);
            btnCreateGroup.setManaged(true);
            btnCreateGroup.setOnAction(e -> SceneNavigator.getInstance().loadScene("group.fxml"));
        } else {
            btnCreateGroup.setVisible(false);
            btnCreateGroup.setManaged(false);
        }

        btnLogout.setOnAction(e -> {
            SecurityContext.getInstance().setCurrentUser(null);
            SceneNavigator.getInstance().loadScene("login.fxml");
        });
    }
}
