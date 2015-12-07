package ga.game;

import ga.engine.core.Application;
import ga.engine.input.Input;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;

public class WeaponController extends GameComponent {
    
    private int index = 0;
    private Weapon selected;
    
    private List<Weapon> weapons = new ArrayList<Weapon>() {
        {
            add(Weapon.loadXML("weapons/Pistol.weapon"));
            add(Weapon.loadXML("weapons/Large.weapon"));
            add(Weapon.loadXML("weapons/Flamethrower.weapon"));
        }
    };
    
    public WeaponController() {
        selected = weapons.get(index);
    }
    
    public Weapon getSelected() {
        return selected;
    }
    
    public void fire() {
        Weapon w = getSelected();
        if ((Application.now - w.lastFire) / 1000000 > w.cooldown) {
            w.lastFire = Application.now;
            GameObject projectile = w.fire(gameobject.transform.scale.x == -1 ? 180 + gameobject.transform.rotation : gameobject.transform.rotation);
            projectile.getTransform().position = gameobject.getParent().transform.position;
            Application.getScene().getRoot().addObject(projectile);
        }
    }

    @Override
    public void render(GraphicsContext g) {
        getSelected().render(g);
    }

    @Override
    public void fixedUpdate() {
        //Weapon Select
        if (Input.getScrollPosition() != 0) {
            index -= Input.getScrollPosition();
            if (index < 0) {
                index = weapons.size() - 1;
            }
            if (index > weapons.size() - 1) {
                index = 0;
            }
            selected = weapons.get(index);
            Input.scrollPosition = 0;
        }
        if (Input.getMouseButton(MouseButton.PRIMARY)) {
            fire();
            if (selected.single) {
                Input.mouseButton = MouseButton.NONE;
            }
        }
    }
    
    @Override
    public GameComponent instantiate() {
        return null;
    }

    @Override
    public Map<String, Integer> getVars() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void xmlVar(String name, String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
