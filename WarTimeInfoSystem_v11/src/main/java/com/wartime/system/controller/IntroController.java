package com.wartime.system.controller;

import com.wartime.system.util.SceneNavigator;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;

public class IntroController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnNext;

    private final Random random = new Random();

    @FXML
    public void initialize() {
        btnInfo.setOnAction(e -> handleInfo());
        btnNext.setOnAction(e -> handleNext());

        applyEnhancedSparkAnimation();
    }

    private void applyEnhancedSparkAnimation() {
        // Spawn sparks frequently
        Timeline sparkTimeline = new Timeline(new KeyFrame(Duration.millis(200), e -> spawnSpark()));
        sparkTimeline.setCycleCount(Animation.INDEFINITE);
        sparkTimeline.play();

        // Spawn fireballs less frequently
        Timeline fireballTimeline = new Timeline(new KeyFrame(Duration.millis(1200), e -> spawnFireball()));
        fireballTimeline.setCycleCount(Animation.INDEFINITE);
        fireballTimeline.play();
    }

    private void spawnSpark() {
        double startX, startY;
        int side = random.nextInt(3); // 0: Bottom, 1: Left, 2: Right

        if (side == 0) {
            startX = random.nextDouble() * 1200;
            startY = 720;
        } else if (side == 1) {
            startX = -20;
            startY = random.nextDouble() * 700;
        } else {
            startX = 1300;
            startY = random.nextDouble() * 700;
        }

        Circle spark = new Circle(1 + random.nextDouble() * 2, Color.GOLD);
        spark.setCenterX(startX);
        spark.setCenterY(startY);
        spark.setOpacity(0.8);
        spark.setEffect(new javafx.scene.effect.Glow(0.8));

        rootPane.getChildren().add(0, spark);

        // Move slower (increased duration)
        double targetX = random.nextDouble() * 1200;
        double targetY = random.nextDouble() * 400 - 100; // Towards upper half

        TranslateTransition tt = new TranslateTransition(Duration.seconds(6 + random.nextDouble() * 6), spark);
        tt.setToX(targetX - startX);
        tt.setToY(targetY - startY);

        FadeTransition ft = new FadeTransition(Duration.seconds(6 + random.nextDouble() * 6), spark);
        ft.setFromValue(0.8);
        ft.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(e -> rootPane.getChildren().remove(spark));
        pt.play();
    }

    private void spawnFireball() {
        double startX, startY;
        int side = random.nextInt(3);

        if (side == 0) {
            startX = random.nextDouble() * 1200;
            startY = 750;
        } else if (side == 1) {
            startX = -50;
            startY = 400 + random.nextDouble() * 300;
        } else {
            startX = 1350;
            startY = 400 + random.nextDouble() * 300;
        }

        double radius = 5 + random.nextDouble() * 5;
        RadialGradient gradient = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.YELLOW),
                new Stop(0.6, Color.ORANGERED),
                new Stop(1, Color.TRANSPARENT));

        Circle fireball = new Circle(radius, gradient);
        fireball.setCenterX(startX);
        fireball.setCenterY(startY);
        fireball.setEffect(new javafx.scene.effect.GaussianBlur(5));

        rootPane.getChildren().add(0, fireball);

        double targetX = 600 + (random.nextDouble() - 0.5) * 800;
        double targetY = -200;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(8 + random.nextDouble() * 5), fireball);
        tt.setToX(targetX - startX);
        tt.setToY(targetY - startY);

        FadeTransition ft = new FadeTransition(Duration.seconds(8 + random.nextDouble() * 5), fireball);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);

        ParallelTransition pt = new ParallelTransition(tt, ft);
        pt.setOnFinished(e -> rootPane.getChildren().remove(fireball));
        pt.play();
    }

    private void handleInfo() {
        SceneNavigator.getInstance().loadScene("info.fxml");
    }

    private void handleNext() {
        SceneNavigator.getInstance().loadScene("login.fxml");
    }
}
