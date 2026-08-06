package com.wartime.system.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import com.wartime.system.security.SecurityContext;
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

    public void loadScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/wartime/system/view/" + fxmlPath));
            Parent root = loader.load();

            // Apply theme preference
            if (SecurityContext.getInstance().isLightMode()) {
                root.getStyleClass().add("light-mode");
            }

            Parent sceneRoot = root;

            // Inject theme toggle on all standard-root pages (not intro/outro)
            boolean isStandardPage = root.getStyleClass().contains("standard-root");
            if (isStandardPage) {
                sceneRoot = wrapWithThemeToggle(root);
            }

            Scene scene = new Scene(sceneRoot);
            scene.getStylesheets().add(getClass().getResource("/com/wartime/system/view/styles.css").toExternalForm());

            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Navigation Error");
            alert.setHeaderText("Failed to load scene: " + fxmlPath);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private Parent wrapWithThemeToggle(Parent root) {
        StackPane wrapper = new StackPane();
        Button themeBtn = new Button();
        themeBtn.getStyleClass().add("theme-toggle");

        // Subtle Background Image Layer
        ImageView bgView = new ImageView(
                new Image(getClass().getResourceAsStream("/com/wartime/system/view/images/other_bg1.jpg")));
        bgView.setOpacity(0.3); // Increased to 30%
        bgView.setPreserveRatio(false);
        bgView.fitWidthProperty().bind(wrapper.widthProperty());
        bgView.fitHeightProperty().bind(wrapper.heightProperty());

        // Load image assets
        Image sunIcon = new Image(getClass().getResourceAsStream("/com/wartime/system/view/images/sun.png"));
        Image moonIcon = new Image(getClass().getResourceAsStream("/com/wartime/system/view/images/moon.png"));

        // Helper to update UI state
        Runnable updateUI = () -> {
            boolean isLight = SecurityContext.getInstance().isLightMode();

            ImageView iconView = new ImageView(isLight ? moonIcon : sunIcon);
            iconView.setFitWidth(28); // Slightly larger as requested
            iconView.setFitHeight(28);
            iconView.setPreserveRatio(true);

            themeBtn.setGraphic(iconView);
            themeBtn.setText(""); // Remove emoji text

            if (isLight) {
                if (!wrapper.getStyleClass().contains("light-mode"))
                    wrapper.getStyleClass().add("light-mode");
                if (!root.getStyleClass().contains("light-mode"))
                    root.getStyleClass().add("light-mode");
            } else {
                wrapper.getStyleClass().remove("light-mode");
                root.getStyleClass().remove("light-mode");
            }
        };

        // Initial state
        updateUI.run();

        themeBtn.setOnAction(e -> {
            SecurityContext.getInstance().setLightMode(!SecurityContext.getInstance().isLightMode());
            updateUI.run();
        });

        wrapper.getChildren().addAll(bgView, root, themeBtn);
        StackPane.setAlignment(themeBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(themeBtn, new Insets(15, 20, 0, 0));

        // CRITICAL: Make the inner root transparent so the background image is visible
        // through it
        root.setStyle("-fx-background-color: transparent;");

        wrapper.getStyleClass().add("standard-root");

        return wrapper;
    }
}
