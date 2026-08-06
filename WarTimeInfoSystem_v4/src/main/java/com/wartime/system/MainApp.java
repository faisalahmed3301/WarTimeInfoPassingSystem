package com.wartime.system;

import com.wartime.system.service.AuthenticationService;
import com.wartime.system.util.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Seed Users
        seedUsers();

        // Setup Navigator
        SceneNavigator.getInstance().setMainStage(primaryStage);
        primaryStage.setTitle("War-Time Information Passing System");

        // 85% of Screen Size
        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        primaryStage.setWidth(screenBounds.getWidth() * 0.85);
        primaryStage.setHeight(screenBounds.getHeight() * 0.85);
        primaryStage.centerOnScreen();

        primaryStage.setOnCloseRequest(event -> {
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
        auth.register("offc1", "OFFICER", "offc1_123");
        auth.register("offc2", "OFFICER", "offc2_123");

        // Priority 3: Soldiers
        for (int i = 1; i <= 6; i++) {
            String name = "soldier" + i;
            auth.register(name, "SOLDIER", name + "_123");
        }

        System.out.println("Users seeded: cmd1 (COMMANDER), offc(1-2) (OFFICER), soldier(1-6) (SOLDIER)");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
