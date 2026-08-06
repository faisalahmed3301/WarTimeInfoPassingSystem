package com.wartime.system.controller;

import com.wartime.system.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class InfoController {

    @FXML
    private Button btnBack;

    @FXML
    public void initialize() {
        btnBack.setOnAction(e -> SceneNavigator.getInstance().loadScene("intro.fxml"));
    }
}
