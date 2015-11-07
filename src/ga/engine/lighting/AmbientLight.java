package ga.engine.lighting;

import ga.engine.physics.Vector2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class AmbientLight extends Light{
    
    private Image lightmap;
    
    public AmbientLight(Color color) {
        this.color = color;
    }

    @Override
    public Vector2D getLightPosition() {
        return new Vector2D(0, 0);
    }
}
