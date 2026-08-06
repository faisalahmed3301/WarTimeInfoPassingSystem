package com.wartime.system.controller;

import com.wartime.system.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OptionController {

    @FXML
    private Button btnSend;
    @FXML
    private Button btnDecrypt;

    @FXML
    public void initialize() {
        btnSend.setOnAction(e -> SceneNavigator.getInstance().loadScene("sender.fxml"));
        btnDecrypt.setOnAction(e -> SceneNavigator.getInstance().loadScene("receiver.fxml"));
    }
}
