package ga.game;

import ga.engine.physics.RigidBody;
import ga.engine.rendering.TileRenderer;
import ga.engine.scene.GameObject;

public class Wall {

    public Wall(GameObject root, double x, double y, int width, int height) {
        GameObject box = new GameObject(x + (width * 32) / 2 - 16, y + (height * 32) / 2 - 16, 0)
                .addComponent(new TileRenderer("textures/Jordlabb.png", width, height));
        RigidBody body2 = new RigidBody(0);
        box.addComponent(body2);
        root.addChild(box);
    }
}
