package ga.game.gui;

import ga.game.weapon.WeaponController;
import ga.engine.core.Application;
import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.game.PlayerController;
import ga.game.entity.HealthComponent;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Affine;

public class HUD extends GameComponent {

    private GameObject player = null;
    private PlayerController controller;
    private WeaponController weaponController;
    private HealthComponent health;
    private final Font fontSmall, fontLarge;

    public HUD() {
        fontSmall = ResourceManager.getFont("fonts/GeosansLight.ttf", 20);
        fontLarge = ResourceManager.getFont("fonts/GeosansLight.ttf", 37);
    }

    @Override
    public void sceneStart() {
        player = GameObject.findObjectWithTag("player");
        if (player != null) {
            controller = (PlayerController) player.getComponent(PlayerController.class);
            weaponController = (WeaponController) controller.arm.getComponent(WeaponController.class);
            health = (HealthComponent) player.getComponent(HealthComponent.class);
        } else {
            throw new NullPointerException("HUD: There is no GameObject with tag Player");
        }
        Application.getScene().checkState();
    }

    @Override
    public void render(GraphicsContext g) {
        if (player != null) {
            g.setTransform(new Affine());
            g.setFill(new Color(0, 0.6, 0.9, 0.4));
            g.setGlobalAlpha(1);
            g.fillRect(10, Application.getHeight() - 110, 275, 65);
            g.fillRect(10, Application.getHeight() - 140, 125, 25);
            g.setFill(Color.BLACK);
            g.setFont(fontLarge);
            g.fillText(String.valueOf(weaponController.getSelected().ammo), 20, Application.getHeight() - 50 - 25);
            g.fillText((int) health.getHealth() + " / " + (int) health.getMaxHealth(), 120, Application.getHeight() - 50 - 25 + 12);
            g.setFont(fontSmall);
            g.fillText("Enemies left: " + Application.getScene().getEnemies(), 12, Application.getHeight() - 120, 125);
            g.setFill(new Color(0, 0, 0, 0.75));
            if (weaponController.getSelected().spareAmmo == -1) {
                g.fillText("Unlimited", 20, Application.getHeight() - 50 - 25 + 22);
            } else {
                g.fillText(String.valueOf(weaponController.getSelected().spareAmmo), 20, Application.getHeight() - 50 - 25 + 22);
            }
        }
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES_NONE;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
    }

    @Override
    public GameComponent instantiate() {
        return new HUD();
    }
}