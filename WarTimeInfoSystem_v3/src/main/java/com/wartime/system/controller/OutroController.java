package com.wartime.system.controller;

import com.wartime.system.util.SceneNavigator;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OutroController {

    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    @FXML
    public void initialize() {
        btnYes.setOnAction
        (e -> {
            Platform.exit();
            System.exit(0);
        }
        
        );

        btnNo.setOnAction(e -> {
            
            SceneNavigator.getInstance().loadScene("option.fxml");
        });
    }
}
