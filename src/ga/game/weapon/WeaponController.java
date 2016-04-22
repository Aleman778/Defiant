package ga.game.weapon;

import com.sun.javafx.geom.Rectangle;
import ga.engine.animation.AnimationController;
import ga.engine.audio.AudioController;
import ga.engine.core.Application;
import ga.engine.input.Input;
import ga.engine.physics.Body;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ParticleEmitter;
import ga.engine.rendering.SpriteRenderer;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import ga.game.PlayerController;
import java.awt.MouseInfo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class WeaponController extends GameComponent {

    private int index = 0;
    private Weapon selected;
    private ParticleEmitter spark, shell;
    private AnimationController AC;
    private SpriteRenderer image;
    private Vector2D aimVector, weaponEnd;
    public PlayerController player;
    private double playerSpeed, playerSpeedLimit;
    private AudioController audioController;

    private List<Weapon> weapons = new ArrayList<Weapon>() {
        {
            add(Weapon.loadXML("weapons/Pistol.weapon"));
            add(Weapon.loadXML("weapons/SMG.weapon"));
            add(Weapon.loadXML("weapons/Flamethrower.weapon"));
            add(Weapon.loadXML("weapons/CR.weapon"));
            add(Weapon.loadXML("weapons/GEP.weapon"));
            add(Weapon.loadXML("weapons/Grenadelauncher.weapon"));
            add(Weapon.loadXML("weapons/Nitrogenthrower.weapon"));
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
            GameObject projectile;
            int shells = 1;
            switch (w.type) {
                case "shotgun":
                    for (int i = 0; i < Integer.parseInt(w.config.get("pellets")); i++) {
                        dir += -(w.spread / (AC.getState().equals("aiming") ? 4 : 2)) / 10 + Math.random() * (w.spread / (AC.getState().equals("aiming") ? 4 : 2)) / 5;
                        projectile = w.fire(dir);
                        if (projectile != null) {
                            projectile.getTransform().position = weaponEnd;
                            Application.getScene().getRoot().queueObject(projectile);
                        }
                        shells = Integer.parseInt(w.config.get("pellets"));
                    }
                    break;
                default:
                    dir += -(w.spread / (AC.getState().equals("aiming") ? 8 : 2)) / 10 + Math.random() * (w.spread / (AC.getState().equals("aiming") ? 8 : 2)) / 5;
                    projectile = w.fire(dir);
                    if (projectile != null) {
                        projectile.getTransform().position = weaponEnd;
                        Application.getScene().getRoot().queueObject(projectile);
                    }
                    break;
            }
            spark.direction = (float) Math.toDegrees(dir);
            spark.object.transform.position = transform.position.add(transform.position).add(new Vector2D(getSelected().getImage().getWidth() / 2 + 5, getSelected().getImage().getWidth() / 2).mul(new Vector2D(Math.cos(dir), Math.sin(dir))));
            shell.direction = spark.direction - 90;
            spark.transform.depth = -1;
            spark.fire();
            shell.object.transform.position = spark.object.transform.position;
            shell.object.transform.position = shell.object.transform.position.mul(new Vector2D(1 / 1.4, 1)).sub(new Vector2D(0, 5));
            if (!(getSelected().type.equals("flamethrower") || getSelected().type.equals("nitrogenthrower"))) {
                shell.fire(shells);
                audioController.play("weapon");
            }
            gameobject.transform.rotation -= (10 + w.spread) * gameobject.transform.scale.x;
            Point p = MouseInfo.getPointerInfo().getLocation();
//                    Input.setMousePosition(new Vector2D(p.x, p.y).add(new Vector2D(0, -w.recoil)));
            selected.ammo--;
        }
        if (selected.reload != 0 && selected.ammo == 0 && (selected.spareAmmo > 0 || selected.spareAmmo == -1) && !AC.getState().equals("reload")) {
            AC.setState("reload");
            selected.lastReload = Application.now;
            Input.mouseButtons.clear();
        }
    }

    @Override
    public void render(GraphicsContext g) {
        getSelected().render(g);
        if (AC.getState().equals("aiming")) {
            g.setTransform(new Affine());
            g.setStroke(Color.RED);
//            if (AC.getState().equals("aiming")) {
//                g.setStroke(Color.GREEN);
//            }
            if (aimVector != null && getSelected().sights && !AC.getState().equals("reload")) {
                g.strokeLine(weaponEnd.x + Application.getScene().getRoot().transform.position.x, weaponEnd.y + Application.getScene().getRoot().transform.position.y, aimVector.x + Application.getScene().getRoot().transform.position.x, aimVector.y + Application.getScene().getRoot().transform.position.y);
            }
        }
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
            shell = selected.shell;
            spark.transform = t;
            gameobject.parent.queueComponent(spark);
            gameobject.parent.queueComponent(shell);
            Input.scrollPosition = 0;
            AC.addAnimation("reload", selected.reloadAnimation);
            AC.addAnimation("idle", selected.idleAnimation);
            AC.addAnimation("aiming", selected.idleAnimation);
            AC.setState("idle");
        }

        double dir = Math.toRadians(gameobject.transform.scale.x == -1 ? 180 + gameobject.transform.rotation : gameobject.transform.rotation);
        weaponEnd = gameobject.getParent().transform.position.add(transform.position.add(new Vector2D(0, 12))).add(new Vector2D(getSelected().getImage().getWidth() / 2, getSelected().getImage().getWidth() / 2).mul(new Vector2D(Math.cos(dir), Math.sin(dir))));
        if (getSelected().sights) {
            if (weaponEnd != null) {
                Vector2D line = new Vector2D(5000 * Math.cos(dir), 5000 * Math.sin(dir));
                List<GameObject> objects = Application.getScene().getAllGameObjects();
                objects.remove(gameobject.parent);
                aimVector = weaponEnd.add(line.mul(getIntersections(weaponEnd, line, getBounds(objects)).get(0)));
            }
            if (aimVector == null) {
                aimVector = weaponEnd.add(new Vector2D(5000 * Math.cos(dir), 5000 * Math.sin(dir)));
            }
        }

        if (Input.getMouseButton(MouseButton.PRIMARY)) {
            fire();
            if (selected.single) {
                Input.mouseButtons.remove(MouseButton.PRIMARY);
            }
        }
        if (Input.getMouseButton(MouseButton.SECONDARY) && !AC.getState().equals("reload") && selected.sights == true) {
            AC.setState("aiming");
            player.SPEED = playerSpeed / 2;
            player.gameobject.getBody().SPEED_LIMIT = playerSpeedLimit / 2;
        } else if (AC.getState().equals("aiming")) {
            AC.setState("idle");
            player.SPEED = playerSpeed;
            player.gameobject.getBody().SPEED_LIMIT = playerSpeedLimit;
        }
        if (Input.getKeyPressed(KeyCode.R) && selected.ammo != selected.clipSize && (selected.spareAmmo > 0 || selected.spareAmmo == -1)) {
            AC.setState("reload");
            selected.lastReload = Application.now;
            Input.mouseButtons.clear();
        }
        if ((Application.now - selected.lastReload) / 1000000 > selected.reload - (selected.ammo > 0 ? selected.reload * 0.4 : 0) && AC.getState().equals("reload")) {
            AC.setState("idle");
            int ammoToLoad;
            if (selected.spareAmmo == -1) {
                ammoToLoad = selected.clipSize - selected.ammo;
            } else {
                ammoToLoad = Math.min(selected.clipSize - selected.ammo, selected.spareAmmo);
                selected.spareAmmo -= ammoToLoad;
            }
            selected.ammo += ammoToLoad;
        }
    }

    private void init() {
        spark = ParticleEmitter.loadXML("particles/systems/Spark.psystem");
        shell = selected.shell;
        gameobject.parent.queueComponent(spark);
        gameobject.parent.queueComponent(shell);
        AC = (AnimationController) getComponent(AnimationController.class);
        AC.addAnimation("reload", selected.reloadAnimation);
        AC.addAnimation("idle", selected.idleAnimation);
        playerSpeed = player.SPEED;
        playerSpeedLimit = player.gameobject.getBody().SPEED_LIMIT;
        audioController = (AudioController) player.getComponent(AudioController.class);
    }

    private static Vector2D[][] getBounds(List<GameObject> objects) {
        Vector2D[][] list = new Vector2D[objects.size() * 4][2];
        int index = 0;
        for (GameObject o : objects) {
            Body body = (Body) o.getComponent(Body.class);
            if (body == null) {
                continue;
            }
            if (body.getClass() == SimpleBody.class || body.getID() == 2) {
                continue;
            }
            Rectangle bounds = o.getAABB();
            double x = o.transform.position.x,
                    y = o.transform.position.y;
            list[index][0] = new Vector2D(x, y);
            list[index][1] = new Vector2D(bounds.width, 0);
            index++;

            list[index][0] = new Vector2D(x + bounds.width, y);
            list[index][1] = new Vector2D(0, bounds.height);
            index++;

            list[index][0] = new Vector2D(x, y);
            list[index][1] = new Vector2D(0, bounds.height);
            index++;

            list[index][0] = new Vector2D(x, y + bounds.height);
            list[index][1] = new Vector2D(bounds.width, 0);
            index++;
        }
        return list;
    }

    private static List<Double> getIntersections(Vector2D V1Base, Vector2D V1Size, Vector2D[][] bounds) {
        List<Double> scales = new ArrayList<>();
        for (int i = 0; i < bounds.length; i++) {
            Vector2D base = bounds[i][0], size = bounds[i][1];
            if (base == null || size == null) {
                continue;
            }
            scales.add(Vector2D.intersectScale(V1Base, V1Size, base, size, true));
        }
        scales.add(1.0);
        Collections.sort(scales);
        scales.removeAll(Collections.singleton(0.0));
        return scales;
    }

    @Override
    public GameComponent instantiate() {
        return new WeaponController();
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES_NONE;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
    }
}
