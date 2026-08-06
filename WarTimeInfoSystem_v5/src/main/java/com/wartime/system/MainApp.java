package com.wartime.system;

import com.wartime.system.service.AuthenticationService;
import com.wartime.system.util.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Load Data from Excel
        com.wartime.system.util.ExcelStorageManager.loadAllData();

        // Seed Users if none exist
        if (AuthenticationService.getInstance().getUsers().isEmpty()) {
            seedUsers();
            com.wartime.system.util.ExcelStorageManager.saveAllData();
        }

        // Setup Navigator
        SceneNavigator.getInstance().setMainStage(primaryStage);
        primaryStage.setTitle("War-Time Information Passing System");

        // 85% of Screen Size
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(screenBounds.getWidth() * 0.85);
        primaryStage.setHeight(screenBounds.getHeight() * 0.85);
        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(event -> {
            com.wartime.system.util.ExcelStorageManager.saveAllData();
            event.consume();
            SceneNavigator.getInstance().loadScene("outro.fxml");
        });

        SceneNavigator.getInstance().loadScene("intro.fxml");
    }

    private void seedUsers() {
        AuthenticationService auth = AuthenticationService.getInstance();

        // Priority 1: Commander
        auth.register("cmd1", "COMMANDER", "cmd1_123");

        // Priority 2: Officers
        auth.register("offcr1", "OFFICER", "offcr1_123");
        auth.register("offcr2", "OFFICER", "offcr2_123");

        // Priority 3: Soldiers
        for (int i = 1; i <= 5; i++) {
            String name = "soldr" + i;
            String pass = "soldr" + i + "_123";
            auth.register(name, "SOLDIER", pass);
        }

        System.out.println("Users seeded: cmd1, offcr1, offcr2, soldr1-5");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
