package com.wartime.system.controller;

import com.wartime.system.model.AbstractUser;
import com.wartime.system.model.Rank;
import com.wartime.system.model.SecureMessage;
import com.wartime.system.security.SecurityContext;
import com.wartime.system.service.MessageService;
import com.wartime.system.util.SceneNavigator;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.*;
import java.util.stream.Collectors;

public class ReceiverController {

    @FXML
    private ListView<SecureMessage> lstMessages;
    @FXML
    private Label lblStatus;
    @FXML
    private ToggleButton tglPrivate;
    @FXML
    private ToggleButton tglGroup;
    @FXML
    private Button btnReturn;
    @FXML
    private Button btnExit;

    // Track failed decryption attempts per message
    private final Map<SecureMessage, Integer> failedAttempts = new HashMap<>();

    @FXML
    public void initialize() {
        ToggleGroup group = new ToggleGroup();
        tglPrivate.setToggleGroup(group);
        tglGroup.setToggleGroup(group);

        btnReturn.setOnAction(e -> SceneNavigator.getInstance().loadScene("option.fxml"));
        btnExit.setOnAction(e -> SceneNavigator.getInstance().loadScene("outro.fxml"));

        tglPrivate.setOnAction(e -> refreshMessages());
        tglGroup.setOnAction(e -> refreshMessages());

        refreshMessages();
        setupContextMenu();

        lstMessages.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(SecureMessage item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                } else {
                    String prefix;
                    if (item.isEmergency()) {
                        prefix = "[⚠ EMERGENCY] ";
                    } else if (item.getTargetUser() != null) {
                        prefix = "[PRIVATE] ";
                    } else {
                        prefix = "[GROUP] ";
                    }

                    // Priority badge
                    String priorityBadge = "";
                    String priority = item.getPriority() != null ? item.getPriority() : "NORMAL";
                    if ("CRITICAL".equals(priority)) {
                        priorityBadge = " [!! CRITICAL] ";
                    } else if ("WARNING".equals(priority)) {
                        priorityBadge = " [! WARNING] ";
                    }

                    String unreadMark = item.isRead() ? "" : " ● ";
                    setText(unreadMark + prefix + priorityBadge + "TRANSMISSION ID: "
                            + Integer.toHexString(item.hashCode()).toUpperCase());

                    // Color-code based on priority and state
                    if (item.isEmergency() && !item.isRead()) {
                        setStyle("-fx-font-weight: bold; -fx-text-fill: #ff4444; -fx-background-color: rgba(139,0,0,0.2);");
                    } else if (!item.isRead()) {
                        // Use priority color for unread messages
                        switch (priority) {
                            case "CRITICAL":
                                setStyle("-fx-font-weight: bold; -fx-text-fill: #ff4444;");
                                break;
                            case "WARNING":
                                setStyle("-fx-font-weight: bold; -fx-text-fill: #ffcc00;");
                                break;
                            default:
                                setStyle("-fx-font-weight: bold; -fx-text-fill: #00ff00;");
                                break;
                        }
                    } else {
                        // Read messages still tinted by priority
                        switch (priority) {
                            case "CRITICAL":
                                setStyle("-fx-text-fill: #cc3333;");
                                break;
                            case "WARNING":
                                setStyle("-fx-text-fill: #aa8800;");
                                break;
                            default:
                                setStyle("-fx-background-color: transparent;");
                                break;
                        }
                    }
                }
            }
        });

        lstMessages.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                SecureMessage selected = lstMessages.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    handleDecrypt(selected);
                }
            }
        });

        // Check for unread emergency alerts and show them immediately
        showPendingEmergencyAlerts();
    }

    private void setupContextMenu() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Delete Transmission");
        deleteItem.getStyleClass().add("menu-item");
        deleteItem.setOnAction(e -> {
            SecureMessage selected = lstMessages.getSelectionModel().getSelectedItem();
            if (selected != null) {
                MessageService.getInstance().deleteMessage(selected);
                refreshMessages();
            }
        });
        contextMenu.getItems().add(deleteItem);
        lstMessages.setContextMenu(contextMenu);
    }

    private void refreshMessages() {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        var allVisible = MessageService.getInstance().getVisibleMessages(currentUser);

        List<SecureMessage> filtered;
        if (tglPrivate.isSelected()) {
            filtered = allVisible.stream()
                    .filter(m -> m.getTargetUser() != null || (m.getTargetGroup() == null && m.getTargetUser() == null))
                    .collect(Collectors.toList());
            lblStatus.setText("Private/Rank Transmissions for " + currentUser.getName() + ":");
        } else {
            filtered = allVisible.stream()
                    .filter(m -> m.getTargetGroup() != null)
                    .collect(Collectors.toList());
            lblStatus.setText("Group Operational Transmissions:");
        }

        // Sort: Unread first
        filtered.sort((a, b) -> Boolean.compare(a.isRead(), b.isRead()));

        lstMessages.setItems(FXCollections.observableArrayList(filtered));
    }

    // =====================================================================
    //  EMERGENCY ALERT POPUP (Feature 3)
    // =====================================================================
    private void showPendingEmergencyAlerts() {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
        List<SecureMessage> emergencies = MessageService.getInstance().getVisibleMessages(currentUser).stream()
                .filter(m -> m.isEmergency() && !m.isRead())
                .collect(Collectors.toList());

        if (!emergencies.isEmpty()) {
            // Show all unread emergency messages in sequence
            javafx.application.Platform.runLater(() -> {
                for (SecureMessage em : emergencies) {
                    showEmergencyPopup(em);
                    MessageService.getInstance().markAsRead(em);
                }
                refreshMessages();
            });
        }
    }

    private void showEmergencyPopup(SecureMessage message) {
        Stage alertStage = new Stage();
        alertStage.initModality(Modality.APPLICATION_MODAL);
        alertStage.initStyle(StageStyle.UNDECORATED);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #1a0000;");
        root.setPrefSize(700, 450);

        // Pulsing red border
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: #ff0000; -fx-border-width: 4; -fx-background-color: transparent;");

        VBox content = new VBox(20);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        // Warning icon
        Text warningIcon = new Text("⚠");
        warningIcon.setFont(Font.font("System", FontWeight.BOLD, 60));
        warningIcon.setFill(Color.RED);

        // Title
        Text title = new Text("EMERGENCY ALERT");
        title.setFont(Font.font("System", FontWeight.BOLD, 36));
        title.setFill(Color.RED);
        title.setTextAlignment(TextAlignment.CENTER);
        Glow titleGlow = new Glow(0.8);
        title.setEffect(titleGlow);

        // Separator
        Text separator = new Text("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        separator.setFill(Color.DARKRED);
        separator.setFont(Font.font("Monospaced", 14));

        // Message content
        Text msgText = new Text(message.getEncryptedContent());
        msgText.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        msgText.setFill(Color.WHITE);
        msgText.setTextAlignment(TextAlignment.CENTER);
        msgText.setWrappingWidth(600);

        // From commander label
        Text fromLabel = new Text("— COMMANDER BROADCAST —");
        fromLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        fromLabel.setFill(Color.rgb(255, 100, 100));

        // Acknowledge button
        Button btnAck = new Button("ACKNOWLEDGE");
        btnAck.setStyle("-fx-background-color: #8B0000; -fx-text-fill: white; -fx-font-size: 16px; "
                + "-fx-font-weight: bold; -fx-padding: 12 40; -fx-border-color: #ff0000; "
                + "-fx-border-width: 2; -fx-background-radius: 8; -fx-border-radius: 8; -fx-cursor: hand;");
        btnAck.setOnAction(e -> alertStage.close());

        content.getChildren().addAll(warningIcon, title, separator, msgText, fromLabel, btnAck);

        borderPane.setCenter(content);
        root.getChildren().add(borderPane);

        // Pulse animation on border
        Timeline pulseTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(root.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(root.opacityProperty(), 0.85)),
                new KeyFrame(Duration.millis(1000),
                        new KeyValue(root.opacityProperty(), 1.0)));
        pulseTimeline.setCycleCount(Animation.INDEFINITE);

        // Title glow animation
        Timeline glowTimeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(titleGlow.levelProperty(), 0.3)),
                new KeyFrame(Duration.millis(800), new KeyValue(titleGlow.levelProperty(), 1.0)),
                new KeyFrame(Duration.millis(1600), new KeyValue(titleGlow.levelProperty(), 0.3)));
        glowTimeline.setCycleCount(Animation.INDEFINITE);

        // Warning icon scale animation
        ScaleTransition iconPulse = new ScaleTransition(Duration.millis(600), warningIcon);
        iconPulse.setFromX(1.0);
        iconPulse.setFromY(1.0);
        iconPulse.setToX(1.3);
        iconPulse.setToY(1.3);
        iconPulse.setAutoReverse(true);
        iconPulse.setCycleCount(Animation.INDEFINITE);

        Scene scene = new Scene(root);
        alertStage.setScene(scene);
        pulseTimeline.play();
        glowTimeline.play();
        iconPulse.play();
        alertStage.showAndWait();
        pulseTimeline.stop();
        glowTimeline.stop();
        iconPulse.stop();
    }

    // =====================================================================
    //  INTRUSION ALERT (Feature 2)
    // =====================================================================
    private void showIntrusionAlert() {
        Stage intrusionStage = new Stage();
        intrusionStage.initModality(Modality.APPLICATION_MODAL);
        intrusionStage.initStyle(StageStyle.UNDECORATED);

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #0a0000;");
        root.setPrefSize(800, 550);

        // Pulsating red border
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: #ff0000; -fx-border-width: 3; -fx-background-color: transparent;");

        VBox content = new VBox(15);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(30));

        // Skull / warning icon
        Text warningIcon = new Text("⛔");
        warningIcon.setFont(Font.font("System", FontWeight.BOLD, 70));
        warningIcon.setFill(Color.RED);

        // Title with pulsing glow
        Text title = new Text("⚠ INTRUSION DETECTED ⚠");
        title.setFont(Font.font("System", FontWeight.BOLD, 38));
        title.setFill(Color.RED);
        title.setTextAlignment(TextAlignment.CENTER);
        Glow titleGlow = new Glow(1.0);
        title.setEffect(titleGlow);

        // Separator line
        Text sep = new Text("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
        sep.setFill(Color.DARKRED);
        sep.setFont(Font.font("Monospaced", 12));

        // Animated status lines
        Text line1 = new Text("UNAUTHORIZED ACCESS ATTEMPT LOGGED");
        line1.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        line1.setFill(Color.ORANGERED);

        Text line2 = new Text("GPS TRACKER ACTIVATED");
        line2.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        line2.setFill(Color.YELLOW);

        Text line3 = new Text("LOCATION BEING TRACKED...");
        line3.setFont(Font.font("Monospaced", FontWeight.BOLD, 18));
        line3.setFill(Color.YELLOW);

        Text line4 = new Text("REPORTING TO COMMAND CENTER...");
        line4.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        line4.setFill(Color.rgb(255, 150, 50));

        // Simulated coordinates
        Text coordinates = new Text("");
        coordinates.setFont(Font.font("Monospaced", 14));
        coordinates.setFill(Color.LIMEGREEN);

        // Progress bar simulation
        Text progressText = new Text("[████████████████████] 100%");
        progressText.setFont(Font.font("Monospaced", FontWeight.BOLD, 14));
        progressText.setFill(Color.RED);

        Text sep2 = new Text("▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓▓");
        sep2.setFill(Color.DARKRED);
        sep2.setFont(Font.font("Monospaced", 12));

        Text footer = new Text("THIS INCIDENT HAS BEEN RECORDED");
        footer.setFont(Font.font("System", FontWeight.BOLD, 14));
        footer.setFill(Color.rgb(200, 200, 200));

        Button btnDismiss = new Button("ACKNOWLEDGED");
        btnDismiss.setStyle("-fx-background-color: #4a0000; -fx-text-fill: #ff6666; -fx-font-size: 14px; "
                + "-fx-font-weight: bold; -fx-padding: 10 30; -fx-border-color: #ff0000; "
                + "-fx-border-width: 2; -fx-background-radius: 6; -fx-border-radius: 6; -fx-cursor: hand;");
        btnDismiss.setOnAction(e -> intrusionStage.close());

        content.getChildren().addAll(warningIcon, title, sep, line1, line2, line3, line4, coordinates, progressText,
                sep2, footer, btnDismiss);

        // Initially hide animated elements
        line1.setOpacity(0);
        line2.setOpacity(0);
        line3.setOpacity(0);
        line4.setOpacity(0);
        coordinates.setOpacity(0);
        progressText.setOpacity(0);
        footer.setOpacity(0);
        btnDismiss.setOpacity(0);

        borderPane.setCenter(content);
        root.getChildren().add(borderPane);

        // ---- ANIMATIONS ----

        // Title glow pulsing
        Timeline glowPulse = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(titleGlow.levelProperty(), 0.4)),
                new KeyFrame(Duration.millis(400), new KeyValue(titleGlow.levelProperty(), 1.0)),
                new KeyFrame(Duration.millis(800), new KeyValue(titleGlow.levelProperty(), 0.4)));
        glowPulse.setCycleCount(Animation.INDEFINITE);

        // Warning icon shake
        TranslateTransition iconShake = new TranslateTransition(Duration.millis(80), warningIcon);
        iconShake.setFromX(-5);
        iconShake.setToX(5);
        iconShake.setAutoReverse(true);
        iconShake.setCycleCount(Animation.INDEFINITE);

        // Scale pulse for warning icon
        ScaleTransition iconScale = new ScaleTransition(Duration.millis(500), warningIcon);
        iconScale.setFromX(1.0);
        iconScale.setFromY(1.0);
        iconScale.setToX(1.2);
        iconScale.setToY(1.2);
        iconScale.setAutoReverse(true);
        iconScale.setCycleCount(Animation.INDEFINITE);

        // Sequential fade-in of text lines
        FadeTransition fadeLine1 = createFadeIn(line1, 800);
        FadeTransition fadeLine2 = createFadeIn(line2, 800);
        FadeTransition fadeLine3 = createFadeIn(line3, 800);
        FadeTransition fadeLine4 = createFadeIn(line4, 800);
        FadeTransition fadeCoords = createFadeIn(coordinates, 600);
        FadeTransition fadeProgress = createFadeIn(progressText, 600);
        FadeTransition fadeFooter = createFadeIn(footer, 600);
        FadeTransition fadeDismiss = createFadeIn(btnDismiss, 600);

        SequentialTransition sequence = new SequentialTransition(
                new PauseTransition(Duration.millis(500)),
                fadeLine1,
                new PauseTransition(Duration.millis(400)),
                fadeLine2,
                new PauseTransition(Duration.millis(300)),
                fadeLine3,
                new PauseTransition(Duration.millis(400)),
                fadeLine4,
                new PauseTransition(Duration.millis(300)),
                fadeCoords,
                fadeProgress,
                new PauseTransition(Duration.millis(500)),
                fadeFooter,
                fadeDismiss);

        // Coordinate cycling animation
        Random rng = new Random();
        Timeline coordCycle = new Timeline(new KeyFrame(Duration.millis(400), e -> {
            double lat = 23.0 + rng.nextDouble() * 5;
            double lon = 88.0 + rng.nextDouble() * 5;
            coordinates.setText(String.format("TRACKING COORDINATES: %.4f°N, %.4f°E", lat, lon));
        }));
        coordCycle.setCycleCount(Animation.INDEFINITE);

        // Progress bar animation
        String[] progressStages = {
                "[█                   ]  5%",
                "[████                ] 20%",
                "[████████            ] 40%",
                "[████████████        ] 60%",
                "[████████████████    ] 80%",
                "[████████████████████] 100%",
                "[████████████████████] LOCKED"
        };
        Timeline progressAnim = new Timeline();
        for (int i = 0; i < progressStages.length; i++) {
            final String stage = progressStages[i];
            progressAnim.getKeyFrames().add(
                    new KeyFrame(Duration.millis(i * 500), e -> progressText.setText(stage)));
        }

        // Background flash effect
        Timeline bgFlash = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(root.styleProperty(), "-fx-background-color: #0a0000;")),
                new KeyFrame(Duration.millis(200),
                        new KeyValue(root.styleProperty(), "-fx-background-color: #2a0000;")),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(root.styleProperty(), "-fx-background-color: #0a0000;")));
        bgFlash.setCycleCount(15);

        Scene scene = new Scene(root);
        intrusionStage.setScene(scene);

        // Start all animations
        glowPulse.play();
        iconShake.play();
        iconScale.play();
        sequence.play();
        coordCycle.play();
        progressAnim.play();
        bgFlash.play();

        intrusionStage.showAndWait();

        // Stop animations on close
        glowPulse.stop();
        iconShake.stop();
        iconScale.stop();
        sequence.stop();
        coordCycle.stop();
        progressAnim.stop();
        bgFlash.stop();
    }

    private FadeTransition createFadeIn(javafx.scene.Node node, double durationMs) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMs), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        return ft;
    }

    // =====================================================================
    //  DECRYPTION HANDLER
    // =====================================================================
    private void handleDecrypt(SecureMessage message) {
        // Emergency messages open directly — no key needed
        if (message.isEmergency()) {
            showEmergencyPopup(message);
            MessageService.getInstance().markAsRead(message);
            refreshMessages();
            return;
        }

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Decryption Check");
        dialog.setHeaderText("Enter Decryption Key");

        ButtonType loginButtonType = new ButtonType("Decrypt", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        PasswordField password = new PasswordField();
        password.setPromptText("Decryption Key");

        grid.add(new Label("Decryption Key:"), 0, 0);
        grid.add(password, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return (password.getText() != null) ? password.getText().trim() : "";
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(key -> {
            AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();
            String statedAppointment = currentUser.getRank().name();

            try {
                // Step 1: Check Access
                boolean accessGranted = checkAccess(message, statedAppointment);

                if (accessGranted) {
                    // Step 2: Decrypt
                    String effectiveKey = key;
                    String decrypted = MessageService.getInstance().attemptDecryption(message, effectiveKey);

                    // Show Content in Custom Dialog
                    Dialog<Void> dialogContent = new Dialog<>();
                    dialogContent.setTitle("Decrypted Message");
                    dialogContent.setHeaderText("Access Granted (Confidential):");

                    ButtonType closeButtonType = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
                    dialogContent.getDialogPane().getButtonTypes().add(closeButtonType);

                    // Check if it's a Mission Brief
                    if (decrypted.startsWith("[MISSION BRIEF]")) {
                        VBox briefBox = buildMissionBriefView(decrypted);
                        dialogContent.getDialogPane().setContent(briefBox);
                    } else {
                        TextArea txtContent = new TextArea(decrypted);
                        txtContent.setEditable(false);
                        txtContent.setWrapText(true);
                        txtContent.getStyleClass().add("terminal-input");
                        txtContent.setPrefHeight(200);
                        txtContent.setPrefWidth(400);
                        dialogContent.getDialogPane().setContent(txtContent);
                    }

                    dialogContent.getDialogPane().getStylesheets()
                            .add(getClass().getResource("/com/wartime/system/view/styles.css").toExternalForm());
                    dialogContent.getDialogPane().getStyleClass().add("standard-root");
                    dialogContent.getDialogPane()
                            .setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white;");

                    dialogContent.showAndWait();

                    // Mark as read after viewing & reset failed attempts
                    MessageService.getInstance().markAsRead(message);
                    failedAttempts.remove(message);
                    refreshMessages();
                } else {
                    // Access Denied → Styled denial message
                    Alert error = new Alert(Alert.AlertType.WARNING);
                    error.setTitle("ACCESS DENIED");
                    error.setHeaderText(null);

                    VBox deniedBox = new VBox(10);
                    deniedBox.setAlignment(Pos.CENTER);
                    deniedBox.setPadding(new Insets(20));
                    deniedBox.setStyle("-fx-background-color: #1a0000; -fx-border-color: #8B0000; "
                            + "-fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8;");

                    Text deniedIcon = new Text("⛔");
                    deniedIcon.setFont(Font.font("System", FontWeight.BOLD, 40));

                    Text deniedTitle = new Text("ACCESS DENIED");
                    deniedTitle.setFont(Font.font("System", FontWeight.BOLD, 22));
                    deniedTitle.setFill(Color.RED);

                    Text deniedMsg = new Text("You are not authorized for this transmission.");
                    deniedMsg.setFont(Font.font("System", FontWeight.NORMAL, 14));
                    deniedMsg.setFill(Color.rgb(200, 200, 200));

                    Text rankInfo = new Text("Your clearance level: " + currentUser.getRank().name());
                    rankInfo.setFont(Font.font("Monospaced", 12));
                    rankInfo.setFill(Color.rgb(150, 150, 150));

                    deniedBox.getChildren().addAll(deniedIcon, deniedTitle, deniedMsg, rankInfo);

                    error.getDialogPane().setContent(deniedBox);
                    error.getDialogPane().setStyle("-fx-background-color: #1a0000;");
                    error.showAndWait();
                }

            } catch (Exception e) {
                // Wrong key — track attempts
                int attempts = failedAttempts.getOrDefault(message, 0) + 1;
                failedAttempts.put(message, attempts);

                if (attempts >= 3) {
                    // INTRUSION ALERT!
                    failedAttempts.put(message, 0); // Reset counter
                    showIntrusionAlert();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Decryption Failed");
                    error.setHeaderText(null);
                    error.setContentText(e.getMessage() + " (Attempt " + attempts + "/3)");
                    error.showAndWait();
                }
            }
        });
    }

    private boolean checkAccess(SecureMessage msg, String statedAppointment) {
        AbstractUser currentUser = SecurityContext.getInstance().getCurrentUser();

        // Commander can always access
        if (currentUser.getRank() == Rank.COMMANDER)
            return true;

        // If it's a group message — only group members can decrypt
        if (msg.getTargetGroup() != null) {
            return msg.getTargetGroup().isMember(currentUser);
        }

        // If it's a private individual message — target user can ALWAYS decrypt (any rank)
        if (msg.getTargetUser() != null) {
            return msg.getTargetUser().getName().equalsIgnoreCase(currentUser.getName());
        }

        // Rank-based broadcast — strict hierarchy enforced
        Rank userRank = currentUser.getRank();
        Rank targetRank = msg.getTargetRank();
        if (targetRank == null) {
            targetRank = msg.getSenderRank();
        }

        return userRank.ordinal() <= targetRank.ordinal();
    }

    // =====================================================================
    //  MISSION BRIEF RENDERER
    // =====================================================================
    private VBox buildMissionBriefView(String decrypted) {
        VBox box = new VBox(12);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #1A1F14; -fx-border-color: #582F0E; -fx-border-width: 2; "
                + "-fx-border-radius: 8; -fx-background-radius: 8;");
        box.setPrefWidth(450);

        Text header = new Text("══════ MISSION BRIEF ══════");
        header.setFont(Font.font("Monospaced", FontWeight.BOLD, 16));
        header.setFill(Color.rgb(164, 172, 134)); // #A4AC86

        // Parse the fields
        String[] lines = decrypted.split("\n");
        for (String line : lines) {
            if (line.startsWith("[MISSION BRIEF]")) continue;
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;

            int colonIdx = trimmed.indexOf(':');
            if (colonIdx > 0) {
                String label = trimmed.substring(0, colonIdx + 1);
                String value = trimmed.substring(colonIdx + 1).trim();

                HBox row = new HBox(10);
                row.setAlignment(Pos.CENTER_LEFT);

                Text labelText = new Text(label);
                labelText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                labelText.setFill(Color.rgb(232, 232, 232)); // #E8E8E8

                Text valueText = new Text(value);
                valueText.setFont(Font.font("Monospaced", FontWeight.NORMAL, 14));
                valueText.setFill(Color.rgb(0, 255, 0)); // green

                row.getChildren().addAll(labelText, valueText);
                box.getChildren().add(row);
            }
        }

        // Add header at top
        box.getChildren().add(0, header);

        return box;
    }
}
