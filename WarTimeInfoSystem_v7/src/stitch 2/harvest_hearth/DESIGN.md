# Design System Strategy: The Digital Harvest

## 1. Overview & Creative North Star
**Creative North Star: "The Modern Agrarian"**
This design system rejects the clinical, "big-box" feel of traditional e-commerce. Instead, we embrace a high-end editorial aesthetic that mimics the experience of a boutique farmers' market. The interface feels organic and curated, moving away from rigid, boxy grids toward a "fluid-shelf" layout. 

To break the "template" look, we employ **intentional asymmetry**. Product descriptions may bleed across grid lines, and hero imagery should use overlapping elements where a fresh produce asset (e.g., a sprig of rosemary) sits atop a `surface-container` card, breaking the container's boundary. This creates a sense of tactile depth and premium quality.

---

## 2. Colors
Our palette balances the vitality of `primary` greens with the grounding reliability of `secondary` earthy tones.

- **The "No-Line" Rule:** Sectioning must never be achieved with 1px solid borders. Use background shifts instead. A featured product section should use `surface-container-low` to distinguish itself from the `background` white, creating soft, natural boundaries.
- **Surface Hierarchy & Nesting:** Treat the UI as layers of fine vellum. 
    - **Base Layer:** `surface` (#f8f9fa)
    - **Content Blocks:** `surface-container-low` (#f3f4f5)
    - **Interactive Cards:** `surface-container-lowest` (#ffffff) to provide a "pop" of clean white against the off-white background.
- **The "Glass & Gradient" Rule:** For floating navigation or "Quick Add" overlays, use a Glassmorphism effect: `surface` color at 70% opacity with a `20px` backdrop-blur. 
- **Signature Textures:** Main CTAs should not be flat. Apply a subtle linear gradient from `primary` (#0f5238) to `primary_container` (#2d6a4f) at a 135-degree angle to give buttons a "sun-dappled" depth.

---

## 3. Typography
We pair the geometric friendliness of **Plus Jakarta Sans** with the high-utility readability of **Manrope**.

- **Display & Headlines (Plus Jakarta Sans):** Used for brand storytelling and category headers. The generous x-height feels welcoming. Use `headline-lg` for product names to convey authority.
- **Body & Labels (Manrope):** Chosen for its exceptional legibility in dense data environments like nutrition facts or price comparisons.
- **Hierarchy as Brand:** Use `letter-spacing: -0.02em` on all `display` scales to create a tight, editorial "masthead" feel. Contrast this with `label-md` in all-caps with `0.05em` spacing for category badges to denote a premium, organized system.

---

## 4. Elevation & Depth
In this system, depth is felt, not seen. We avoid the heavy drop-shadows of the early web.

- **The Layering Principle:** Stack your containers. An "Organic Certification" badge (`tertiary_fixed`) should sit directly on a `surface-container-lowest` card. The color contrast provides the elevation.
- **Ambient Shadows:** For elevated states (e.g., a card on hover), use a "Botanical Shadow": `box-shadow: 0 20px 40px rgba(25, 28, 29, 0.04)`. The tint is derived from `on_surface`, ensuring the shadow feels like a natural obstruction of light.
- **The "Ghost Border" Fallback:** If high-contrast accessibility is required, use `outline_variant` (#bfc9c1) at **15% opacity**. It should be a whisper of a line, barely perceptible but structurally functional.

---

## 5. Components

### Buttons: The Tactile Interaction
- **Primary:** Gradient-filled (`primary` to `primary_container`) with `DEFAULT` (0.5rem) roundedness. Text is `on_primary` (#ffffff).
- **Secondary:** Use `secondary_fixed` background with `on_secondary_fixed` text. This earthy tone provides a "warm" alternative for less urgent actions like "Add to List."
- **Tertiary:** Text-only using `primary` color, bold weight, with a `2px` underline in `primary_fixed` to signal interactivity without bulk.

### Product Cards
- **Structure:** No borders. Use `surface-container-lowest` as the card base. 
- **Spacing:** Use `spacing-4` (1rem) internal padding. 
- **Interaction:** On hover, the card should scale slightly (1.02x) and transition from `surface-container-lowest` to a subtle `surface_bright` with an Ambient Shadow.

### Category Badges
- **Styling:** Use `secondary_container` (#fdc39a) for "Earthy/Pantry" items and `primary_fixed` (#b1f0ce) for "Fresh/Produce."
- **Shape:** Use `full` (9999px) roundedness to create a soft, pill-shaped organic feel.

### Input Fields
- **Surface:** Use `surface-container-highest` (#e1e3e4) for the input track.
- **Focus State:** Transition the background to `surface_container_lowest` and add a `2px` "Ghost Border" using `primary`.

### Cards & Lists: The Separation Rule
**Forbid dividers.** To separate cart items, use `spacing-6` (1.5rem) of vertical white space or alternate background tints between `surface` and `surface-container-low`.

---

## 6. Do's and Don'ts

### Do
- **Do** use whitespace as a functional element. If a layout feels crowded, increase spacing using the `spacing-12` or `spacing-16` tokens.
- **Do** lean into the `tertiary` (warm ochre/orange) tones for sales or "seasonal pick" highlights to create heat-map interest.
- **Do** ensure all product photography is shot against neutral, warm-grey backgrounds to align with our `surface` tokens.

### Don't
- **Don't** use pure black (#000000) for text. Use `on_surface` (#191c1d) to maintain the soft, organic feel.
- **Don't** use `none` roundedness. Even "sharp" elements should have at least `sm` (0.25rem) rounding to remain "friendly."
- **Don't** use high-saturation red for errors. Use the sophisticated `error` (#ba1a1a) paired with `error_container` for a more muted, trustworthy warning.