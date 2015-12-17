package ga.engine.rendering;

import com.sun.javafx.geom.Rectangle;
import ga.engine.physics.Body;
import ga.engine.physics.SimpleBody;
import ga.engine.physics.Vector2D;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Particle {

    public float size, life, lifeTime, velocity;
    public String shape;
    public SimpleBody body;
    public Image sprite;
    public Color color;
    public boolean interpolate;
    protected Vector2D lastPosition;
    protected GameObject emitter;

    public Particle(GameObject emitter, Vector2D position, Vector2D velocity, float size, float life, Color color, boolean eventOnly, int id) {
        body = new SimpleBody(new Rectangle(0, 0, (int) size / 2, (int) size / 2), 1, id);
        body.addCollide(1);
        body.addNoCollide(2);
        body.eventOnly = eventOnly;
        this.emitter = emitter;
        body.gameobject = emitter;
        body.transform = new Transform2D(body.gameobject, position.x, position.y);
        body.velocity = velocity;
        this.size = (int) (0.75 * size * Math.random() + size / 2);
        this.lifeTime = life;
        this.life = life;
        this.color = color;
    }

    public boolean update() {
        life--;
        return life > 0 && size > 0;
    }

    public void render(GraphicsContext g) {
        if (interpolate) {
            if (lastPosition != null) {
                Vector2D pos = body.transform.position;
                interpolate = false;
                int x = (int) ((body.transform.position.x - lastPosition.x) / 2), y = (int) ((body.transform.position.y - lastPosition.y) / 2);
                while (x > 1 || y > 1) {
                    body.transform.position.x += x;
                    body.transform.position.y += y;
                    render(g);
                    body.transform.position.x -= 2 * x;
                    body.transform.position.y -= 2 * y;
                    render(g);
                    body.transform.position.x += x;
                    body.transform.position.y += y;
                    x /= 8;
                    y /= 8;
                }
                interpolate = true;
                body.transform.position = pos;
            }
            lastPosition = body.transform.position;
        }
        g.setGlobalAlpha(life / lifeTime);
        g.setFill(color);
        if (sprite != null) {
            g.drawImage(sprite, body.transform.position.x, body.transform.position.y, size, size);
        } else {
            switch (shape) {
                case "circle":
                    g.fillOval(body.transform.position.x - size / 2, body.transform.position.y - size / 2, size, size);
                    break;
                case "square":
                    g.fillRect(body.transform.position.x - size / 2, body.transform.position.y - size / 2, size, size);
                    break;
                default:
                    g.fillOval(body.transform.position.x - size / 2, body.transform.position.y - size / 2, size, size);
            }
            
        }
    }

    public void physicsUpdate(Body body) {
        this.body.physicsUpdate(body);
    }
}
