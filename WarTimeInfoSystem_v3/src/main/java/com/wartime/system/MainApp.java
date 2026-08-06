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
        //  Commander
        auth.register("Faisal", "COMMANDER", "123");
        //  Officer
        auth.register("Imdad", "OFFICER", "123");
        //  Soldier                  
        auth.register("Mugdho", "SOLDIER", "123");

        System.out.println("Users seeded: Faisal (COMMANDER), Imdad (OFFICER), Mugdho (SOLDIER) - Pass: 123");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
