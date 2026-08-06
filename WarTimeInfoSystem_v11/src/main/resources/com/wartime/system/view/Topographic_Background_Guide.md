# SafePass Architecture Guide: Integrating Topographic Backgrounds in JavaFX

This guide explains how to properly integrate the high-resolution light-gray topographic map background requested for the "Clean Intelligence" Light Mode in the JavaFX environment.

## 1. Asset Placement
1. Obtain the `topographic_grid.png` (or `.jpg`) seamlessly looping background image from the design team.
2. Place the image asset directly into your resources directory:
   `/Users/macbookair/Desktop/JAVA_ALL_PROJECTS/WarTimeInfoSystem_v11/src/main/resources/com/wartime/system/view/images/topographic_grid.png`

## 2. CSS Integration (`light_tactical.css`)
The `.topographic-bg` class has already been established in the CSS file. To activate the image background instead of relying purely on the solid `#F8FAFC` color, uncomment and update the `-fx-background-image` property in the `light_tactical.css` file:

```css
.topographic-bg {
    -fx-background-color: #F8FAFC;
    /* ACTIVATE THE IMAGE BY UNCOMMENTING BELOW */
    -fx-background-image: url('images/topographic_grid.png');
    -fx-background-repeat: repeat; /* If it's a tileable pattern */
    -fx-background-size: cover; /* Or 'auto' if it's a seamless repeating tile */
}
```

## 3. Applying to FXML Views
Both `intro.fxml` and `login.fxml` are already configured with the correct classes via the `styleClass="topographic-bg"` attribute on their root layouts (`AnchorPane` and `StackPane`, respectively).

## 4. Addressing Artifact Ghosting
If the image overlaps or "ghosts" into other FXML boundaries when transitioning between scenes using `SceneNavigator`, ensure the target `.surface-card` logic retains its fully opaque hex color (`#FFFFFF`), as was implemented in the refactored files. Because JavaFX applies root styles dynamically, transparent backgrounds will stack over remnants.

**Result**: Navigating to the Login Screen will present the crisp, white `VBox` hovering lightly over an expansive, intelligence-grade topographic map!
