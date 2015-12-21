package ga.game;

import ga.engine.animation.AnimationController;
import ga.engine.core.Application;
import ga.engine.input.Input;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.rendering.SpriteRenderer;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;

public class WeaponController extends GameComponent {

    private int index = 0;
    private Weapon selected;
    private ParticleEmitter spark;
    private AnimationController AC;
    private SpriteRenderer image;

    private List<Weapon> weapons = new ArrayList<Weapon>() {
        {
            add(Weapon.loadXML("weapons/Pistol.weapon"));
            add(Weapon.loadXML("weapons/SMG.weapon"));
            add(Weapon.loadXML("weapons/Flamethrower.weapon"));
            add(Weapon.loadXML("weapons/CR.weapon"));
            add(Weapon.loadXML("weapons/GEP.weapon"));
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
        if ((Application.now - w.lastFire) / 1000000 > w.cooldown && !AC.getState().equals("reload") && selected.ammo != 0) {
            w.lastFire = Application.now;
            double dir = Math.toRadians(gameobject.transform.scale.x == -1 ? 180 + gameobject.transform.rotation : gameobject.transform.rotation);
            dir += -(w.spread / 2) / 10 + Math.random() * (w.spread / 2) / 10;
            GameObject projectile = w.fire(dir);
            Vector2D end = gameobject.getParent().transform.position.add(transform.position).add(new Vector2D(w.getImage().getWidth() / 2, w.getImage().getHeight() / 2).mul(new Vector2D(Math.cos(dir), Math.sin(dir))));
            if (projectile != null) {
                projectile.getTransform().position = end;
                Application.getScene().getRoot().queueObject(projectile);
            }
            spark.direction = (float) Math.toDegrees(dir);
            spark.object.transform.position = transform.position.add(new Vector2D(w.getImage().getWidth(), w.getImage().getHeight()).mul(new Vector2D(Math.cos(dir), Math.sin(dir))));
            spark.fire();
            gameobject.transform.rotation -= (10 + w.spread) * gameobject.transform.scale.x;
            Point p = MouseInfo.getPointerInfo().getLocation();
//            Input.setMousePosition(new Vector2D(p.x, p.y).add(new Vector2D(0, -w.recoil)));
            selected.ammo--;

            if (selected.reload != 0 && selected.ammo == 0 && selected.spareAmmo > 0) {
                AC.setState("reload");
                selected.lastReload = Application.now;
                Input.mouseButton = MouseButton.NONE;
            }
        }
    }

    @Override
    public void render(GraphicsContext g) {
        getSelected().render(g);
    }

    @Override
    public void fixedUpdate() {
        if (spark == null) {
            init();
        }
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
            Transform2D t = spark.transform;
            spark = selected.spark;
            spark.transform = t;
            gameobject.parent.queueComponent(spark);
            Input.scrollPosition = 0;
            AC.addAnimation("reload", selected.reloadAnimation);
            AC.addAnimation("idle", selected.idleAnimation);
        }
        if (Input.getMouseButton(MouseButton.PRIMARY)) {
            fire();
            if (selected.single) {
                Input.mouseButton = MouseButton.NONE;
            }
        }
        if (Input.getKeyPressed(KeyCode.R) && selected.ammo != selected.clipSize && selected.spareAmmo > 0) {
            AC.setState("reload");
            selected.lastReload = Application.now;
            Input.mouseButton = MouseButton.NONE;
        }
        if ((Application.now - selected.lastReload) / 1000000 > selected.reload && AC.getState().equals("reload")) {
            AC.setState("idle");
            int ammoToLoad = Math.min(selected.clipSize - selected.ammo, selected.spareAmmo);
            selected.spareAmmo -= ammoToLoad;
            selected.ammo += ammoToLoad;
        }
    }

    private void init() {
        spark = ParticleEmitter.loadXML("particles/systems/Spark.psystem");
        gameobject.parent.queueComponent(spark);
        AC = (AnimationController) getComponent(AnimationController.class);
        AC.addAnimation("reload", selected.reloadAnimation);
        AC.addAnimation("idle", selected.idleAnimation);
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
