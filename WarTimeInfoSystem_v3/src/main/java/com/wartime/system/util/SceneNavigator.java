package com.wartime.system.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {
    private static SceneNavigator instance;
    private Stage mainStage;

    private SceneNavigator() {
    }

    public static synchronized SceneNavigator getInstance() {
        if (instance == null) {
            instance = new SceneNavigator();
        }
        return instance;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public void loadScene(String fxmlPath) { // fxmlPath : resources/com/wartime/system/view/
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wartime/system/view/" + fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/wartime/system/view/styles.css").toExternalForm());
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
           // show alert
        }
    }
}
