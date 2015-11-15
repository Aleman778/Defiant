package ga.engine.lighting;

import com.sun.javafx.geom.Rectangle;
import ga.engine.core.Application;
import ga.engine.scene.GameComponent;
import ga.engine.scene.GameObject;
import java.util.List;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;

public abstract class Light extends GameComponent {
    
    public Canvas lightMap;
    protected Color color;
    protected GraphicsContext g;
    protected double intensity;

    public Light() {
        color = Color.WHITE;
        lightMap = new Canvas();
        lightMap.setBlendMode(BlendMode.ADD);
        g = lightMap.getGraphicsContext2D();
    }

    @Override
    public void start() {
        if (Application.getScene() != null) {
            System.exit(1000);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
    
    public List<GameObject> getLitObjects() {
        Rectangle bounds = gameobject.localAABB();
        for (GameObject object: Application.getScene().getAllGameObjects()) {
            Rectangle otherBounds = object.localAABB();
            if (otherBounds.contains(bounds)) {
                float x = otherBounds.x - bounds.x;
                float y = otherBounds.y - bounds.y;
                g.setFill(Color.GREEN);
                g.fillRect(x, y, otherBounds.width, otherBounds.height);
            }
        }
        return null;
    }

    public abstract void refresh();
}
