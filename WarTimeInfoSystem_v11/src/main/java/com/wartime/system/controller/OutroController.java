package com.wartime.system.controller;

import com.wartime.system.util.SceneNavigator;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;

public class OutroController {

    @FXML
    private Pane sparkPane;

    @FXML
    private Button btnYes;
    @FXML
    private Button btnNo;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        btnYes.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        btnNo.setOnAction(e -> {
            if (com.wartime.system.security.SecurityContext.getInstance().getCurrentUser() == null) {
                SceneNavigator.getInstance().loadScene("intro.fxml");
            } else {
                SceneNavigator.getInstance().loadScene("option.fxml");
            }
        });

        applyEnhancedSparkAnimation();
    }

    private void applyEnhancedSparkAnimation() {
        // High frequency for intense feel but slow movement
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(150), e -> spawnRandomSpark()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void spawnRandomSpark() {
        double startX, startY, targetX, targetY;

        // Randomly choose a side to spawn from: Bottom, Left, or Right
        int side = random.nextInt(3);
        if (side == 0) { // Bottom
            startX = random.nextDouble() * 800;
            startY = 620;
            targetX = random.nextDouble() * 800;
            targetY = -100;
        } else if (side == 1) { // Left side
            startX = -30;
            startY = random.nextDouble() * 600;
            targetX = 900;
            targetY = random.nextDouble() * 600;
        } else { // Right side
            startX = 830;
            startY = random.nextDouble() * 600;
            targetX = -100;
            targetY = random.nextDouble() * 600;
        }

        Circle spark = new Circle(1 + random.nextDouble() * 2);
        spark.setFill(random.nextBoolean() ? Color.ORANGERED : Color.GOLD);
        spark.setCenterX(startX);
        spark.setCenterY(startY);
        spark.setOpacity(0.7);
        spark.setEffect(new javafx.scene.effect.Glow(0.6));

        sparkPane.getChildren().add(spark);

        // Move slower (increased duration)
        double durationSeconds = 5 + random.nextDouble() * 5;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(durationSeconds), spark);
        tt.setToX(targetX - startX);
        tt.setToY(targetY - startY);

        FadeTransition ft = new FadeTransition(Duration.seconds(durationSeconds), spark);
        ft.setFromValue(0.7);
        ft.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(e -> sparkPane.getChildren().remove(spark));
        pt.play();
    }
}
