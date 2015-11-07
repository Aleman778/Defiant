package ga.engine.lighting;

import ga.engine.physics.Vector2D;
import ga.engine.scene.GameComponent;
import javafx.scene.paint.Color;

public abstract class Light extends GameComponent {
    
    public Color color;
    
    public abstract Vector2D getLightPosition();
}
