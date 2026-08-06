package com.wartime.system.controller;

import com.wartime.system.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class IntroController {

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnNext;

    @FXML
    public void initialize() {
        btnInfo.setOnAction(e -> handleInfo());
        btnNext.setOnAction(e -> handleNext());
    }

    private void handleInfo() {
        SceneNavigator.getInstance().loadScene("info.fxml");
    }

    private void handleNext() {
        SceneNavigator.getInstance().loadScene("login.fxml");
    }
}
