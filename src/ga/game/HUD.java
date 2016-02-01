package ga.game;

import ga.game.weapon.WeaponController;
import ga.engine.core.Application;
import ga.engine.scene.GameComponent;
import ga.game.entity.HealthComponent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class HUD extends GameComponent {

    private PlayerController player;
    private WeaponController weaponController;
    private HealthComponent health;
    private Font fontLarge, fontSmall;

    @Override
    public void update() {
        if (player == null) {
            player = (PlayerController) getComponent(PlayerController.class);
            weaponController = (WeaponController) player.arm.getComponent(WeaponController.class);
            health = (HealthComponent) getComponent(HealthComponent.class);
        }
    }

    @Override
    public void render(GraphicsContext g) {
        g.setFill(new Color(0, 0.6, 0.9, 0.4));
        g.setGlobalAlpha(1);
        g.fillRect(10, Application.getHeight() - 110, 200, 65);
        g.setFill(Color.BLACK);
        g.setFont(fontLarge);
        g.fillText(String.valueOf(weaponController.getSelected().ammo), 20, Application.getHeight() - 50 - 25 + 5);
        g.setFont(fontSmall);
        g.setFill(new Color(0, 0, 0, 0.75));
        g.fillText(String.valueOf(weaponController.getSelected().spareAmmo), 20, Application.getHeight() - 50 - 25 + 22);
    }

    @Override
    public void awoke() {
        try {
            fontLarge = Font.loadFont(new FileInputStream("res/fonts/GeosansLight.ttf"), 37);
            fontSmall = Font.loadFont(new FileInputStream("res/fonts/GeosansLight.ttf"), 20);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(HUD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public GameComponent instantiate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map<String, Integer> getVars() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void xmlVar(String name, String value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
