# SafePass UI Refactor: Mapping Guide

Pursuant to the **"Obsidian & Emerald" Design System Blueprint** and the structural requirements for a **Master-Detail** architecture, the FXML templates have been heavily refactored. Because of the strict stricture regarding a **Feature Freeze on Controller logic**, this guide clarifies how the newly generated layouts map to existing backend bindings.

## 1. `tactical_modern.css` mapping
**Integration**: The `styles.css` can be entirely replaced by `tactical_modern.css`, or the primary `SceneNavigator` can be updated to load `tactical_modern.css` instead of `styles.css`.
- The new CSS leverages `-fx-control-inner-background`, `transparent` borders/backgrounds, and specific `rgba` glow combinations to bypass default JavaFX styling without needing Java Controller intervention.
- The `SplitPane` dividers have been deliberately flattened and styled to maintain the "No-Line Rule" described in the blueprint.

## 2. `MainDashboard.fxml` mapping to `OptionController.java`
**Role**: Replaces `option.fxml` as the root entry point post-login.
- **Controller Binding**: Uses `fx:controller="com.wartime.system.controller.OptionController"`.
- **Navigation Rails**: 
  - The 60px Left Global Rail holds the `btnLogout` component directly mapped to the existing `@FXML Button btnLogout`.
  - The 280px Sub-Rail holds `btnSend`, `btnDecrypt`, and `btnCreateGroup`, mapping perfectly to the `OptionController` interactions.
- **Visual Update**: Rather than a series of disconnected, centered cards, the dashboard is now a persistently laid out `BorderPane` where the "fluid canvas" acts as a placeholder graphic.

## 3. `MessageCenter.fxml` mapping to `SenderController.java`
**Role**: Replaces `sender.fxml` specifically for message composition operations.
- **Controller Binding**: Uses `fx:controller="com.wartime.system.controller.SenderController"`.
- **Layout Adjustments**: 
  - The Top Navigation options (Target Group, Individual Recipient, Appointment) have been moved out of the center and into the **280px Navigation Rail**, making it appear as if the user is selecting a channel on the left and chatting on the right.
  - The **Composer Area** (TextArea, Mission Brief Toggles) has been moved to the bottom of the Fluid Canvas.
  - The **Encryption Strategy** dropdown (`cmbEncryption`) and **Manual Key** field (`txtKey`) have been docked natively into the composer bottom bar next to the `TRANSMIT` (`btnSend`) button, satisfying the feature-to-prototype mapping requirement.
  - The `btnReturn` is situated on the 60px global rail, acting as the global "back/home" button.

## 4. Secure Receiver Note
**Role**: `receiver.fxml` and `ReceiverController.java`.
While this operation focused on creating the composite `MessageCenter.fxml` layout containing the Sender bindings, achieving the "Gaussian Blur" decryption challenge strictly via FXML and CSS is done using a `<StackPane>` overlay with the `-fx-effect: gaussianBlur(15);` styling. 
Because `ReceiverController.java` currently handles decryption via a distinct `Dialog<String>` popup window defined entirely in Java (`Dialog<String> dialog = new Dialog<>();`), building a native in-canvas overlay that clears upon entry *cannot* be fully activated without removing the `new Dialog<String>()` logic from the controller.
**Recommended Future-State**: Remove the Java `Dialog`, map the prompt to a `<StackPane fx:id="blurOverlay">`, and use `blurOverlay.setVisible(false)` locally within the `handleDecrypt` execution inside `ReceiverController.java`.
