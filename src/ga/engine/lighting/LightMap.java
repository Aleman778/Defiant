package ga.engine.lighting;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlendMode;

public class LightMap extends Canvas {
    
    private AmbientLight ambientLight;
    private final Set<Light> lights;
    private final GraphicsContext g;
    
    public LightMap(double width, double height) {
        super(width, height);
        this.lights = new HashSet<>();
        this.ambientLight = null;
        this.g = getGraphicsContext2D();
        setBlendMode(BlendMode.MULTIPLY);
    }
    
    public void setAmbientLight(AmbientLight light) {
        ambientLight = light;
    }
    
    public void refresh() {
        g.clearRect(0, 0, getWidth(), getHeight());
        if (ambientLight == null)
            return;
        g.setFill(ambientLight.color);
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
